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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pequenossabores.ui.components.*
import com.example.pequenossabores.ui.theme.*
import com.example.pequenossabores.viewmodel.InicioViewModel

@Composable
fun PantallaInicio(
    onTab: (Tab) -> Unit,
    onVerReceta: (Int) -> Unit,
    onCerrarSesion: () -> Unit,
    vm: InicioViewModel = viewModel()
) {
    val s = vm.estado
    val usuario by vm.usuario.collectAsStateWithLifecycle()
    val peque by vm.peque.collectAsStateWithLifecycle()

    val primerNombre = usuario?.nombre?.substringBefore(" ") ?: "Familia"
    val nombreNino = peque?.nombre ?: "tu peque"

    Scaffold(
        containerColor = Fondo,
        topBar = { EncabezadoApp("Inicio", onCerrarSesion) },
        bottomBar = { BarraInferior(Tab.INICIO, onTab) }
    ) { pad ->
        Column(
            Modifier.padding(pad).fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Saludo (nombre del usuario y del peque salen de Room)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("¡Hola, $primerNombre! 👋", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "● Alimentación de $nombreNino" + (peque?.let { " (${it.edadTexto})" } ?: ""),
                        fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Marron,
                        modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(Color(0xFFFFE2B8)).padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                Box(Modifier.size(52.dp).clip(CircleShape).background(AzulSuave), contentAlignment = Alignment.Center) { Text("👶", fontSize = 26.sp) }
            }

            // PequeChef sugiere
            Box(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(28.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFFFFCB9A), Color(0xFFFFE2C0)))).padding(20.dp)
            ) {
                Row {
                    Box(Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)).background(Color.White), contentAlignment = Alignment.Center) { Text("🤖", fontSize = 24.sp) }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("PEQUECHEF SUGIERE", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Marron)
                            Spacer(Modifier.width(8.dp))
                            Text("IA Activa", fontSize = 10.sp, color = Naranja, fontWeight = FontWeight.Bold,
                                modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Color.White.copy(alpha = .6f)).padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                        Text("¿Qué preparamos hoy?", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
                        Text("He adaptado el menú para reforzar el hierro y la textura preferida de $nombreNino.", fontSize = 13.sp, color = Marron)
                    }
                }
            }

            // Menú recomendado
            Card(shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(16.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("✨ Menú recomendado hoy", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3B5BDB),
                            modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(AzulSuave).padding(horizontal = 12.dp, vertical = 6.dp))
                        Text("● 100% Natural", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF065F46),
                            modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(VerdeClaro).padding(horizontal = 12.dp, vertical = 6.dp))
                    }
                    Spacer(Modifier.height(12.dp))
                    FotoPlaceholder("🥞", Modifier.fillMaxWidth().height(180.dp), tamano = 80)
                    Spacer(Modifier.height(12.dp))
                    Text(s.menu.titulo, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
                    Spacer(Modifier.height(6.dp))
                    Text("⏱️ ${s.menu.tiempoMin} min   ☺️ ${s.menu.textura}   🔥 ${s.menu.kcal} kcal", fontSize = 12.sp, color = GrisTexto)
                    Spacer(Modifier.height(14.dp))
                    BotonNaranja("Ver receta completa") { onVerReceta(1) }
                }
            }

            // Accesos rápidos
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Accesos rápidos", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Navy)
                Text("4 herramientas", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GrisTexto)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TarjetaAcceso("Plan semanal", "Ver 7 días", Icons.Filled.DateRange, NaranjaClaro, Marron,
                    Modifier.weight(1f).clickable { onTab(Tab.PLAN) })
                TarjetaAcceso("PequeChef IA", "Consultar asistente", Icons.Filled.Face, NaranjaClaro, Marron,
                    Modifier.weight(1f).clickable { onTab(Tab.PEQUECHEF) })
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TarjetaAcceso("Lista de compras", "Ingredientes listos", Icons.Filled.ShoppingCart, Color(0xFFFFD58A), Marron, Modifier.weight(1f))
                TarjetaAcceso("Favoritos", "Platos preferidos", Icons.Filled.Star, VerdeClaro, Color(0xFF065F46), Modifier.weight(1f))
            }

            // Consejo
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(28.dp)).background(Durazno).padding(20.dp)) {
                Box(Modifier.size(44.dp).clip(CircleShape).background(Ambar), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Lightbulb, null, tint = Color.White)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("CONSEJO NUTRICIONAL", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Marron)
                    Text("Poder de absorción de hierro", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF4A2511))
                    Text("La combinación de vitamina C (como fresas o naranja) con hierro vegetal optimiza su absorción en niños pequeños.", fontSize = 13.sp, color = GrisTexto)
                }
            }

            // Progreso
            Card(shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(20.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("Progreso de Grupos Semanal", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
                            Text("Meta nutricional balanceada", fontSize = 12.sp, color = GrisTexto)
                        }
                        Text("✔️ ${s.total}% Total", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF065F46),
                            modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(VerdeClaro).padding(horizontal = 12.dp, vertical = 8.dp))
                    }
                    Spacer(Modifier.height(10.dp))
                    val colores = listOf(Naranja, Verde, Ambar)
                    s.progreso.forEachIndexed { i, g -> BarraProgresoGrupo(g.emoji, g.nombre, g.porcentaje, colores[i % colores.size]) }
                }
            }
        }
    }
}