package com.example.pequenossabores.data.repository

import com.example.pequenossabores.data.model.*

/** Catálogo de ejemplo. Valores nutricionales y cantidades son de muestra. */
object RecetasData {

    val recetas: List<Receta> = listOf(
        Receta(
            id = 1, titulo = "Pancakes de avena y banano", emoji = "🥞", momento = "Desayuno",
            resumen = "Suaves, dulces de forma natural y fáciles de agarrar.",
            minutos = 15, kcal = 210, proteinas = 6.5, carbohidratos = 32, grasas = 4.2,
            aporteClave = "Rico en potasio natural, fibra prebiótica digestiva y energía sostenida sin azúcares añadidos.",
            consejo = "Cocina a fuego bajo con tapa para que esponjen sin quemarse. ¡Se cortan fácil en bastones para manitas pequeñas!",
            distintivo = "BLW apto", etiquetas = listOf("Avena probada", "Frutas 100% seguras"),
            alergenos = setOf("Huevo", "Sin gluten / Celiaquía"),
            ingredientes = listOf(
                Ingrediente("🍌", "1 banano maduro mediano", "Dulzura natural y humedad"),
                Ingrediente("🌾", "1 taza de copos de avena finos", "Triturados o enteros según preferencia"),
                Ingrediente("🥚", "1 huevo campero", "O sustituto: 1 cda semillas de lino hidratadas"),
                Ingrediente("🥛", "1/4 taza de leche vegetal", "Avena, almendra o leche materna/fórmula"),
                Ingrediente("🌿", "1 pizca de canela de Ceilán", "Aroma suave y seguro para infantes")
            ),
            pasos = listOf(
                PasoReceta("Lavar ingredientes y pelar banano",
                    "Lava tus manos y los ingredientes. Pela el banano y deja los demás ingredientes a la mano.",
                    3, "Alistar", "Todo listo",
                    "Usa un banano bien maduro: es más dulce y se machaca fácilmente."),
                PasoReceta("Triturar y mezclar la masa 🥣",
                    "En un tazón, machaca el banano con un tenedor hasta formar un puré suave. Agrega el huevo batido y la avena poco a poco. Remueve con movimientos circulares hasta obtener una mezcla homogénea y espesa.",
                    4, "Mezclar masa", "Textura suave y grumosa",
                    "No agregues miel si tu bebé tiene menos de 1 año. El dulzor natural del banano maduro es más que suficiente y seguro para su sistema."),
                PasoReceta("Dorar en sartén a fuego bajo sin aceite",
                    "Calienta una sartén antiadherente a fuego bajo. Vierte porciones pequeñas de masa y cocina unos 2 minutos por lado hasta que doren y queden firmes. Deja enfriar antes de servir.",
                    6, "Cocción", "Dorado suave",
                    "Comprueba que el centro esté bien cocido por el huevo y córtalos en bastones.")
            )
        ),
        Receta(
            id = 2, titulo = "Crema suave de calabaza", emoji = "🥣", momento = "Almuerzo",
            resumen = "Cremosa, sin lácteos y con sabor naturalmente dulce.",
            minutos = 20, kcal = 140, proteinas = 2.5, carbohidratos = 24, grasas = 3.5,
            aporteClave = "Aporta betacarotenos (vitamina A) y fibra suave para la digestión.",
            consejo = "Si tu peque ya usa cuchara, deja la crema un poco más espesa para que no se escurra.",
            distintivo = "Veggie",
            ingredientes = listOf(
                Ingrediente("🎃", "2 tazas de calabaza en cubos", "Cocida hasta que esté muy blanda"),
                Ingrediente("🥔", "1/2 papa mediana", "Da cremosidad sin lácteos"),
                Ingrediente("🫒", "1 cdta de aceite de oliva", "Ayuda a absorber las vitaminas")
            ),
            pasos = listOf(
                PasoReceta("Lavar y cortar", "Lava, pela y corta la calabaza y la papa en cubos pequeños.", 4, "Alistar"),
                PasoReceta("Cocer al vapor", "Cocina los cubos al vapor unos 12 minutos, hasta que se aplasten sin esfuerzo.", 12, "Cocción", "Muy blandos"),
                PasoReceta("Licuar hasta lograr una crema", "Licúa con un poco del agua de cocción y agrega el aceite al final. Deja entibiar antes de servir.", 4, "Licuado", "Textura cremosa",
                    "Prueba siempre la temperatura antes de servir.")
            )
        ),
        Receta(
            id = 3, titulo = "Arroz con verduras y pollo desmenuzado", emoji = "🍚", momento = "Almuerzo & Cena",
            resumen = "Textura picadita suave, aporte de hierro y proteína.",
            minutos = 25, kcal = 220, proteinas = 14.0, carbohidratos = 28, grasas = 5.0,
            aporteClave = "Combina el hierro del pollo con la vitamina C de las verduras para absorberlo mejor.",
            consejo = "Desmenuza el pollo en hebras finas para evitar riesgo de atragantamiento.",
            etiquetas = listOf("Proteína", "Sin gluten"),
            ingredientes = listOf(
                Ingrediente("🍚", "1/2 taza de arroz", "Bien cocido y suave"),
                Ingrediente("🍗", "1/2 pechuga de pollo", "Bien cocida y desmenuzada fino"),
                Ingrediente("🥕", "1/2 taza de zanahoria y calabacín", "En cubitos muy blandos")
            ),
            pasos = listOf(
                PasoReceta("Cocer arroz y pollo", "Cocina el arroz y el pollo por separado hasta que estén muy blandos y bien cocidos.", 12, "Cocción"),
                PasoReceta("Cocinar las verduras", "Cocina las verduras al vapor hasta que se aplasten con un tenedor.", 8, "Verduras", "Cubitos blandos"),
                PasoReceta("Mezclar y desmenuzar", "Desmenuza el pollo en hebras finas, mezcla todo y deja entibiar.", 5, "Mezcla", "Textura picadita suave")
            )
        ),
        Receta(
            id = 4, titulo = "Crema de verduras con queso", emoji = "🥕", momento = "Almuerzo",
            resumen = "Zanahorias, calabacín y daditos de queso fresco pasteurizado.",
            minutos = 18, kcal = 165, proteinas = 6.0, carbohidratos = 14, grasas = 8.0,
            aporteClave = "Aporta calcio, proteína y vitamina A en un plato suave.",
            consejo = "Usa siempre queso pasteurizado y córtalo en daditos muy pequeños.",
            etiquetas = listOf("Lácteos pasteurizados"), alergenos = setOf("Sin lactosa"),
            ingredientes = listOf(
                Ingrediente("🥕", "1 zanahoria mediana", "Cocida hasta quedar muy blanda"),
                Ingrediente("🥒", "1/2 calabacín", "Sin semillas grandes"),
                Ingrediente("🧀", "2 cdas de queso fresco pasteurizado", "En daditos pequeños")
            ),
            pasos = listOf(
                PasoReceta("Preparar las verduras", "Lava y corta la zanahoria y el calabacín en rodajas finas.", 4, "Alistar"),
                PasoReceta("Cocer y licuar", "Cocina al vapor 10 minutos y licúa hasta lograr una crema suave.", 10, "Licuado", "Textura cremosa"),
                PasoReceta("Agregar el queso", "Sirve tibio con los daditos de queso encima.", 4, "Servir", "", "Revisa que el queso sea pasteurizado.")
            )
        ),
        Receta(
            id = 5, titulo = "Mini hamburguesas de lentejas", emoji = "🍔", momento = "Fav del peque ⭐",
            resumen = "Formato ideal para agarre en pinza motriz fina.",
            minutos = 30, kcal = 195, proteinas = 9.0, carbohidratos = 26, grasas = 4.0,
            aporteClave = "Fuente de hierro vegetal y fibra; combínalas con una fruta con vitamina C.",
            consejo = "Sírvelas tibias y cortadas en tiras o mini discos que pueda sostener.",
            desdeMeses = 8, etiquetas = listOf("Hierro vegetal", "BLW"),
            alergenos = setOf("Huevo", "Sin gluten / Celiaquía"),
            ingredientes = listOf(
                Ingrediente("🫘", "1 taza de lentejas cocidas", "Bien blandas y escurridas"),
                Ingrediente("🌾", "1/4 taza de avena", "Para dar consistencia"),
                Ingrediente("🥚", "1 huevo", "Bien cocido en el horno")
            ),
            pasos = listOf(
                PasoReceta("Triturar las lentejas", "Tritura las lentejas con un tenedor y mezcla con la avena y el huevo.", 8, "Mezcla", "Masa firme pero húmeda"),
                PasoReceta("Formar las hamburguesas", "Forma discos pequeños y colócalos en una bandeja con papel horno.", 7, "Formar"),
                PasoReceta("Hornear", "Hornea a 180 °C por 15 minutos, volteando a la mitad. Deja enfriar.", 15, "Cocción", "Doradas por fuera",
                    "El centro debe quedar bien cocido.")
            )
        ),
        Receta(
            id = 6, titulo = "Fideos de calabacín con ricotta", emoji = "🍝", momento = "Cena suave",
            resumen = "Espirales suaves con tomatitos pelados y ricotta.",
            minutos = 15, kcal = 150, proteinas = 7.0, carbohidratos = 10, grasas = 7.0,
            aporteClave = "Ligero para la noche, con proteína del queso y vitamina C del tomate.",
            consejo = "Corta los fideos en trozos cortos para que sean fáciles de comer.",
            etiquetas = listOf("Sin acidez", "Rápido"), alergenos = setOf("Sin lactosa"),
            ingredientes = listOf(
                Ingrediente("🥒", "1 calabacín mediano", "En espirales cortos"),
                Ingrediente("🧀", "2 cdas de ricotta", "Pasteurizada"),
                Ingrediente("🍅", "2 tomates cherry pelados", "Sin piel ni semillas")
            ),
            pasos = listOf(
                PasoReceta("Hacer los espirales", "Corta el calabacín en espirales y luego en trozos de unos 5 cm.", 4, "Alistar"),
                PasoReceta("Saltear suave", "Cocina los fideos 3 minutos al vapor o en sartén sin aceite hasta que estén tiernos.", 5, "Cocción", "Tiernos"),
                PasoReceta("Servir", "Mezcla con la ricotta y los tomates picados finos.", 3, "Servir", "", "Los tomates sin piel son más fáciles de tragar.")
            )
        ),
        Receta(
            id = 7, titulo = "Tortillitas de espinaca y maíz", emoji = "🥬", momento = "Diversión BLW",
            resumen = "Textura esponjosa cortada en tiras para agarrar.",
            minutos = 12, kcal = 175, proteinas = 8.0, carbohidratos = 15, grasas = 7.0,
            aporteClave = "Aporta hierro, folato y proteína del huevo en un formato fácil de agarrar.",
            consejo = "Córtalas en tiras del largo de su puño para que las sostenga sin esfuerzo.",
            etiquetas = listOf("Huevo bien cocido"), alergenos = setOf("Huevo"),
            ingredientes = listOf(
                Ingrediente("🥚", "1 huevo", "Bien cocido"),
                Ingrediente("🥬", "1/2 taza de espinaca picada", "Picada muy fina"),
                Ingrediente("🌽", "1/4 taza de maíz tierno", "Triturado para evitar atragantamientos")
            ),
            pasos = listOf(
                PasoReceta("Preparar la mezcla", "Bate el huevo y mezcla con la espinaca y el maíz triturado.", 4, "Mezcla", "Mezcla homogénea"),
                PasoReceta("Cocinar", "Cocina porciones pequeñas en sartén antiadherente a fuego bajo, 2 minutos por lado.", 5, "Cocción", "Firmes y doradas",
                    "El huevo debe quedar bien cocido."),
                PasoReceta("Cortar en tiras", "Deja entibiar y corta en tiras para servir.", 3, "Servir")
            )
        ),
        Receta(
            id = 8, titulo = "Papilla rústica de frutas horneadas", emoji = "🍎", momento = "Merienda",
            resumen = "Manzana y pera caramelizadas de forma natural.",
            minutos = 20, kcal = 130, proteinas = 1.5, carbohidratos = 30, grasas = 2.0,
            aporteClave = "Fibra y vitaminas de la fruta, con omega 3 de la chía y sin azúcar añadido.",
            consejo = "Déjala con pequeños trozos si tu peque ya mastica, o tritúrala más si es muy pequeño.",
            etiquetas = listOf("Omega 3", "100% Sin Azúcar"),
            ingredientes = listOf(
                Ingrediente("🍎", "1 manzana", "Sin piel ni semillas"),
                Ingrediente("🍐", "1 pera madura", "Sin piel ni semillas"),
                Ingrediente("🌱", "1 cdta de semillas de chía molidas", "Aportan omega 3")
            ),
            pasos = listOf(
                PasoReceta("Cortar la fruta", "Pela y corta la manzana y la pera en trozos.", 4, "Alistar"),
                PasoReceta("Hornear", "Hornea a 180 °C por 15 minutos hasta que estén blandas y caramelizadas.", 15, "Horneado", "Dulce natural"),
                PasoReceta("Triturar", "Aplasta con un tenedor, agrega la chía molida y deja entibiar.", 3, "Servir", "Textura rústica")
            )
        )
    )

    fun porId(id: Int): Receta = recetas.first { it.id == id }

    /** Lunes con 2 comidas y el resto de la semana con 1 por día. */
    fun armarSemana(orden: List<Int>): List<DiaPlan> {
        val dias = listOf(
            "LUN" to "Lunes", "MAR" to "Martes", "MIÉ" to "Miércoles", "JUE" to "Jueves",
            "VIE" to "Viernes", "SÁB" to "Sábado", "DOM" to "Domingo"
        )
        val porDia = listOf(2, 1, 1, 1, 1, 1, 1)
        var i = 0
        return dias.mapIndexed { idx, (abrev, nombre) ->
            val recetasDia = List(porDia[idx]) { porId(orden[i++]) }
            DiaPlan(abrev, nombre, 18 + idx, recetasDia)
        }
    }
}