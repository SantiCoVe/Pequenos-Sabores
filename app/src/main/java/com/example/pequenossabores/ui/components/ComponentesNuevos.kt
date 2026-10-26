package com.example.pequenossabores.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pequenossabores.ui.theme.*

enum class Tab { INICIO, PLAN, PEQUECHEF, PERFIL }

/** Encabezado de las pantallas con pestañas. La foto abre el diálogo de cerrar sesión. */
@Composable
fun EncabezadoApp(subtitulo: String, onCerrarSesion: () -> Unit) {
    var mostrarDialogo by remember { mutableStateOf(false) }

    Row(
        Modifier.fillMaxWidth().background(Color.White).statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(NaranjaClaro), contentAlignment = Alignment.Center) { Text("👨‍🍳") }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text("Pequeños Sabores", color = Marron, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            Text(subtitulo, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Navy)
        }
        Icon(Icons.Filled.Notifications, null, tint = Navy)
        Spacer(Modifier.width(12.dp))
        Box(
            Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFD9A38A)).clickable { mostrarDialogo = true },
            contentAlignment = Alignment.Center
        ) { Text("👩") }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Quieres salir de tu cuenta familiar?") },
            confirmButton = {
                Text("Cerrar sesión", color = Color.Red, fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { mostrarDialogo = false; onCerrarSesion() }.padding(8.dp))
            },
            dismissButton = {
                Text("Cancelar", modifier = Modifier.clickable { mostrarDialogo = false }.padding(8.dp))
            }
        )
    }
}

/** Encabezado de Detalle de receta y Preparación (flecha atrás + logo + título). */
@Composable
fun EncabezadoDetalle(titulo: String, onAtras: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().background(Color.White).statusBarsPadding().padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onAtras) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Navy) }
        Box(Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(NaranjaClaro), contentAlignment = Alignment.Center) { Text("👨‍🍳") }
        Spacer(Modifier.width(8.dp))
        Text(titulo, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Navy, modifier = Modifier.weight(1f))
        Box(Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFD9A38A)), contentAlignment = Alignment.Center) { Text("👩") }
        Spacer(Modifier.width(8.dp))
    }
}

@Composable
fun BarraInferior(seleccionada: Tab, onTab: (Tab) -> Unit) {
    Box(Modifier.fillMaxWidth().background(Color.White).navigationBarsPadding()) {
        Row(
            Modifier.fillMaxWidth().padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.Bottom
        ) {
            ItemBarra("Inicio", Icons.Filled.Home, seleccionada == Tab.INICIO) { onTab(Tab.INICIO) }
            ItemBarra("Plan", Icons.Filled.DateRange, seleccionada == Tab.PLAN) { onTab(Tab.PLAN) }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.offset(y = (-18).dp).clickable { onTab(Tab.PEQUECHEF) }
            ) {
                Box(Modifier.size(64.dp).shadow(8.dp, CircleShape).clip(CircleShape).background(Naranja), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Face, null, tint = Color.White, modifier = Modifier.size(30.dp))
                }
                Text("PequeChef", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Marron)
            }
            ItemBarra("Perfil", Icons.Filled.Person, seleccionada == Tab.PERFIL) { onTab(Tab.PERFIL) }
        }
    }
}

@Composable
private fun ItemBarra(texto: String, icono: ImageVector, activo: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable(onClick = onClick).padding(4.dp)) {
        Icon(icono, null, tint = if (activo) Marron else GrisTexto)
        Text(texto, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (activo) Marron else GrisTexto)
    }
}

/** Tarjeta blanca con título, ícono y texto opcional a la derecha. */
@Composable
fun TarjetaSeccion(
    titulo: String, icono: ImageVector,
    derecha: String? = null, colorDerecha: Color = Marron,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(icono, null, tint = Marron, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(8.dp))
                Text(titulo, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp, color = Navy, modifier = Modifier.weight(1f))
                derecha?.let {
                    Text(it, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = colorDerecha,
                        textAlign = TextAlign.End, modifier = Modifier.widthIn(max = 120.dp))
                }
            }
            content()
        }
    }
}

@Composable
fun ChipSeleccion(texto: String, seleccionado: Boolean, colorSel: Color = Marron, onClick: () -> Unit) {
    Text(
        texto, fontWeight = FontWeight.Bold, fontSize = 13.sp,
        color = if (seleccionado) Color.White else Navy,
        modifier = Modifier.clip(RoundedCornerShape(20.dp))
            .background(if (seleccionado) colorSel else AzulSuave)
            .clickable(onClick = onClick).padding(horizontal = 14.dp, vertical = 10.dp)
    )
}

@Composable
fun Pildora(texto: String, fondo: Color, colorTexto: Color, modifier: Modifier = Modifier) {
    Text(
        texto, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = colorTexto,
        modifier = modifier.clip(RoundedCornerShape(12.dp)).background(fondo).padding(horizontal = 10.dp, vertical = 5.dp)
    )
}

/** Reemplaza por Image(painterResource(R.drawable.tu_foto), ...) cuando tengas las imágenes. */
@Composable
fun FotoPlaceholder(emoji: String, modifier: Modifier = Modifier, tamano: Int = 32) {
    Box(modifier.clip(RoundedCornerShape(16.dp)).background(Color(0xFFF1E3D3)), contentAlignment = Alignment.Center) {
        Text(emoji, fontSize = tamano.sp)
    }
}