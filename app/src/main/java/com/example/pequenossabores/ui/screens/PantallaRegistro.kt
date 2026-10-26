package com.example.pequenossabores.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pequenossabores.R
import com.example.pequenossabores.data.model.RolFamiliar
import com.example.pequenossabores.ui.components.*
import com.example.pequenossabores.ui.theme.*
import com.example.pequenossabores.viewmodel.AuthViewModel

@Composable
fun PantallaRegistro(
    iniciarEnLogin: Boolean,
    onAtras: () -> Unit,
    onExito: (Boolean) -> Unit,
    vm: AuthViewModel = viewModel()
) {
    val s by vm.estado.collectAsStateWithLifecycle()

    LaunchedEffect(iniciarEnLogin) { vm.setModoLogin(iniciarEnLogin) }

    Column(Modifier.fillMaxSize().background(Fondo).verticalScroll(rememberScrollState())) {
        // Barra superior
        Row(Modifier.fillMaxWidth().background(Color.White).statusBarsPadding().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onAtras) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Navy) }
            Spacer(Modifier.weight(1f))
            Text("Pequeños Sabores", color = Naranja, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, modifier = Modifier.padding(end = 15.dp),)
            Image(
                painter = painterResource(
                    id = R.drawable.logopequenossabores
                ),
                contentDescription = "Logo de Pequeños Sabores",
                modifier = Modifier
                    .width(30.dp)
                    .clip(RoundedCornerShape(5.dp)),
                contentScale = ContentScale.Fit
            )
        }

        Column(Modifier.padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(12.dp))

            if (!s.modoLogin) {
                Text("‹ Paso 1 de 3 · Cuenta de la Familia", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Marron,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(Color(0xFFFFD6A5)).padding(horizontal = 12.dp, vertical = 4.dp))
                Spacer(Modifier.height(12.dp))
            }

            Text(if (s.modoLogin) "Bienvenido de nuevo" else "Crea tu cuenta familiar",
                fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
            Text(
                if (s.modoLogin) "Ingresa para ver el menú de tu peque y hablar con PequeChef IA."
                else "Te acompañaremos a nutrir el crecimiento de tu peque con el apoyo de PequeChef IA.",
                textAlign = TextAlign.Center, color = GrisTexto, fontSize = 14.sp
            )
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("🥑", "🥕", "🥦", "🍓").forEach {
                    Box(Modifier.size(36.dp).clip(RoundedCornerShape(18.dp)).background(NaranjaClaro), contentAlignment = Alignment.Center) { Text(it) }
                }
            }
            Spacer(Modifier.height(16.dp))

            // Pestañas Crear cuenta / Iniciar sesión
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(AzulSuave).padding(4.dp)) {
                Pestana("Crear Cuenta", !s.modoLogin, Modifier.weight(1f)) { vm.setModoLogin(false) }
                Pestana("Iniciar Sesión", s.modoLogin, Modifier.weight(1f)) { vm.setModoLogin(true) }
            }
            Spacer(Modifier.height(16.dp))

            // Formulario
            Card(shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {

                    if (!s.modoLogin) {
                        CampoFormulario("Nombre del padre, madre o cuidador", s.nombre, vm::onNombre, Icons.Filled.Favorite, obligatorio = true)
                    }
                    CampoFormulario("Correo electrónico", s.correo, vm::onCorreo, Icons.Filled.Email, tipo = KeyboardType.Email)
                    CampoFormulario(if (s.modoLogin) "Contraseña" else "Contraseña segura", s.contrasena, vm::onContrasena, Icons.Filled.Lock,
                        esContrasena = true, mostrar = s.verContrasena, onToggleVer = vm::toggleVerContrasena)

                    if (!s.modoLogin) {
                        BarraSeguridad(s.nivelSeguridad, s.etiquetaSeguridad)

                        Text("Rol familiar (para adaptar tus menús)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Navy)
                        RolFamiliar.values().toList().chunked(2).forEach { fila ->
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                fila.forEach { rol ->
                                    ChipRol(rol, rol == s.rol, Modifier.weight(1f)) { vm.onRol(rol) }
                                }
                            }
                        }
                        Row(verticalAlignment = Alignment.Top) {
                            Checkbox(checked = s.aceptaTerminos, onCheckedChange = vm::onTerminos,
                                colors = CheckboxDefaults.colors(checkedColor = Naranja))
                            Text("Acepto los Términos de Servicio y la Política de Privacidad de Salud Infantil. Tus datos y los de tu peque están 100% protegidos.",
                                fontSize = 12.sp, color = GrisTexto, modifier = Modifier.padding(top = 12.dp))
                        }
                    }

                    s.error?.let {
                        Text(it, color = Color.Red, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    BotonNaranja(
                        texto = when {
                            s.cargando -> "Procesando..."
                            s.modoLogin -> "Iniciar sesión"
                            else -> "Crear cuenta y continuar"
                        },
                        habilitado = s.formularioValido && !s.cargando
                    ) { vm.enviar(onExito) }

                    Text("🛡 Algoritmo pediátrico certificado · Datos confidenciales", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Marron)
                    Text(if (s.modoLogin) "O INGRESA CON" else "O REGÍSTRATE CON", fontSize = 11.sp, color = GrisTexto, fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally))
                    BotonSuave("G  Google") { /* TODO Google Sign-In */ }
                    BotonSuave("  Apple", fondo = Navy, colorTexto = Color.White) { /* TODO Apple */ }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(
                if (s.modoLogin) "¿Aún no tienes cuenta? Crear cuenta" else "¿Ya tienes una cuenta registrada? Iniciar sesión",
                fontSize = 13.sp, color = Marron, fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { vm.setModoLogin(!s.modoLogin) }
            )
            Text("¿Olvidaste tu contraseña?", fontSize = 12.sp, color = Marron, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun Pestana(texto: String, activa: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier.clip(RoundedCornerShape(20.dp))
            .background(if (activa) Color.White else Color.Transparent)
            .clickable(onClick = onClick).padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(texto, fontWeight = FontWeight.Bold, color = if (activa) Marron else GrisTexto)
    }
}