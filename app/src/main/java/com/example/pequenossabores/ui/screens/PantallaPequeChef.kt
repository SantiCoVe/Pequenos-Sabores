
package com.example.pequenossabores.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pequenossabores.ui.components.BarraInferior
import com.example.pequenossabores.ui.components.EncabezadoDetalle
import com.example.pequenossabores.ui.components.Tab

private val FondoPequeChef = androidx.compose.ui.graphics.Color(0xFFF8F9FF)
private val AzulPequeChef = androidx.compose.ui.graphics.Color(0xFF0D1C2E)
private val VerdePequeChef = androidx.compose.ui.graphics.Color(0xFF00B07A)
private val NaranjaPequeChef = androidx.compose.ui.graphics.Color(0xFFF97316)
private val TextoSecundario = androidx.compose.ui.graphics.Color(0xFF584237)

@Composable
fun PantallaPequeChef(
    onTab: (Tab) -> Unit,
    onAbrirChat: () -> Unit = {},
    onGenerarReceta: (String) -> Unit = {},
    onAbrirMenuSemanal: () -> Unit = {}
) {
    var ingredientes by rememberSaveable {
        mutableStateOf("")
    }

    Scaffold(
        bottomBar = {
            BarraInferior(Tab.PEQUECHEF, onTab)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // Presentación de PequeChef
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(112.dp)
                        .background(
                            color = androidx.compose.ui.graphics.Color(0xFFFFE0BD),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🤖", fontSize = 58.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "PequeChef",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulPequeChef
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "✨", fontSize = 22.sp)
                }

                Surface(
                    color = androidx.compose.ui.graphics.Color(0xFFE0F7EC),
                    shape = CircleShape
                ) {
                    Text(
                        text = "● IA de nutrición activa",
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        ),
                        color = androidx.compose.ui.graphics.Color(0xFF006C49),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Tu asistente inteligente de alimentación infantil. " +
                            "Personaliza recetas teniendo en cuenta las necesidades de tu peque.",
                    color = TextoSecundario,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    textAlign = TextAlign.Center
                )
            }

            // Información del perfil infantil
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFFFFEEDC)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🛡️ Información en cuenta",
                            color = androidx.compose.ui.graphics.Color(0xFF684000),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Surface(
                            color = androidx.compose.ui.graphics.Color.White,
                            shape = CircleShape
                        ) {
                            Text(
                                text = "Perfil infantil",
                                modifier = Modifier.padding(
                                    horizontal = 9.dp,
                                    vertical = 5.dp
                                ),
                                fontSize = 10.sp,
                                color = androidx.compose.ui.graphics.Color(0xFF855300)
                            )
                        }
                    }

                    InformacionPerfil(
                        icono = "👶",
                        titulo = "Etapa de desarrollo",
                        descripcion = "Se mostrará según el perfil registrado"
                    )

                    InformacionPerfil(
                        icono = "❤️",
                        titulo = "Preferencias alimentarias",
                        descripcion = "Alimentos favoritos y texturas preferidas"
                    )

                    InformacionPerfil(
                        icono = "🚫",
                        titulo = "Alimentos que se deben evitar",
                        descripcion = "Preferencias y alimentos rechazados"
                    )

                    Surface(
                        color = androidx.compose.ui.graphics.Color(0xFFFFDAD6),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(text = "⚠️", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))

                            Column {
                                Text(
                                    text = "Alergias y restricciones",
                                    fontWeight = FontWeight.Bold,
                                    color = androidx.compose.ui.graphics.Color(0xFF93000A),
                                    fontSize = 13.sp
                                )

                                Text(
                                    text = "Se consultarán desde el perfil para tenerlas " +
                                            "en cuenta al preparar recomendaciones.",
                                    color = androidx.compose.ui.graphics.Color(0xFF93000A),
                                    fontSize = 12.sp,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }

                    HorizontalDivider(
                        color = androidx.compose.ui.graphics.Color(0xFFFFD7B0)
                    )

                    Text(
                        text = "La personalización se basará en la información registrada. " +
                                "Las alergias deben validarse antes de recomendar una receta.",
                        color = androidx.compose.ui.graphics.Color(0xFF684000),
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }

            // Generador de recetas
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.ui.graphics.Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "🥕 Crear una receta",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulPequeChef
                    )

                    Text(
                        text = "¿Qué ingredientes tienes en casa? Cuéntaselos a " +
                                "PequeChef para comenzar a preparar una idea.",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = TextoSecundario
                    )

                    OutlinedTextField(
                        value = ingredientes,
                        onValueChange = { ingredientes = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Ingredientes disponibles") },
                        placeholder = {
                            Text("Ej. avena, banano, huevo...")
                        },
                        minLines = 2,
                        maxLines = 4,
                        shape = RoundedCornerShape(16.dp)
                    )

                    Button(
                        onClick = {
                            onGenerarReceta(ingredientes.trim())
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled = ingredientes.isNotBlank(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NaranjaPequeChef
                        )
                    ) {
                        Text(
                            text = "✨ Generar receta",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    TextButton(
                        onClick = onAbrirChat,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "💬 Hablar con PequeChef",
                            color = VerdePequeChef,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Menú semanal
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFFE5F5ED)
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "📅", fontSize = 30.sp)

                    Text(
                        text = "Planifica toda la semana",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulPequeChef
                    )

                    Text(
                        text = "Organiza ideas de comidas para tu peque con " +
                                "un menú que tenga en cuenta su perfil.",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = TextoSecundario
                    )

                    Button(
                        onClick = onAbrirMenuSemanal,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VerdePequeChef
                        )
                    ) {
                        Text(
                            text = "Crear menú semanal",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InformacionPerfil(
    icono: String,
    titulo: String,
    descripcion: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = androidx.compose.ui.graphics.Color(0xCCFFFFFF),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(text = icono, fontSize = 21.sp)
            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = titulo,
                    color = AzulPequeChef,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = descripcion,
                    color = TextoSecundario,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}
