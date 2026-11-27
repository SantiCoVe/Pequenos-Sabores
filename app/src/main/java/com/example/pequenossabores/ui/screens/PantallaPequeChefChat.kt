
package com.example.pequenossabores.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pequenossabores.data.repository.MensajeChat
import com.example.pequenossabores.ui.components.BarraInferior
import com.example.pequenossabores.ui.components.EncabezadoDetalle
import com.example.pequenossabores.ui.components.Tab
import com.example.pequenossabores.viewmodel.ChatViewModel

private val ChatBackground = Color(0xFFF8F9FF)
private val ChatNavy = Color(0xFF0D1C2E)
private val ChatBrown = Color(0xFF584237)
private val ChatOrange = Color(0xFF9D4300)
private val ChatLightOrange = Color(0xFFFFDDB8)
private val ChatAccent = Color(0xFFFEA619)
private val ChatChipBlue = Color(0xFFE6EEFF)
private val ChatMuted = Color(0xFF7A8493)
private val ChatWhite = Color.White

@Composable
fun PantallaPequeChefChat(
    onVolver: () -> Unit = {},
    onInicio: () -> Unit = {},
    onPlan: () -> Unit = {},
    onPerfil: () -> Unit = {},
    chatViewModel: ChatViewModel = viewModel()
) {
    val mensajes by chatViewModel.mensajes.collectAsState()
    val cargando by chatViewModel.cargando.collectAsState()
    val error by chatViewModel.error.collectAsState()

    var textoEntrada by remember { mutableStateOf("") }
    val estadoLista = rememberLazyListState()

    LaunchedEffect(mensajes.size, cargando, error) {
        val totalElementos = 1 +
                (if (mensajes.isEmpty()) 1 else mensajes.size) +
                (if (cargando) 1 else 0) +
                (if (error != null) 1 else 0)

        estadoLista.animateScrollToItem(totalElementos - 1)
    }

    fun enviar() {
        val texto = textoEntrada.trim()

        if (texto.isNotEmpty() && !cargando) {
            chatViewModel.enviarMensaje(texto)
            textoEntrada = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ChatBackground)
            .imePadding()
    ) {
        // Cabecera reutilizable del proyecto.
        EncabezadoDetalle(
            titulo = "PequeChef IA",
            onAtras = onVolver
        )

        HorizontalDivider(color = Color(0xFFEEF0F5))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = estadoLista,
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 18.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Etiqueta superior.
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Surface(
                        color = Color(0xFFEDF1F8),
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text(
                            text = "CONSULTA DE ALIMENTACIÓN",
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 7.dp
                            ),
                            color = ChatMuted,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            // Mensaje inicial cuando todavía no hay conversación.
            if (mensajes.isEmpty()) {
                item {
                    ColumnaMensaje(
                        mensaje = MensajeChat(
                            texto = "¡Hola! Soy PequeChef. 🧑‍🍳 " +
                                    "Pregúntame sobre recetas, ingredientes " +
                                    "o la organización de las comidas de tu peque.",
                            esAsistente = true
                        )
                    )
                }
            }

            // Conversación real.
            items(mensajes) { mensaje ->
                ColumnaMensaje(mensaje = mensaje)
            }

            // Indicador de espera mientras responde Gemini.
            if (cargando) {
                item {
                    ColumnaMensaje(
                        mensaje = MensajeChat(
                            texto = "Estoy preparando tu respuesta… 🧡",
                            esAsistente = true
                        )
                    )
                }
            }

            // Error devuelto por el ViewModel.
            if (error != null) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = error.orEmpty(),
                            color = Color(0xFFB3261E),
                            fontSize = 12.sp
                        )

                        Text(
                            text = "Puedes intentarlo nuevamente.",
                            color = ChatOrange,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .clickable {
                                    chatViewModel.limpiarError()
                                }
                                .padding(top = 5.dp)
                        )
                    }
                }
            }
        }

        // Sugerencias rápidas: rellenan el campo, no se envían solas.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(ChatWhite)
                .padding(top = 10.dp, bottom = 8.dp)
        ) {
            Text(
                text = "SUGERENCIAS RÁPIDAS",
                modifier = Modifier.padding(horizontal = 16.dp),
                color = ChatBrown,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )

            Spacer(Modifier.height(9.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChipSugerencia(
                    texto = "🔄 Sustituir ingrediente",
                    modifier = Modifier.weight(1f)
                ) {
                    textoEntrada =
                        "¿Con qué ingrediente puedo sustituir otro alimento?"
                }

                ChipSugerencia(
                    texto = "✨ Otra receta rápida",
                    modifier = Modifier.weight(1f)
                ) {
                    textoEntrada = "Dame una receta rápida para mi peque."
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                ChipSugerencia(texto = "⏱️ Tiempo de cocción") {
                    textoEntrada =
                        "¿Cuánto tiempo de cocción necesita esta receta?"
                }
            }
        }

        // Campo de escritura y botón para enviar.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ChatWhite)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        ChatBackground,
                        RoundedCornerShape(24.dp)
                    )
                    .padding(
                        horizontal = 15.dp,
                        vertical = 13.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = textoEntrada,
                    onValueChange = { textoEntrada = it },
                    modifier = Modifier.weight(1f),
                    enabled = !cargando,
                    textStyle = TextStyle(
                        color = ChatNavy,
                        fontSize = 13.sp
                    ),
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = { enviar() }
                    ),
                    decorationBox = { campo ->
                        Box {
                            if (textoEntrada.isEmpty()) {
                                Text(
                                    text = "Pregunta sobre la comida de tu peque…",
                                    color = ChatMuted,
                                    fontSize = 12.sp
                                )
                            }
                            campo()
                        }
                    }
                )
            }

            Spacer(Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = if (cargando) ChatMuted else ChatAccent,
                        shape = CircleShape
                    )
                    .clickable(enabled = !cargando) {
                        enviar()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (cargando) "…" else "➤",
                    color = ChatWhite,
                    fontSize = 22.sp
                )
            }
        }

        Text(
            text = "Adapta las comidas a la edad y desarrollo de tu peque.",
            modifier = Modifier
                .fillMaxWidth()
                .background(ChatWhite)
                .padding(horizontal = 12.dp, vertical = 5.dp),
            color = ChatMuted,
            fontSize = 9.sp,
            textAlign = TextAlign.Center
        )

        // Barra de navegación reutilizable del proyecto.
        BarraInferior(
            seleccionada = Tab.PEQUECHEF,
            onTab = { tab ->
                when (tab) {
                    Tab.INICIO -> onInicio()
                    Tab.PLAN -> onPlan()
                    Tab.PEQUECHEF -> Unit
                    Tab.PERFIL -> onPerfil()
                }
            }
        )
    }
}

@Composable
private fun ColumnaMensaje(
    mensaje: MensajeChat
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (mensaje.esAsistente) {
            Alignment.Start
        } else {
            Alignment.End
        }
    ) {
        if (mensaje.esAsistente) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(27.dp)
                        .background(
                            Color(0xFFFFE1AD),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👨‍🍳", fontSize = 15.sp)
                }

                Spacer(Modifier.width(7.dp))

                Text(
                    text = "PequeChef IA",
                    color = ChatBrown,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(
                if (mensaje.esAsistente) 0.94f else 0.88f
            ),
            color = if (mensaje.esAsistente) {
                ChatLightOrange
            } else {
                ChatOrange
            },
            shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomStart = if (mensaje.esAsistente) 5.dp else 18.dp,
                bottomEnd = if (mensaje.esAsistente) 18.dp else 5.dp
            )
        ) {
            Text(
                text = mensaje.texto,
                modifier = Modifier.padding(14.dp),
                color = if (mensaje.esAsistente) {
                    Color(0xFF2A1700)
                } else {
                    ChatWhite
                },
                fontSize = 13.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun ChipSugerencia(
    texto: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        color = ChatChipBlue,
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = texto,
            modifier = Modifier.padding(
                horizontal = 11.dp,
                vertical = 10.dp
            ),
            color = ChatNavy,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}