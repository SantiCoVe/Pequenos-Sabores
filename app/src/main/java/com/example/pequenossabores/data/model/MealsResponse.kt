package com.example.pequenossabores.data.model

/** TheMealDB devuelve cada receta como un objeto plano con strIngredient1..20, así que la leemos como mapa. */
data class MealsResponse(
    val meals: List<Map<String, String?>>?
)