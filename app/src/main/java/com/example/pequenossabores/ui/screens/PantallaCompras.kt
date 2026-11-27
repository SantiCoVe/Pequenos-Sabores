package com.example.pequenossabores.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pequenossabores.data.model.Clasificador
import com.example.pequenossabores.ui.components.*
import com.example.pequenossabores.ui.theme.*
import com.example.pequenossabores.viewmodel.ComprasViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

private val filtros = listOf(
    "Todos" to "Todos",
    "Verduras" to Clasificador.FRUTAS,
    "Lácteos" to Clasificador.LACTEOS,
    "Cereales" to Clasificador.CEREALES,
    "Proteínas" to Clasificador.PROTEINAS,
    "Otros" to Clasificador.OTROS
)

@Composable
fun PantallaCompras(
    onTab: (Tab) -> Unit,
    onCerrarSesion: () -> Unit,
    vm: ComprasViewModel = viewModel()
) {
    val peque by vm.peque.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val nombreNino = peque?.nombre ?: "tu peque"

    var dialogoAgregar by remember { mutableStateOf(false) }
    var dialogoVaciar by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Fondo,
        topBar = { EncabezadoApp("Inicio", onCerrarSesion) },
        bottomBar = { BarraInferior(Tab.INICIO, onTab) }
    ) { pad ->
        Column(
            Modifier.padding(pad).fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Fila superior
            Row(verticalAlignment = Alignment.CenterVertically) {
                Pildora("Semana 12 · Plan semanal", Color(0xFFFFE2B8), Marron)
                Spacer(Modifier.width(8.dp))
                Text(
                    if (vm.actualizado) "✔ Actualizado" else "● Sin sincronizar",
                    fontSize = 12.sp, fontWeight = FontWeight.Bold,
                    color = if (vm.actualizado) Verde else GrisTexto,
                    modifier = Modifier.weight(1f)
                )
                Row(
                    Modifier.clip(RoundedCornerShape(16.dp)).background(Color(0xFFD9E3FF))
                        .clickable { compartir(context, vm.textoCompartir()) }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Share, null, tint = Navy, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Compartir", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Navy)
                }
            }

            // Título
            Column {
                Text("Mi lista de compras", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
                Text(buildAnnotatedString {
                    append("Generada automáticamente para el menú de ")
                    withStyle(SpanStyle(color = Naranja, fontWeight = FontWeight.ExtraBold)) { append(nombreNino) }
                }, fontSize = 13.sp, color = GrisTexto)
            }

            // Mensajes de red / servidor
            vm.mensaje?.let { AvisoInfo("⚠ $it") }

            // Canasta
            Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("TU CANASTA HOY", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = GrisTexto)
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("${vm.comprados} de ${vm.total} comprados", fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold, color = Navy, modifier = Modifier.weight(1f))
                        Pildora("${vm.porcentaje}%", Color(0xFFFFE2B8), Marron)
                    }
                    LinearProgressIndicator(
                        progress = { vm.porcentaje / 100f },
                        modifier = Modifier.fillMaxWidth().height(10.dp).clip(CircleShape),
                        color = Naranja, trackColor = AzulSuave,
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("✔ Ingredientes del plan semanal", fontSize = 12.sp, color = GrisTexto)
                        Text("${vm.porComprar} por comprar", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Naranja)
                    }
                }
            }

            // Agregar + sincronizar
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { dialogoAgregar = true },
                    modifier = Modifier.weight(1f).height(52.dp), shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Naranja)
                ) {
                    Icon(Icons.Filled.Add, null, tint = Color.White)
                    Spacer(Modifier.width(6.dp))
                    Text("Agregar ingrediente", fontWeight = FontWeight.Bold, color = Color.White)
                }
                Box(
                    Modifier.size(52.dp).clip(CircleShape).background(Color.White).clickable { vm.cargar() },
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.Refresh, "Sincronizar", tint = Navy) }
            }

            // Filtros
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                filtros.forEach { (etiqueta, categoria) ->
                    ChipSeleccion(
                        if (categoria == "Todos") "Todos (${vm.total})" else etiqueta,
                        vm.filtro == categoria
                    ) { vm.filtrar(categoria) }
                }
            }

            // Lista por categoría
            when {
                vm.cargando -> Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Naranja)
                }
                vm.items.isEmpty() -> Text(
                    "Tu lista está vacía. Ve al Plan semanal y toca \"Generar lista\", o agrega un ingrediente.",
                    fontSize = 14.sp, color = GrisTexto
                )
                else -> vm.grupos.forEach { (categoria, lista) ->
                    Row(Modifier.fillMaxWidth().padding(top = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("${Clasificador.emojiDe(categoria)} $categoria", fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold, color = Navy, modifier = Modifier.weight(1f))
                        Pildora("${lista.count { it.comprado == 1 }}/${lista.size} listos", AzulSuave, GrisTexto)
                    }
                    lista.forEach { item ->
                        val marcado = item.comprado == 1
                        Row(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Color.White)
                                .clickable { vm.alternar(item) }.padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                Modifier.size(26.dp).clip(RoundedCornerShape(7.dp))
                                    .background(if (marcado) Naranja else AzulSuave),
                                contentAlignment = Alignment.Center
                            ) {
                                if (marcado) Icon(Icons.Filled.Check, null, tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(
                                    item.nombre, fontWeight = FontWeight.Bold, fontSize = 15.sp,
                                    color = if (marcado) GrisTexto else Navy,
                                    textDecoration = if (marcado) TextDecoration.LineThrough else null
                                )
                                Text(item.cantidad.ifBlank { "Según receta" }, fontSize = 12.sp, color = GrisTexto)
                            }
                            Text(Clasificador.emojiDe(item.categoria), fontSize = 20.sp)
                        }
                    }
                }
            }

            // Consejo
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(Durazno).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.size(44.dp).clip(CircleShape).background(Ambar), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Lightbulb, null, tint = Color.White)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Consejo de PequeChef", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Navy)
                    Text("Recuerda elegir frutas maduras y suaves para facilitar el agarre en palito de $nombreNino.",
                        fontSize = 13.sp, color = GrisTexto)
                }
            }

            BotonSuave("🛍 Finalizar compra o vaciar marcados", fondo = Color(0xFFD9E3FF)) { dialogoVaciar = true }
            Spacer(Modifier.height(8.dp))
        }
    }

    // Diálogo: agregar ingrediente
    if (dialogoAgregar) {
        var nombre by remember { mutableStateOf("") }
        var cantidad by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { dialogoAgregar = false },
            title = { Text("Agregar ingrediente") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(nombre, { nombre = it }, label = { Text("Ingrediente") }, singleLine = true)
                    OutlinedTextField(cantidad, { cantidad = it }, label = { Text("Cantidad (opcional)") }, singleLine = true)
                }
            },
            confirmButton = {
                Text("Agregar", color = Naranja, fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { vm.agregar(nombre, cantidad); dialogoAgregar = false }.padding(8.dp))
            },
            dismissButton = {
                Text("Cancelar", modifier = Modifier.clickable { dialogoAgregar = false }.padding(8.dp))
            }
        )
    }

    // Diálogo: vaciar marcados
    if (dialogoVaciar) {
        AlertDialog(
            onDismissRequest = { dialogoVaciar = false },
            title = { Text("Finalizar compra") },
            text = { Text("Se eliminarán de la lista los ${vm.comprados} ingredientes marcados como comprados.") },
            confirmButton = {
                Text("Vaciar marcados", color = Color.Red, fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { vm.vaciarMarcados(); dialogoVaciar = false }.padding(8.dp))
            },
            dismissButton = {
                Text("Cancelar", modifier = Modifier.clickable { dialogoVaciar = false }.padding(8.dp))
            }
        )
    }
}

private fun compartir(context: Context, texto: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, texto)
    }
    context.startActivity(Intent.createChooser(intent, "Compartir lista"))
}