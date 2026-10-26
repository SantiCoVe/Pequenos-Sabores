package com.example.pequenossabores.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pequenossabores.data.repository.RecetasData
import com.example.pequenossabores.ui.components.*
import com.example.pequenossabores.ui.theme.*
import com.example.pequenossabores.viewmodel.PreparacionViewModel

@Composable
fun PantallaPreparacion(
    recetaId: Int,
    onAtras: () -> Unit,
    onFinalizar: () -> Unit,
    vm: PreparacionViewModel = viewModel()
) {
    val r = remember(recetaId) { RecetasData.porId(recetaId) }
    val total = r.pasos.size
    val i = vm.pasoActual
    val paso = r.pasos[i]
    val progreso = (i + 1) * 100 / total

    Scaffold(
        containerColor = Fondo,
        topBar = { EncabezadoDetalle("Detalle Receta", onAtras) }
    ) { pad ->
        Column(
            Modifier.padding(pad).fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Pildora("🍴 Modo Cocina Activo", Color(0xFFFFE2B8), Marron)
                Pildora("● En vivo", Color.White, Verde)
            }
            Column {
                Text("PREPARANDO", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = GrisTexto)
                Text("${r.titulo} ${r.emoji}", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
            }

            // Progreso
            Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                        Text(buildAnnotatedString {
                            withStyle(SpanStyle(color = Marron, fontWeight = FontWeight.Bold, fontSize = 16.sp)) { append("Paso ") }
                            withStyle(SpanStyle(color = Naranja, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)) { append("${i + 1}") }
                            withStyle(SpanStyle(color = GrisTexto, fontSize = 13.sp)) { append(" de $total") }
                        })
                        Text("$progreso% completado", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Marron)
                    }
                    LinearProgressIndicator(
                        progress = { progreso / 100f },
                        modifier = Modifier.fillMaxWidth().height(10.dp).clip(CircleShape),
                        color = Naranja, trackColor = Color(0xFFFFE2B8),
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(if (i > 0) "✓ Listo: ${r.pasos[i - 1].etiqueta}" else "", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Verde)
                        Text("● ${paso.etiqueta}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Marron)
                        Text(if (i < total - 1) "Siguiente: ${r.pasos[i + 1].etiqueta}" else "Último paso", fontSize = 12.sp, color = GrisTexto)
                    }
                }
            }

            // Paso anterior completado
            if (i > 0) {
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(AzulSuave).padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(40.dp).clip(CircleShape).background(Color(0xFF065F46)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.Check, null, tint = Color.White)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Paso $i · Completado", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Verde)
                        Text(r.pasos[i - 1].titulo, fontWeight = FontWeight.Bold, color = Navy,
                            textDecoration = TextDecoration.LineThrough, fontSize = 14.sp)
                    }
                    Icon(Icons.Filled.Visibility, null, tint = GrisTexto)
                }
            }

            // Paso en curso
            Card(shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Pildora("🍳 Paso en curso", Naranja, Color.White)
                        Pildora("⏱️ ~${paso.minutos} min", Durazno, Marron)
                    }
                    Text("Paso ${i + 1}: ${paso.titulo}", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Navy)

                    Box(Modifier.fillMaxWidth().height(180.dp)) {
                        FotoPlaceholder(r.emoji, Modifier.fillMaxSize(), tamano = 72)
                        if (paso.nota.isNotBlank()) {
                            Pildora("☝️ ${paso.nota}", Color.White.copy(alpha = .9f), Navy,
                                Modifier.align(Alignment.BottomStart).padding(10.dp))
                        }
                    }

                    Text(paso.descripcion, fontSize = 16.sp, color = Navy, lineHeight = 24.sp)

                    if (paso.tip.isNotBlank()) {
                        Row(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Color(0xFFFFF1E0)).padding(14.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(Modifier.size(36.dp).clip(CircleShape).background(Ambar), contentAlignment = Alignment.Center) { Text("💡") }
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Text("💡 Tip PequeChef", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = Marron)
                                Text(paso.tip, fontSize = 13.sp, color = GrisTexto)
                            }
                        }
                    }
                }
            }

            // Siguiente paso
            if (i < total - 1) {
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(AzulSuave).padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(40.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                        Text("${i + 2}", fontWeight = FontWeight.Bold, color = Marron)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Siguiente paso", fontSize = 11.sp, color = GrisTexto)
                        Text(r.pasos[i + 1].titulo, fontWeight = FontWeight.Bold, color = Navy, fontSize = 14.sp, maxLines = 1)
                    }
                    Icon(Icons.Filled.Lock, null, tint = GrisTexto)
                }
            }

            // Asistente manos libres
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(28.dp))
                    .background(Brush.horizontalGradient(listOf(Ambar, Marron)))
                    .clickable { /* TODO asistente por voz */ }.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.size(44.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) { Text("🤖") }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("ASISTENTE MANOS LIBRES", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Color.White.copy(alpha = .85f))
                    Text("Preguntar a PequeChef", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                }
                Text("🎤 Hablar", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White,
                    modifier = Modifier.clip(RoundedCornerShape(14.dp)).background(Color.White.copy(alpha = .25f)).padding(horizontal = 12.dp, vertical = 8.dp))
            }

            // Navegación entre pasos
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    Modifier.weight(1f).height(52.dp).clip(CircleShape).background(AzulSuave)
                        .clickable(enabled = i > 0) { vm.anterior() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("‹ Anterior", fontWeight = FontWeight.Bold, color = if (i > 0) Marron else GrisTexto.copy(alpha = .5f))
                }
                Box(
                    Modifier.weight(1f).height(52.dp).clip(CircleShape).background(Naranja)
                        .clickable { if (vm.siguiente(total)) onFinalizar() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (i < total - 1) "Siguiente paso →" else "Finalizar ✓", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}