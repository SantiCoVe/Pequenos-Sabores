package com.example.pequenossabores.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pequenossabores.R

@Composable
fun SplashScreen() {
    val fondo = Color(0xFFF7F5F0)
    val naranja = Color(0xFFD96832)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.logopequenossabores
                ),
                contentDescription = "Logo de Pequeños Sabores",
                modifier = Modifier.size(230.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Pequeños Sabores",
                color = naranja,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Pequeñas recetas, grandes sonrisas",
                color = Color(0xFF8D7665),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}