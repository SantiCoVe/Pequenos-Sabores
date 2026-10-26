package com.example.pequenossabores.data.repository

import android.content.Context
import com.example.pequenossabores.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.security.MessageDigest

/** Única fuente de datos: Room (usuarios, peques, favoritos) + SharedPreferences (sesión). */
class PequenosSaboresRepository(
    private val usuarioDao: UsuarioDao,
    private val pequeDao: PequeDao,
    private val favoritoDao: FavoritoDao,
    private val sesion: SesionPreferences
) {

    // ---------- Autenticación ----------
    suspend fun registrar(nombre: String, correo: String, contrasena: String, rol: RolFamiliar): ResultadoAuth {
        val id = correo.trim().lowercase()
        if (usuarioDao.obtenerPorCorreo(id) != null) return ResultadoAuth.CORREO_EXISTENTE

        usuarioDao.insertar(UsuarioEntity(id, nombre.trim(), hash(contrasena), rol.name))
        sesion.guardarSesion(id)
        sesion.setPerfilCompleto(false)
        return ResultadoAuth.OK
    }

    suspend fun iniciarSesion(correo: String, contrasena: String): ResultadoAuth {
        val id = correo.trim().lowercase()
        val usuario = usuarioDao.obtenerPorCorreo(id)
        return if (usuario != null && usuario.contrasenaHash == hash(contrasena)) {
            sesion.guardarSesion(id)
            sesion.setPerfilCompleto(pequeDao.obtener(id) != null)
            ResultadoAuth.OK
        } else ResultadoAuth.CREDENCIALES_INVALIDAS
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

    fun progresoSemanal() = listOf(
        ProgresoGrupo("Frutas", "🍎", 85),
        ProgresoGrupo("Vegetales", "🥦", 70),
        ProgresoGrupo("Proteínas", "🥚", 90)
    )

    private fun hash(texto: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(texto.toByteArray())
            .joinToString("") { "%02x".format(it) }

    companion object {
        fun crear(context: Context): PequenosSaboresRepository {
            val db = DatabaseProvider.getDatabase(context)
            return PequenosSaboresRepository(
                db.usuarioDao(), db.pequeDao(), db.favoritoDao(), SesionPreferences(context)
            )
        }
    }
}