package com.example.pequenossabores.data.remote

import com.example.pequenossabores.data.model.Ingrediente
import com.example.pequenossabores.data.model.Receta
import com.example.pequenossabores.data.model.*
import com.example.pequenossabores.data.model.Clasificador

/** Convierte el JSON de TheMealDB en nuestra clase Receta. */
object MealMapper {

    fun aReceta(m: Map<String, String?>): Receta? {
        val id = m["idMeal"]?.toIntOrNull() ?: return null
        val titulo = m["strMeal"]?.trim().orEmpty()
        if (titulo.isEmpty()) return null

        val categoria = m["strCategory"].orEmpty().trim()
        val area = m["strArea"].orEmpty().trim()

        val ingredientes = (1..20).mapNotNull { i ->
            val nombre = m["strIngredient$i"]?.trim().orEmpty()
            if (nombre.isEmpty()) return@mapNotNull null
            val cantidad = m["strMeasure$i"]?.trim().orEmpty()
            val cat = Clasificador.categoria(nombre)
            Ingrediente(
                emoji = Clasificador.emojiDe(cat),
                texto = "$cantidad $nombre".trim(),
                nota = cat,
                nombre = nombre,
                cantidad = cantidad,
                categoria = cat
            )
        }

        val etiquetas = (m["strTags"].orEmpty().split(",") + area)
            .map { it.trim() }.filter { it.isNotEmpty() }.distinct().take(3)

        return  Receta(
            id = id,
            titulo = titulo,
            emoji = emojiReceta(categoria),
            momento = categoria.ifBlank { "Receta" },
            resumen = listOf(categoria, area).filter { it.isNotBlank() }.joinToString(" · "),
            minutos = 0, kcal = 0, proteinas = 0.0, carbohidratos = 0, grasas = 0.0,
            aporteClave = "Receta del catálogo internacional TheMealDB. No incluye información nutricional.",
            consejo = "Antes de servirla a tu peque, ajusta la textura (trozos blandos y pequeños), " +
                    "reduce la sal y el azúcar y consulta con su pediatra.",
            ingredientes = ingredientes,
            pasos = pasos(m["strInstructions"].orEmpty()),
            etiquetas = etiquetas,
            alergenos = Clasificador.alergenos(ingredientes.map { it.nombre }),
            distintivo = categoria,
            categoria = categoria,
            area = area,
            imagenUrl = m["strMealThumb"],
            datosCompletos = false
        )
    }

    private fun pasos(texto: String): List<PasoReceta> {
        var partes = texto.split(Regex("\\r?\\n"))
            .map { it.trim().replace(Regex("^\\d+[.)]\\s*"), "") }
            .filter { it.isNotBlank() && !it.matches(Regex("(?i)^step\\s*\\d+$")) }

        if (partes.size < 3) {
            partes = texto.replace(Regex("\\s+"), " ").split(Regex("(?<=[.!?])\\s+")).filter { it.isNotBlank() }
        }
        if (partes.isEmpty()) {
            return listOf(PasoReceta("Preparación", "Sigue la preparación indicada en la receta.", 0, "Preparar"))
        }

        val tam = maxOf(1, Math.ceil(partes.size / 6.0).toInt())   // máximo 6 pasos
        return partes.chunked(tam).mapIndexed { i, grupo ->
            val desc = grupo.joinToString(" ")
            val corto = desc.substringBefore(".").take(45).trim()
            PasoReceta(
                titulo = if (desc.length > corto.length) "$corto…" else corto,
                descripcion = desc,
                minutos = 0,
                etiqueta = "Paso ${i + 1}"
            )
        }
    }

    private fun emojiReceta(c: String) = when (c.lowercase()) {
        "dessert" -> "🍰"
        "breakfast" -> "🥞"
        "chicken" -> "🍗"
        "beef", "lamb", "pork", "goat" -> "🥩"
        "seafood" -> "🐟"
        "pasta" -> "🍝"
        "vegetarian", "vegan" -> "🥗"
        "side", "starter" -> "🥣"
        else -> "🍽"
    }
}