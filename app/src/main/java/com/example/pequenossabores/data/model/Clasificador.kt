package com.example.pequenossabores.data.model

/** Clasifica un ingrediente (en inglés o español) por palabras clave. */
object Clasificador {

    const val FRUTAS = "Frutas y verduras"
    const val LACTEOS = "Lácteos y alternativas"
    const val CEREALES = "Cereales y harinas"
    const val PROTEINAS = "Proteínas"
    const val OTROS = "Otros y especias"

    val orden = listOf(FRUTAS, LACTEOS, CEREALES, PROTEINAS, OTROS)

    private val otros = setOf(
        "salt", "sugar", "oil", "vinegar", "stock", "sauce", "cumin", "paprika", "cinnamon", "honey",
        "yeast", "baking", "spice", "seasoning", "curry", "chilli", "chili", "oregano", "thyme",
        "vanilla", "mustard", "ketchup", "cocoa", "sal", "azúcar", "azucar", "aceite", "canela", "miel", "chía", "chia"
    )
    private val lacteos = setOf(
        "milk", "cheese", "butter", "cream", "yogurt", "yoghurt", "ricotta", "parmesan",
        "mozzarella", "cheddar", "leche", "queso", "mantequilla", "yogur"
    )
    private val proteinas = setOf(
        "chicken", "beef", "pork", "lamb", "turkey", "bacon", "sausage", "ham", "egg", "fish", "salmon",
        "tuna", "cod", "prawn", "shrimp", "lentil", "bean", "chickpea", "tofu", "mince", "steak", "duck",
        "pollo", "carne", "huevo", "pescado", "lenteja", "frijol", "garbanzo"
    )
    private val cereales = setOf(
        "flour", "rice", "pasta", "bread", "oat", "noodle", "spaghetti", "macaroni", "wheat", "couscous",
        "tortilla", "breadcrumb", "penne", "lasagne", "pastry", "harina", "arroz", "avena", "fideo", "pan", "maíz", "maiz"
    )
    private val verduras = setOf(
        "onion", "garlic", "tomato", "potato", "carrot", "pepper", "spinach", "lettuce", "apple", "banana",
        "lemon", "lime", "orange", "mushroom", "cabbage", "celery", "pumpkin", "zucchini", "courgette",
        "broccoli", "cucumber", "ginger", "parsley", "coriander", "basil", "mint", "leek", "pea", "aubergine",
        "eggplant", "avocado", "strawberry", "pear", "berry", "mango", "herb", "cebolla", "ajo", "tomate",
        "papa", "zanahoria", "espinaca", "manzana", "banano", "calabaza", "calabacín", "pera", "lechuga", "fresa"
    )

    private fun tokens(nombre: String) =
        nombre.lowercase().split(Regex("[^a-záéíóúñ]+")).filter { it.isNotBlank() }

    private fun coincide(t: List<String>, claves: Set<String>) = t.any { w ->
        w in claves || (w.endsWith("s") && w.dropLast(1) in claves) || (w.endsWith("es") && w.dropLast(2) in claves)
    }

    fun categoria(nombre: String): String {
        val t = tokens(nombre)
        return when {
            coincide(t, otros) -> OTROS
            coincide(t, lacteos) -> LACTEOS
            coincide(t, proteinas) -> PROTEINAS
            coincide(t, cereales) -> CEREALES
            coincide(t, verduras) -> FRUTAS
            else -> OTROS
        }
    }

    fun emojiDe(categoria: String) = when (categoria) {
        FRUTAS -> "🥬"; LACTEOS -> "🥛"; CEREALES -> "🌾"; PROTEINAS -> "🍗"; else -> "🧂"
    }

    /** Usa los mismos textos que las alergias del perfil del peque. */
    fun alergenos(nombres: List<String>): Set<String> {
        val t = nombres.flatMap { tokens(it) }
        return buildSet {
            if (coincide(t, setOf("egg", "huevo"))) add("Huevo")
            if (coincide(t, lacteos)) add("Sin lactosa")
            if (coincide(t, setOf("flour", "bread", "pasta", "noodle", "spaghetti", "macaroni", "wheat", "couscous",
                    "breadcrumb", "penne", "lasagne", "pastry", "oat", "harina", "fideo", "avena"))) add("Sin gluten / Celiaquía")
            if (coincide(t, setOf("peanut", "almond", "walnut", "hazelnut", "cashew", "pecan", "pistachio", "nut",
                    "maní", "mani", "almendra", "nuez"))) add("Maní / Frutos secos")
            if (coincide(t, setOf("fish", "salmon", "tuna", "cod", "anchovy", "anchovies", "haddock", "mackerel",
                    "sardine", "pescado", "atún", "atun"))) add("Pescado")
        }
    }
}