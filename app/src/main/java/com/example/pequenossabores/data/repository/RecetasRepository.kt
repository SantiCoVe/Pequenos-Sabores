package com.example.pequenossabores.data.repository

import com.example.pequenossabores.data.model.Receta
import com.example.pequenossabores.data.remote.MealMapper
import com.example.pequenossabores.data.remote.RetrofitInstance
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

data class ResultadoPool(val recetas: List<Receta>, val desdeApi: Boolean)

/** Recetas de la API con respaldo en el catálogo local si falla la red. */
object RecetasRepository {

    private val cache = mutableMapOf<Int, Receta>()
    private var poolGuardado: ResultadoPool? = null

    private val terminos = listOf(
        "pancake", "soup", "chicken", "rice", "pasta", "lentil", "spinach", "apple", "fish", "egg"
    )

    suspend fun obtenerPool(): ResultadoPool {
        poolGuardado?.let { return it }
        return try {
            val recetas = coroutineScope {
                terminos.map { t -> async { RetrofitInstance.api.buscar(t).meals.orEmpty() } }.awaitAll()
            }.flatMap { it.take(2) }
                .mapNotNull { MealMapper.aReceta(it) }
                .distinctBy { it.id }

            if (recetas.size >= 8) {
                recetas.forEach { cache[it.id] = it }
                ResultadoPool(recetas, true).also { poolGuardado = it }
            } else ResultadoPool(RecetasData.recetas, false)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            ResultadoPool(RecetasData.recetas, false)
        }
    }

    private fun enCache(id: Int): Receta? =
        cache[id] ?: RecetasData.recetas.firstOrNull { it.id == id }

    suspend fun obtenerReceta(id: Int): Receta? {
        enCache(id)?.let { return it }
        return try {
            RetrofitInstance.api.detalle(id.toString()).meals?.firstOrNull()
                ?.let { MealMapper.aReceta(it) }
                ?.also { cache[id] = it }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            null
        }
    }
}