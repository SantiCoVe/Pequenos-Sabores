package com.example.pequenossabores.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pequenossabores.ui.theme.*

@Composable
fun CargandoPantalla(texto: String, modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = Naranja)
        Spacer(Modifier.height(12.dp))
        Text(texto, color = GrisTexto, fontSize = 14.sp)
    }
}

@Composable
fun AvisoInfo(texto: String, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color(0xFFFFF1D6)).padding(14.dp)
    ) {
        Text(texto, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Marron)
    }
}

/** Se muestra mientras carga la receta, o con error si no se pudo traer. */
@Composable
fun EstadoReceta(error: Boolean, onAtras: () -> Unit) {
    Scaffold(containerColor = Fondo, topBar = { EncabezadoDetalle("Detalle Receta", onAtras) }) { pad ->
        if (error) {
            Column(
                Modifier.padding(pad).fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("No se pudo cargar la receta. Revisa tu conexión a internet.",
                    color = Navy, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                BotonSuave("Volver", onClick = onAtras)
            }
        } else {
            CargandoPantalla("Cargando receta…", Modifier.padding(pad))
        }
    }
}