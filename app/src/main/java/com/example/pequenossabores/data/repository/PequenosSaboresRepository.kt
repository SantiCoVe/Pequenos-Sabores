package com.example.pequenossabores.data.repository

import android.content.Context
import com.example.pequenossabores.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.security.MessageDigest
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.io.IOException

/** Única fuente de datos: Room (usuarios, peques, favoritos) + SharedPreferences (sesión). */
class PequenosSaboresRepository(
    private val usuarioDao: UsuarioDao,
    private val pequeDao: PequeDao,
    private val favoritoDao: FavoritoDao,
    private val sesion: SesionPreferences,
    private val consumoDao: ConsumoDao
) {
    suspend fun registrar(
        nombre: String,
        correo: String,
        contrasena: String,
        rol: RolFamiliar
    ): ResultadoAuth = autenticarServidor(
        accion = "registrar",
        correo = correo,
        contrasena = contrasena,
        nombre = nombre,
        rol = rol.name
    )

    suspend fun iniciarSesion(
        correo: String,
        contrasena: String
    ): ResultadoAuth = autenticarServidor(
        accion = "login",
        correo = correo,
        contrasena = contrasena
    )

    private suspend fun autenticarServidor(
        accion: String,
        correo: String,
        contrasena: String,
        nombre: String = "",
        rol: String = "MAMA"
    ): ResultadoAuth = withContext(Dispatchers.IO) {

        val correoNormalizado = correo.trim().lowercase()

        try {
            val url = URL(
                "http://10.0.2.2/pequenos_sabores/auth.php"
            )

            val conexion = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                connectTimeout = 10000
                readTimeout = 15000
                doOutput = true
                setRequestProperty(
                    "Content-Type",
                    "application/json; charset=UTF-8"
                )
                setRequestProperty("Accept", "application/json")
            }

            try {
                val cuerpo = JSONObject().apply {
                    put("accion", accion)
                    put("correo", correoNormalizado)
                    put("contrasena", contrasena)

                    if (accion == "registrar") {
                        put("nombre", nombre.trim())
                        put("rol", rol)
                    }
                }

                conexion.outputStream.use { salida ->
                    salida.write(cuerpo.toString().toByteArray(Charsets.UTF_8))
                }

                val codigo = conexion.responseCode

                val flujo = if (codigo in 200..299) {
                    conexion.inputStream
                } else {
                    conexion.errorStream
                }

                val respuestaTexto = flujo?.bufferedReader(Charsets.UTF_8)
                    ?.use { it.readText() }
                    .orEmpty()

                    android.util.Log.d(
                        "AUTH_DEBUG",
                        "Acción: $accion | HTTP: $codigo | Respuesta: $respuestaTexto"
                    )

                val respuesta = try {
                    JSONObject(respuestaTexto)
                } catch (_: Exception) {
                    return@withContext ResultadoAuth.ERROR_SERVIDOR
                }

                if (codigo == 409) {
                    return@withContext ResultadoAuth.CORREO_EXISTENTE
                }

                if (codigo == 401) {
                    return@withContext ResultadoAuth.CREDENCIALES_INVALIDAS
                }

                if (codigo !in 200..299 || !respuesta.optBoolean("ok")) {
                    return@withContext ResultadoAuth.ERROR_SERVIDOR
                }

                val usuario = respuesta.optJSONObject("usuario")
                    ?: return@withContext ResultadoAuth.ERROR_SERVIDOR

                val token = respuesta.optString("token")
                val nombreServidor = usuario.optString("nombre", nombre)
                val rolServidor = usuario.optString("rol", rol)
                val perfilCompleto = respuesta.optBoolean("perfil_completo", false)

                if (token.isBlank()) {
                    return@withContext ResultadoAuth.ERROR_SERVIDOR
                }

                usuarioDao.insertar(
                    UsuarioEntity(
                        correo = correoNormalizado,
                        nombre = nombreServidor,
                        contrasenaHash = hash(contrasena),
                        rol = rolServidor
                    )
                )

                sesion.guardarSesion(correoNormalizado, token)
                sesion.setPerfilCompleto(perfilCompleto)

                ResultadoAuth.OK

            } finally {
                conexion.disconnect()
            }

        } catch (e: Exception) {
        android.util.Log.e(
            "AUTH_DEBUG",
            "Error en la petición de autenticación: ${e.javaClass.simpleName}: ${e.message}",
            e
        )
        ResultadoAuth.ERROR_SERVIDOR
    }
    }

    fun perfilCompleto(): Boolean = sesion.perfilCompleto()

    fun observarUsuarioActivo(): Flow<UsuarioEntity?> {
        val correo = sesion.correoActivo() ?: return flowOf(null)
        return usuarioDao.observarPorCorreo(correo)
    }

    fun cerrarSesion() = sesion.cerrarSesion()

    // ---------- Perfil del peque ----------
    suspend fun guardarPeque(
        avatar: Int, nombre: String, edadMeses: Int,
        preferencias: Set<String>, noLeGustan: String, alergias: Set<String>
    ) {
        val correo = sesion.correoActivo() ?: return
        pequeDao.insertar(
            PequeEntity(
                correoUsuario = correo,
                nombre = nombre.trim(),
                avatar = avatar,
                edadMeses = edadMeses,
                preferencias = preferencias.joinToString("|"),
                noLeGustan = noLeGustan.trim(),
                alergias = alergias.joinToString("|")
            )
        )
        sesion.setPerfilCompleto(true)
    }

    suspend fun obtenerPeque(): PequeEntity? =
        sesion.correoActivo()?.let { pequeDao.obtener(it) }

    fun observarPeque(): Flow<PequeEntity?> {
        val correo = sesion.correoActivo() ?: return flowOf(null)
        return pequeDao.observar(correo)
    }

    // ---------- Favoritos ----------
    fun observarFavoritos(): Flow<List<Int>> {
        val correo = sesion.correoActivo() ?: return flowOf(emptyList())
        return favoritoDao.observarIds(correo)
    }

    suspend fun alternarFavorito(recetaId: Int, yaEsFavorito: Boolean) {
        val correo = sesion.correoActivo() ?: return
        val fav = FavoritoEntity(correo, recetaId)
        if (yaEsFavorito) favoritoDao.eliminar(fav) else favoritoDao.insertar(fav)
    }

    // ---------- Datos de ejemplo del dashboard ----------
    fun menuDelDia() = MenuDelDia(
        titulo = "Pancakes suaves de avena, banano y canela 🥞🍌",
        momento = "Desayuno & Merienda",
        tiempoMin = 15,
        textura = "Textura suave BLW",
        kcal = 210
    )

    fun observarProgresoSemanal(): Flow<List<ProgresoGrupo>> {
        val correo = sesion.correoActivo().orEmpty()

        if (correo.isBlank()) {
            return flowOf(progresoInicial())
        }

        val (inicio, fin) = rangoSemanaActual()

        return consumoDao.observarPorPeriodo(correo, inicio, fin)
            .map { consumos ->
                val grupos = listOf(
                    Triple(Clasificador.FRUTAS, "🥬", "Frutas y verduras"),
                    Triple(Clasificador.LACTEOS, "🥛", "Lácteos y alternativas"),
                    Triple(Clasificador.CEREALES, "🌾", "Cereales y harinas"),
                    Triple(Clasificador.PROTEINAS, "🍗", "Proteínas")
                )

                grupos.map { (categoria, emoji, nombre) ->
                    val diasConsumidos = consumos
                        .filter { consumo ->
                            consumo.gruposConsumidos
                                .split("|")
                                .any { it == categoria }
                        }
                        .map { it.fecha }
                        .distinct()
                        .size

                    ProgresoGrupo(
                        nombre = nombre,
                        emoji = emoji,
                        porcentaje = (diasConsumidos * 100 / 7).coerceIn(0, 100)
                    )
                }
            }
    }

    suspend fun registrarConsumo(
        receta: Receta,
        fecha: String = fechaActual()
    ): Boolean {
        val correo = sesion.correoActivo().orEmpty()
        if (correo.isBlank()) return false

        val grupos = receta.ingredientes
            .map { ingrediente ->
                ingrediente.categoria.ifBlank {
                    Clasificador.categoria(
                        ingrediente.nombre.ifBlank { ingrediente.texto }
                    )
                }
            }
            .filter { it != Clasificador.OTROS }
            .distinct()

        if (grupos.isEmpty()) return false

        consumoDao.insertar(
            ConsumoEntity(
                correoUsuario = correo,
                recetaId = receta.id,
                nombreReceta = receta.titulo,
                fecha = fecha,
                gruposConsumidos = grupos.joinToString("|")
            )
        )

        return true
    }

    private fun progresoInicial() = listOf(
        ProgresoGrupo("Frutas y verduras", "🥬", 0),
        ProgresoGrupo("Lácteos y alternativas", "🥛", 0),
        ProgresoGrupo("Cereales y harinas", "🌾", 0),
        ProgresoGrupo("Proteínas", "🍗", 0)
    )

    /** Devuelve el lunes y el domingo de la semana actual en formato ISO. */
    private fun rangoSemanaActual(): Pair<String, String> {
        val calendario = Calendar.getInstance().apply {
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val formato = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
        val inicio = formato.format(calendario.time)

        calendario.add(Calendar.DAY_OF_YEAR, 6)
        val fin = formato.format(calendario.time)

        return inicio to fin
    }

    private fun fechaActual(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(Calendar.getInstance().time)

    private fun hash(texto: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(texto.toByteArray())
            .joinToString("") { "%02x".format(it) }

    companion object {
        fun crear(context: Context): PequenosSaboresRepository {
            val db = DatabaseProvider.getDatabase(context)

            return PequenosSaboresRepository(
                usuarioDao = db.usuarioDao(),
                pequeDao = db.pequeDao(),
                favoritoDao = db.favoritoDao(),
                sesion = SesionPreferences(context),
                consumoDao = db.consumoDao()
            )
        }
    }
}