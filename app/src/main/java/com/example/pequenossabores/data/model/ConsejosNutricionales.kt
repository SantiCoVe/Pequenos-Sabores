package com.example.pequenossabores.data.model

import java.time.LocalDate

data class ConsejoNutricional(
    val titulo: String,
    val descripcion: String
)

object ConsejosNutricionales {

    private val consejos = listOf(
        ConsejoNutricional(
            titulo = "Variedad en cada comida",
            descripcion = "Ofrece diferentes alimentos a lo largo de la semana. La variedad ayuda a incorporar distintos nutrientes."
        ),
        ConsejoNutricional(
            titulo = "Vitamina C y hierro",
            descripcion = "Combinar fuentes de hierro vegetal con alimentos ricos en vitamina C puede favorecer su absorción."
        ),
        ConsejoNutricional(
            titulo = "Respeta su apetito",
            descripcion = "Observa las señales de hambre y saciedad de tu peque, sin obligarlo a terminar el plato."
        ),
        ConsejoNutricional(
            titulo = "Explorar nuevos sabores",
            descripcion = "Presenta nuevos alimentos con paciencia. A veces se necesitan varios intentos para aceptarlos."
        ),
        ConsejoNutricional(
            titulo = "Agua durante las comidas",
            descripcion = "Ofrece agua de acuerdo con su edad y etapa de alimentación, especialmente cuando ya inició la alimentación complementaria."
        ),
        ConsejoNutricional(
            titulo = "Seguridad al comer",
            descripcion = "Adapta la textura y el tamaño de los alimentos a su desarrollo, y mantén siempre la supervisión durante las comidas."
        ),
        ConsejoNutricional(
            titulo = "Menos sal y azúcar añadidas",
            descripcion = "Prioriza alimentos variados y preparaciones sencillas, evitando añadir sal y azúcar innecesarias."
        )
    )

    fun obtenerDelDia(): ConsejoNutricional {
        val indice = (LocalDate.now().dayOfYear - 1) % consejos.size
        return consejos[indice]
    }
}
