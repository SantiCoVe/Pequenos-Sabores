package com.example.pequenossabores.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pequenossabores.ui.components.*
import com.example.pequenossabores.ui.theme.*
import com.example.pequenossabores.R

@Composable
fun PantallaBienvenida(onComenzar: () -> Unit, onIniciarSesion: () -> Unit) {
    Column(
        Modifier.fillMaxSize().background(Color.White).verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(170.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(Fondo),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.logopequenossabores
                ),
                contentDescription = "Logo de Pequeños Sabores",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(Modifier.height(20.dp))
        Text(buildAnnotatedString {
            withStyle(SpanStyle(color = Navy)) { append("Pequeños ") }
            withStyle(SpanStyle(color = Naranja)) { append("Sabores") }
        }, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
        Text("Alimentar, aprender y crecer juntos", color = Marron, fontWeight = FontWeight.Bold, fontSize = 17.sp)
        Spacer(Modifier.height(10.dp))
        Text(
            "La guía inteligente impulsada por IA para la nutrición balanceada y divertida de tu pequeño en su primera infancia.",
            textAlign = TextAlign.Center, color = GrisTexto, fontSize = 15.sp
        )
        Spacer(Modifier.height(24.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            TarjetaBeneficio("🥗", "Menús semanales balanceados", "Adaptados según edad, etapa y preferencias", VerdeClaro)
            TarjetaBeneficio("🤖", "PequeChef IA en vivo", "Respuestas pediátricas y tips en segundos", NaranjaClaro, etiqueta = "NUEVO")
            TarjetaBeneficio("🥑", "Sustitución inteligente", "Cambia ingredientes ante alergias o antojos", Color(0xFFFFE4CC))
        }
        Spacer(Modifier.height(16.dp))
        Row(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(AzulSuave).padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🍊🥦🍌", fontSize = 16.sp)
            Spacer(Modifier.width(8.dp))
            Text("+25k familias cocinando feliz", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Navy, modifier = Modifier.weight(1f))
            Text("✔ Aval BLW", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Verde)
        }
        Spacer(Modifier.height(24.dp))
        BotonNaranja("Comenzar", onClick = onComenzar)
        Spacer(Modifier.height(12.dp))
        BotonSuave("¿Ya tienes cuenta? Iniciar sesión", colorTexto = Marron, onClick = onIniciarSesion)
        Spacer(Modifier.height(16.dp))
        Text("🔒 Entorno 100% seguro para tu familia", fontSize = 12.sp, color = GrisTexto)
        Text(
            "Al continuar, aceptas nuestras pautas nutricionales avaladas y políticas de privacidad familiar.",
            fontSize = 11.sp, color = Naranja.copy(alpha = .5f), textAlign = TextAlign.Center
        )
    }
}