package com.example.pequenossabores.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pequenossabores.data.model.RolFamiliar
import com.example.pequenossabores.ui.theme.*

@Composable
fun BotonNaranja(texto: String, modifier: Modifier = Modifier, habilitado: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick, enabled = habilitado,
        modifier = modifier.fillMaxWidth().height(56.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = Naranja, disabledContainerColor = Naranja.copy(alpha = .5f))
    ) {
        Text(texto, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
        Spacer(Modifier.width(8.dp))
        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White, modifier = Modifier.size(18.dp))
    }
}

@Composable
fun BotonSuave(texto: String, fondo: Color = AzulSuave, colorTexto: Color = Navy, onClick: () -> Unit) {
    Box(
        Modifier.fillMaxWidth().height(52.dp).clip(RoundedCornerShape(26.dp))
            .background(fondo).clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) { Text(texto, fontWeight = FontWeight.Bold, color = colorTexto) }
}

@Composable
fun TarjetaBeneficio(emoji: String, titulo: String, subtitulo: String, fondoIcono: Color, etiqueta: String? = null) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(48.dp).clip(CircleShape).background(fondoIcono), contentAlignment = Alignment.Center) {
                Text(emoji, fontSize = 22.sp)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(titulo, fontWeight = FontWeight.Bold, color = Navy)
                    etiqueta?.let {
                        Spacer(Modifier.width(8.dp))
                        Text(it, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Marron,
                            modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Color(0xFFFFD58A)).padding(horizontal = 8.dp, vertical = 2.dp))
                    }
                }
                Text(subtitulo, fontSize = 13.sp, color = GrisTexto)
            }
            Icon(Icons.Filled.CheckCircle, null, tint = Ambar.copy(alpha = .6f))
        }
    }
}

@Composable
fun CampoFormulario(
    etiqueta: String, valor: String, onCambio: (String) -> Unit, icono: ImageVector,
    esContrasena: Boolean = false, mostrar: Boolean = false, onToggleVer: () -> Unit = {},
    obligatorio: Boolean = false, tipo: KeyboardType = KeyboardType.Text
) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(etiqueta, fontWeight = FontWeight.Bold, color = Navy, fontSize = 14.sp)
            if (obligatorio) Text("Obligatorio", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Marron)
        }
        Spacer(Modifier.height(8.dp))
        TextField(
            value = valor, onValueChange = onCambio, singleLine = true,
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)),
            leadingIcon = { Icon(icono, null, tint = if (esContrasena) GrisTexto else Marron) },
            trailingIcon = if (esContrasena) {
                { IconButton(onClick = onToggleVer) {
                    Icon(if (mostrar) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, null, tint = GrisTexto)
                } }
            } else null,
            visualTransformation = if (esContrasena && !mostrar) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = if (esContrasena) KeyboardType.Password else tipo),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = AzulSuave, unfocusedContainerColor = AzulSuave,
                focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun ChipRol(rol: RolFamiliar, seleccionado: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier.height(40.dp).clip(RoundedCornerShape(20.dp))
            .background(if (seleccionado) Naranja else AzulSuave).clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text("${rol.emoji} ${rol.etiqueta}", fontWeight = FontWeight.Bold, fontSize = 13.sp,
            color = if (seleccionado) Color.White else Navy)
    }
}

@Composable
fun BarraSeguridad(nivel: Int, etiqueta: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(3) { i ->
                Box(Modifier.weight(1f).height(6.dp).clip(CircleShape)
                    .background(if (i < nivel) Verde else Color(0xFFD9E2F5)))
            }
        }
        Spacer(Modifier.width(12.dp))
        Text("✔ Seguridad: $etiqueta", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Verde)
    }
}

@Composable
fun TarjetaAcceso(titulo: String, subtitulo: String, icono: ImageVector, fondo: Color, tinte: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier, shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Box(Modifier.size(44.dp).clip(CircleShape).background(fondo), contentAlignment = Alignment.Center) {
                Icon(icono, null, tint = tinte)
            }
            Spacer(Modifier.height(12.dp))
            Text(titulo, fontWeight = FontWeight.Bold, color = Navy)
            Text(subtitulo, fontSize = 12.sp, color = GrisTexto)
        }
    }
}

@Composable
fun BarraProgresoGrupo(emoji: String, nombre: String, porcentaje: Int, color: Color) {
    Column(Modifier.padding(vertical = 6.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("$emoji $nombre", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Navy)
            Text("$porcentaje%", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = color)
        }
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { porcentaje / 100f },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
            color = color, trackColor = AzulSuave, strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
        )
    }
}