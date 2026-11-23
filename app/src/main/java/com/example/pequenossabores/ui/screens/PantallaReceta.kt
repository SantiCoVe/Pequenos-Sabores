package com.example.pequenossabores.ui.screens

import com.example.pequenossabores.ui.components.EncabezadoDetalle
import com.example.pequenossabores.ui.components.EstadoReceta
import com.example.pequenossabores.ui.components.Pildora
import com.example.pequenossabores.ui.theme.Fondo
import com.example.pequenossabores.ui.theme.Marron
import com.example.pequenossabores.ui.theme.Naranja
import com.example.pequenossabores.ui.theme.Navy
import com.example.pequenossabores.ui.theme.VerdeClaro
import com.example.pequenossabores.ui.theme.Durazno
import com.example.pequenossabores.ui.theme.AzulSuave
import com.example.pequenossabores.ui.theme.GrisTexto
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pequenossabores.data.repository.RecetasData
import com.example.pequenossabores.ui.components.*
import com.example.pequenossabores.ui.theme.*
import com.example.pequenossabores.viewmodel.RecetaViewModel
import com.example.pequenossabores.ui.components.AvisoInfo
import com.example.pequenossabores.ui.components.FotoPlaceholder

private val nombresAlergeno = mapOf(
    "Sin gluten / Celiaquía" to "gluten",
    "Sin lactosa" to "lácteos",
    "Huevo" to "huevo",
    "Maní / Frutos secos" to "maní o frutos secos",
    "Pescado" to "pescado"
)

@Composable
fun PantallaReceta(
    recetaId: Int,
    onAtras: () -> Unit,
    onIniciar: (Int) -> Unit,
    vm: RecetaViewModel = viewModel()
) {
    LaunchedEffect(recetaId) { vm.cargar(recetaId) }
    val peque by vm.peque.collectAsStateWithLifecycle()
    val favoritos by vm.favoritos.collectAsStateWithLifecycle()
    val esFavorito = recetaId in favoritos
    val marcados = vm.marcados

    val r = vm.receta?.takeIf { it.id == recetaId }
    if (r == null) {
        EstadoReceta(error = vm.errorCarga, onAtras = onAtras)
        return
    }

    val nombre = peque?.nombre ?: "tu peque"
    val conflictos = r.alergenos.intersect(peque?.alergiasSet() ?: emptySet())

    Scaffold(
        containerColor = Fondo,
        topBar = { EncabezadoDetalle("Detalle Receta", onAtras) }
    ) { pad ->
        Column(
            Modifier.padding(pad).fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Volver + para quién
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("← Volver al plan", fontWeight = FontWeight.Bold, color = Marron, fontSize = 14.sp,
                    modifier = Modifier.clickable(onClick = onAtras))
                Pildora(
                    peque?.let { "✔ Para ${it.nombre} (${it.edadTexto})" } ?: "✔ Para tu peque",
                    Color(0xFFFFE2B8), Marron
                )
            }

            // Foto + título + datos rápidos
            Card(shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column {
                    Box(Modifier.fillMaxWidth().height(200.dp).background(Color(0xFFF1E3D3))) {
                        FotoPlaceholder(r.emoji, Modifier.fillMaxSize(), tamano = 90, url = r.imagenUrl)
                        Pildora(
                            if (r.datosCompletos) "👶 Apto BLW desde ${r.desdeMeses}m"
                            else "🌍 ${r.categoria.ifBlank { "Receta internacional" }}",
                            Color.White, Navy,
                            Modifier.align(Alignment.BottomStart).padding(12.dp)
                        )
                        Box(
                            Modifier.align(Alignment.BottomEnd).padding(12.dp).size(44.dp).clip(CircleShape)
                                .background(Color.White).clickable { vm.alternarFavorito(recetaId) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                if (esFavorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder, null,
                                tint = if (esFavorito) Color.Red else Naranja
                            )
                        }
                    }
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("${r.emoji} ${r.titulo}", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            if (r.datosCompletos) {
                                InfoTile("⏱", "${r.minutos} min", "Tiempo", Modifier.weight(1f))
                                InfoTile("🍽", "${r.porciones} porciones", "Rendimiento", Modifier.weight(1f))
                                InfoTile("😊", r.textura, "Textura suave", Modifier.weight(1f))
                            } else {
                                InfoTile("🛒", "${r.ingredientes.size}", "Ingredientes", Modifier.weight(1f))
                                InfoTile("🍽", r.categoria.ifBlank { "—" }, "Categoría", Modifier.weight(1f))
                                InfoTile("🌍", r.area.ifBlank { "—" }, "Cocina", Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            // Alerta de alergias del perfil
            if (conflictos.isNotEmpty()) {
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Color(0xFFFFD6D6)).padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Filled.Warning, null, tint = Color(0xFFB91C1C), modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Atención: ") }
                        append("esta receta contiene ${conflictos.joinToString(", ") { nombresAlergeno[it] ?: it }}, " +
                                "que coincide con las restricciones de $nombre. Consulta con su pediatra antes de prepararla.")
                    }, fontSize = 13.sp, color = Color(0xFF991B1B))
                }
            }

            // Aporte nutricional
            if (!r.datosCompletos) {
                AvisoInfo("ℹ Receta de TheMealDB (en inglés). No está adaptada para bebés ni incluye información nutricional: ajusta la textura, evita sal y azúcar y consulta con el pediatra.")
            }

            // Aporte nutricional
            if (r.datosCompletos) {
                // Ingredientes
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("🧺 Ingredientes", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
                    Text("${r.ingredientes.size} elementos", fontSize = 12.sp, color = GrisTexto)
                }
                Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                        r.ingredientes.forEachIndexed { i, ing ->
                            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = i in marcados, onCheckedChange = { vm.alternarMarcado(i) },
                                    colors = CheckboxDefaults.colors(checkedColor = Naranja)
                                )
                                Text(ing.emoji, fontSize = 22.sp)
                                Spacer(Modifier.width(10.dp))
                                Column {
                                    Text(ing.texto, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Navy)
                                    Text(ing.nota, fontSize = 12.sp, color = GrisTexto)
                                }
                            }
                            if (i < r.ingredientes.lastIndex) HorizontalDivider(color = Color(0xFFEEF2FA))
                        }
                    }
                }
            }

            // Consejo
            Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(72.dp).clip(RoundedCornerShape(18.dp)).background(Color(0xFFFFE2B8)), contentAlignment = Alignment.Center) {
                        Text("🤖", fontSize = 30.sp)
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text("CONSEJO DE PEQUECHEF", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Marron)
                        Text(r.consejo, fontSize = 14.sp, color = Navy)
                    }
                }
            }

            // Acciones
            Button(
                onClick = { onIniciar(recetaId) },
                modifier = Modifier.fillMaxWidth().height(56.dp), shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Naranja)
            ) { Text("👨‍🍳 Iniciar preparación paso a paso", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White) }

            BotonSuave("🤖 Preguntar a PequeChef", fondo = Color(0xFFFFE2B8), colorTexto = Marron) { /* TODO asistente */ }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                BotonBlanco(if (esFavorito) "★ En favoritos" else "☆ Guardar favoritos", Modifier.weight(1f)) {
                    vm.alternarFavorito(recetaId)
                }
                BotonBlanco("⟳ Cambiar receta", Modifier.weight(1f), onAtras)
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun InfoTile(emoji: String, valor: String, etiqueta: String, modifier: Modifier) {
    Column(
        modifier.clip(RoundedCornerShape(20.dp)).background(AzulSuave).padding(vertical = 12.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 18.sp)
        Text(valor, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Navy, textAlign = TextAlign.Center)
        Text(etiqueta, fontSize = 11.sp, color = GrisTexto, textAlign = TextAlign.Center)
    }
}

@Composable
private fun TileNutri(label: String, valor: String, unidad: String, emoji: String, fondoIcono: Color, modifier: Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(label, fontSize = 12.sp, color = GrisTexto)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(valor, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
                    Text(" $unidad", fontSize = 12.sp, color = GrisTexto, modifier = Modifier.padding(bottom = 3.dp))
                }
            }
            Box(Modifier.size(36.dp).clip(CircleShape).background(fondoIcono), contentAlignment = Alignment.Center) { Text(emoji) }
        }
    }
}

@Composable
private fun BotonBlanco(texto: String, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier.height(48.dp).clip(CircleShape).background(Color.White).clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) { Text(texto, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Navy) }
}