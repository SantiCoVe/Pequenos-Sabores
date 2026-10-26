package com.example.pequenossabores.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pequenossabores.data.model.DiaPlan
import com.example.pequenossabores.ui.components.*
import com.example.pequenossabores.ui.theme.*
import com.example.pequenossabores.viewmodel.PlanViewModel

@Composable
fun PantallaPlan(
    onTab: (Tab) -> Unit,
    onVerReceta: (Int) -> Unit,
    onCerrarSesion: () -> Unit,
    vm: PlanViewModel = viewModel()
) {
    val peque by vm.peque.collectAsStateWithLifecycle()
    val semana = vm.semana
    val sel = vm.diaSeleccionado
    val nombre = peque?.nombre ?: "tu peque"

    Scaffold(
        containerColor = Fondo,
        topBar = { EncabezadoApp("Plan", onCerrarSesion) },
        bottomBar = { BarraInferior(Tab.PLAN, onTab) }
    ) { pad ->
        Column(
            Modifier.padding(pad).fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Encabezado del plan
            Card(shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Pildora("✨ Generado por PequeChef IA", Color(0xFFFFE2B8), Marron)
                        Spacer(Modifier.weight(1f))
                        Pildora("Semana 12", VerdeClaro, Color(0xFF065F46))
                    }
                    Text("Plan semanal para $nombre", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
                    Text(
                        peque?.let { "Adaptado a la fase de alimentación complementaria (${it.edadTexto})" }
                            ?: "Adaptado a la fase de alimentación complementaria",
                        fontSize = 13.sp, color = GrisTexto
                    )
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("📅 18 - 24 Mayo", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Marron, modifier = Modifier.weight(1f))
                        Row(
                            Modifier.clip(CircleShape).background(Color(0xFFFFE2D2)).clickable { vm.regenerar() }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Refresh, null, tint = Naranja, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Regenerar menú", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Naranja)
                        }
                    }
                }
            }

            // Selector de días
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                semana.forEachIndexed { i, d ->
                    val activo = i == sel
                    Column(
                        Modifier.width(54.dp).clip(RoundedCornerShape(18.dp))
                            .background(if (activo) Marron else Color.White)
                            .clickable { vm.seleccionarDia(i) }.padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(d.abrev, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                            color = if (activo) Color.White.copy(alpha = .8f) else GrisTexto)
                        Text("${d.numero}", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold,
                            color = if (activo) Color.White else Navy)
                        Box(Modifier.size(5.dp).clip(CircleShape).background(if (activo) Color.White else Color.Transparent))
                    }
                }
            }

            // Día seleccionado (expandido) y luego los demás días
            TarjetaDiaExpandida(semana[sel], esHoy = sel == 0, onVerReceta = onVerReceta)
            semana.forEachIndexed { i, dia ->
                if (i != sel) TarjetaDiaCompacta(dia, onVerReceta)
            }

            // Lista de compras
            val totalIngredientes = semana.sumOf { d -> d.recetas.sumOf { it.ingredientes.size } }
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(Color.White).padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.size(44.dp).clip(CircleShape).background(VerdeClaro), contentAlignment = Alignment.Center) { Text("✅") }
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("$totalIngredientes ingredientes listos", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Navy)
                    Text("Organizados por pasillo", fontSize = 12.sp, color = GrisTexto)
                }
                Button(
                    onClick = { /* TODO lista de compras */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Marron),
                    shape = RoundedCornerShape(20.dp)
                ) { Text("🛒 Generar lista", fontWeight = FontWeight.Bold, color = Color.White) }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TarjetaDiaExpandida(dia: DiaPlan, esHoy: Boolean, onVerReceta: (Int) -> Unit) {
    Card(shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Pildora(if (esHoy) "HOY · ${dia.nombre.uppercase()}" else dia.nombre.uppercase(), NaranjaClaro, Marron)
                Spacer(Modifier.width(8.dp))
                Text("${dia.numero} Mayo", fontSize = 13.sp, color = GrisTexto, modifier = Modifier.weight(1f))
                Text("✓ Balanceado", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Verde)
            }

            dia.recetas.forEach { r ->
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(AzulSuave)
                        .clickable { onVerReceta(r.id) }.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(r.momento.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Marron)
                        Text("${r.titulo} ${r.emoji}", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "⏱️ ${r.minutos} min   🔥 ${r.kcal} kcal" + if (r.distintivo.isNotBlank()) "   ${r.distintivo}" else "",
                            fontSize = 12.sp, color = GrisTexto
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    FotoPlaceholder(r.emoji, Modifier.size(72.dp))
                }
            }

            val etiquetas = dia.recetas.flatMap { it.etiquetas }.distinct()
            if (etiquetas.isNotEmpty()) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    etiquetas.forEach { Pildora(it, Color(0xFFFFE2B8), Marron) }
                }
            }
            Button(
                onClick = { onVerReceta(dia.recetas.first().id) },
                colors = ButtonDefaults.buttonColors(containerColor = Naranja),
                shape = CircleShape
            ) { Text("Ver receta →", fontWeight = FontWeight.Bold, color = Color.White) }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TarjetaDiaCompacta(dia: DiaPlan, onVerReceta: (Int) -> Unit) {
    val r = dia.recetas.first()
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth().clickable { onVerReceta(r.id) }
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Pildora(dia.nombre.uppercase(), AzulSuave, Marron)
                Spacer(Modifier.width(8.dp))
                Text("${dia.numero} Mayo", fontSize = 13.sp, color = GrisTexto, modifier = Modifier.weight(1f))
                Text(r.momento, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Navy)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("${r.titulo} ${r.emoji}", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Navy, maxLines = 2)
                    Text(r.resumen, fontSize = 12.sp, color = GrisTexto, maxLines = 2)
                    Spacer(Modifier.height(4.dp))
                    Text("⏱️ ${r.minutos} min   🔥 ${r.kcal} kcal", fontSize = 12.sp, color = GrisTexto)
                }
                Spacer(Modifier.width(10.dp))
                FotoPlaceholder(r.emoji, Modifier.size(72.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                FlowRow(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    r.etiquetas.forEach { Pildora(it, AzulSuave, Marron) }
                }
                Text("Ver receta ›", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Marron)
            }
        }
    }
}