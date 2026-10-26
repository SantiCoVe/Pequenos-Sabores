package com.example.pequenossabores.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pequenossabores.ui.components.*
import com.example.pequenossabores.ui.theme.*
import com.example.pequenossabores.viewmodel.PerfilViewModel

private data class AvatarOpcion(val emoji: String, val edad: String, val etiqueta: String, val titulo: String)

private val avatares = listOf(
    AvatarOpcion("👶", "6-12 meses", "Primeros bocados", "Bebé explorador (6-12m)"),
    AvatarOpcion("🧒", "1-2 años", "Curioso", "Pequeño curioso (1-2a)"),
    AvatarOpcion("🧑‍🍳", "3-5 años", "Mini Chef", "Mini Chef (3-5a)")
)
private val edades = listOf(8 to "🥑 8 meses", 12 to "🥕 12 meses", 24 to "🍓 2 años", 36 to "🥦 3 años")
private val preferenciasOpc = listOf(
    "Frutas dulces" to "🍌", "Purés suaves" to "🥣", "Texturas BLW" to "🥕", "Legumbres" to "🫘", "Sopitas" to "🍲"
)
private val alergiasOpc = listOf(
    "Ninguna" to "", "Sin gluten / Celiaquía" to "🌾", "Sin lactosa" to "🥛",
    "Huevo" to "🥚", "Maní / Frutos secos" to "🥜", "Pescado" to "🐟"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PantallaPerfil(
    esOnboarding: Boolean,
    onTab: (Tab) -> Unit,
    onGuardado: () -> Unit,
    onCerrarSesion: () -> Unit,
    vm: PerfilViewModel = viewModel()
) {
    val s by vm.estado.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Fondo,
        topBar = { EncabezadoApp("Perfil", onCerrarSesion) },
        bottomBar = { if (!esOnboarding) BarraInferior(Tab.PERFIL, onTab) }
    ) { pad ->
        Column(
            Modifier.padding(pad).fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (esOnboarding) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(28.dp).clip(CircleShape).background(Naranja), contentAlignment = Alignment.Center) {
                        Text("3", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Spacer(Modifier.width(10.dp))
                    Text("PASO 3 DE 3 · PERFIL NUTRICIONAL", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold,
                        color = Marron, modifier = Modifier.weight(1f))
                    repeat(3) {
                        Box(Modifier.padding(start = 4.dp).width(24.dp).height(5.dp).clip(CircleShape).background(Naranja))
                    }
                }
            }

            // Encabezado
            Card(shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(Modifier.padding(20.dp)) {
                    Column(Modifier.weight(1f)) {
                        Text("¡Conozcamos a tu pequeño!", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
                        Spacer(Modifier.height(6.dp))
                        Text("PequeChef usará estos datos para formular recetas seguras y adaptadas a su etapa de crecimiento.",
                            fontSize = 14.sp, color = GrisTexto)
                    }
                    Text("👶✨", fontSize = 26.sp)
                }
            }

            // Avatar
            TarjetaSeccion("Elige su avatar favorito", Icons.Filled.Face, derecha = avatares[s.avatar].titulo) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    avatares.forEachIndexed { i, a ->
                        val sel = i == s.avatar
                        Column(
                            Modifier.weight(1f).clip(RoundedCornerShape(24.dp))
                                .background(if (sel) Color(0xFFFFE2D2) else AzulSuave)
                                .clickable { vm.onAvatar(i) }.padding(vertical = 14.dp, horizontal = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(Modifier.size(60.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                                Text(a.emoji, fontSize = 30.sp)
                            }
                            Text(a.edad, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Navy)
                            Pildora(a.etiqueta, if (sel) Color(0xFFFFD6A5) else Color.White, if (sel) Marron else GrisTexto)
                        }
                    }
                }
            }

            // Nombre
            TarjetaSeccion("Nombre o apodo del niño", Icons.Filled.Person) {
                CampoSimple(s.nombre, vm::onNombre, "Ej: Mateo", Icons.Filled.Face)
            }

            // Edad
            TarjetaSeccion("Edad actual", Icons.Filled.DateRange, derecha = "● Etapa BLW activa", colorDerecha = Verde) {
                edades.chunked(2).forEach { fila ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        fila.forEach { (meses, texto) ->
                            val sel = s.edadMeses == meses
                            Row(
                                Modifier.weight(1f).height(48.dp).clip(RoundedCornerShape(24.dp))
                                    .background(if (sel) Ambar else AzulSuave)
                                    .clickable { vm.onEdad(meses) }.padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(texto, fontWeight = FontWeight.Bold, color = if (sel) Color.White else Navy)
                                if (sel) Icon(Icons.Filled.CheckCircle, null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
                Text(
                    if (s.edadMeses <= 12) "💡 Adaptaremos la textura: alimentos triturados, bastoncitos suaves o trozos seguros."
                    else "💡 Adaptaremos porciones y texturas a su etapa, siempre con trozos blandos y seguros.",
                    fontSize = 12.sp, color = GrisTexto
                )
            }

            // Preferencias
            TarjetaSeccion("Preferencias alimentarias", Icons.Filled.FavoriteBorder) {
                Text("Selecciona lo que más le emociona comer:", fontSize = 13.sp, color = GrisTexto)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    preferenciasOpc.forEach { (label, emoji) ->
                        ChipSeleccion("$emoji $label", label in s.preferencias, Marron) { vm.togglePreferencia(label) }
                    }
                }
            }

            // No le gustan
            TarjetaSeccion("Alimentos que no le gustan", Icons.Filled.ThumbDown) {
                CampoSimple(s.noLeGustan, vm::onNoLeGustan, "Ej: Zanahoria cocida, brócoli entero", Icons.Filled.Block)
                Text("PequeChef buscará maneras creativas o alternativas nutricionales ricas.", fontSize = 12.sp, color = GrisTexto)
            }

            // Alergias
            TarjetaSeccion("Alergias o restricciones médicas", Icons.Filled.Warning) {
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color(0xFFFFD6D6)).padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Filled.Warning, null, tint = Color(0xFFB91C1C), modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Advertencia de seguridad pediátrica: ") }
                        append("Estas recetas excluirán estrictamente cualquier ingrediente que marques aquí.")
                    }, fontSize = 13.sp, color = Color(0xFF991B1B))
                }
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    alergiasOpc.forEach { (label, emoji) ->
                        ChipSeleccion(
                            if (emoji.isEmpty()) label else "$emoji $label",
                            label in s.alergias, Naranja
                        ) { vm.toggleAlergia(label) }
                    }
                }
            }

            // Confidencialidad
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(VerdeClaro).padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Shield, null, tint = Color(0xFF065F46), modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("🔒 Datos confidenciales y supervisados por pautas pediátricas",
                    fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF065F46))
            }

            BotonNaranja(
                texto = when {
                    s.guardando -> "Guardando..."
                    esOnboarding -> "Crear perfil y generar menú"
                    else -> "Guardar cambios"
                },
                habilitado = s.valido && !s.guardando
            ) { vm.guardar(onGuardado) }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun CampoSimple(valor: String, onCambio: (String) -> Unit, placeholder: String, icono: ImageVector) {
    TextField(
        value = valor, onValueChange = onCambio, singleLine = true,
        placeholder = { Text(placeholder, color = GrisTexto) },
        leadingIcon = { Icon(icono, null, tint = GrisTexto) },
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = AzulSuave, unfocusedContainerColor = AzulSuave,
            focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
        )
    )
}