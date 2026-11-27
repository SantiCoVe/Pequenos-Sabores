package com.example.pequenossabores.data.model

data class Ingrediente(
    val emoji: String,
    val texto: String,
    val nota: String,
    val nombre: String = "",      // solo recetas de la API
    val cantidad: String = "",
    val categoria: String = ""
)

data class PasoReceta(
    val titulo: String,
    val descripcion: String,
    val minutos: Int,             // 0 = no se conoce
    val etiqueta: String,
    val nota: String = "",
    val tip: String = ""
)

data class Receta(
    val id: Int,
    val titulo: String,
    val emoji: String,
    val momento: String,
    val resumen: String,
    val minutos: Int,
    val kcal: Int,
    val proteinas: Double,
    val carbohidratos: Int,
    val grasas: Double,
    val aporteClave: String,
    val consejo: String,
    val ingredientes: List<Ingrediente>,
    val pasos: List<PasoReceta>,
    val etiquetas: List<String> = emptyList(),
    val alergenos: Set<String> = emptySet(),
    val distintivo: String = "",
    val porciones: Int = 2,
    val textura: String = "BLW Fácil",
    val desdeMeses: Int = 6,

    val categoria: String = "",
    val area: String = "",
    val imagenUrl: String? = null,
    val datosCompletos: Boolean = true   // false = la API no trae tiempo ni nutrición
)

data class DiaPlan(val abrev: String, val nombre: String, val numero: Int, val recetas: List<Receta>)

val Receta.datosRapidos: String
    get() = if (datosCompletos) "⏱ $minutos min   🔥 $kcal kcal"
    else listOfNotNull(
        categoria.takeIf { it.isNotBlank() }?.let { "🍽 $it" },
        area.takeIf { it.isNotBlank() }?.let { "🌍 $it" }
    ).joinToString("   ")