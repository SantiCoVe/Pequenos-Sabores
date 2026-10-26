package com.example.pequenossabores.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Naranja = Color(0xFFF97316)
val NaranjaClaro = Color(0xFFFFEDD5)
val Marron = Color(0xFF92400E)
val Navy = Color(0xFF0F172A)
val AzulSuave = Color(0xFFE8EEFF)
val Verde = Color(0xFF10B981)
val VerdeClaro = Color(0xFFB7F7D8)
val Ambar = Color(0xFFF59E0B)
val Fondo = Color(0xFFF8F9FF)
val Durazno = Color(0xFFFBEAE0)
val GrisTexto = Color(0xFF64748B)

private val Colores = lightColorScheme(
    primary = Naranja,
    onPrimary = Color.White,
    background = Fondo,
    surface = Color.White,
    onSurface = Navy
)

@Composable
fun PequenosSaboresTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Colores, content = content)
}