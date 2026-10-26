package com.example.pequenossabores.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.pequenossabores.data.repository.SesionPreferences
import com.example.pequenossabores.ui.components.Tab
import com.example.pequenossabores.ui.screens.*

sealed class Ruta(val ruta: String) {
    data object Bienvenida : Ruta("bienvenida")
    data object Registro : Ruta("registro/{login}") {
        fun conModo(login: Boolean) = "registro/$login"
    }
    data object Perfil : Ruta("perfil/{onboarding}") {
        fun conModo(onboarding: Boolean) = "perfil/$onboarding"
    }
    data object Inicio : Ruta("inicio")
    data object Plan : Ruta("plan")
    data object Receta : Ruta("receta/{id}") {
        fun conId(id: Int) = "receta/$id"
    }
    data object Preparacion : Ruta("preparacion/{id}") {
        fun conId(id: Int) = "preparacion/$id"
    }
}

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val nav = rememberNavController()

    // Con sesión y perfil completo -> Inicio; con sesión sin perfil -> Perfil; sin sesión -> Bienvenida
    val destinoInicial = remember {
        val sesion = SesionPreferences(context)
        when {
            !sesion.haySesion() -> Ruta.Bienvenida.ruta
            sesion.perfilCompleto() -> Ruta.Inicio.ruta
            else -> Ruta.Perfil.conModo(true)
        }
    }

    val irATab: (Tab) -> Unit = { tab ->
        when (tab) {
            Tab.INICIO -> nav.popBackStack(Ruta.Inicio.ruta, false)
            Tab.PLAN -> nav.navigate(Ruta.Plan.ruta) {
                popUpTo(Ruta.Inicio.ruta); launchSingleTop = true
            }
            Tab.PERFIL -> nav.navigate(Ruta.Perfil.conModo(false)) {
                popUpTo(Ruta.Inicio.ruta); launchSingleTop = true
            }
            Tab.PEQUECHEF -> { /* TODO pantalla del asistente */ }
        }
    }

    val cerrarSesion: () -> Unit = {
        SesionPreferences(context).cerrarSesion()
        nav.navigate(Ruta.Bienvenida.ruta) { popUpTo(nav.graph.id) { inclusive = true } }
    }

    NavHost(navController = nav, startDestination = destinoInicial) {

        composable(Ruta.Bienvenida.ruta) {
            PantallaBienvenida(
                onComenzar = { nav.navigate(Ruta.Registro.conModo(false)) },
                onIniciarSesion = { nav.navigate(Ruta.Registro.conModo(true)) }
            )
        }

        composable(
            route = Ruta.Registro.ruta,
            arguments = listOf(navArgument("login") { type = NavType.BoolType })
        ) { entrada ->
            PantallaRegistro(
                iniciarEnLogin = entrada.arguments?.getBoolean("login") ?: false,
                onAtras = { nav.popBackStack() },
                onExito = { tienePerfil ->
                    val destino = if (tienePerfil) Ruta.Inicio.ruta else Ruta.Perfil.conModo(true)
                    nav.navigate(destino) { popUpTo(nav.graph.id) { inclusive = true } }
                }
            )
        }

        composable(
            route = Ruta.Perfil.ruta,
            arguments = listOf(navArgument("onboarding") { type = NavType.BoolType })
        ) { entrada ->
            PantallaPerfil(
                esOnboarding = entrada.arguments?.getBoolean("onboarding") ?: false,
                onTab = irATab,
                onGuardado = {
                    nav.navigate(Ruta.Inicio.ruta) { popUpTo(nav.graph.id) { inclusive = true } }
                },
                onCerrarSesion = cerrarSesion
            )
        }

        composable(Ruta.Inicio.ruta) {
            PantallaInicio(
                onTab = irATab,
                onVerReceta = { nav.navigate(Ruta.Receta.conId(it)) },
                onCerrarSesion = cerrarSesion
            )
        }

        composable(Ruta.Plan.ruta) {
            PantallaPlan(
                onTab = irATab,
                onVerReceta = { nav.navigate(Ruta.Receta.conId(it)) },
                onCerrarSesion = cerrarSesion
            )
        }

        // composable(
        //    route = Ruta.Receta.ruta,
        //    arguments = listOf(navArgument("id") { type = NavType.IntType })
        // ) { entrada ->
        //    PantallaReceta(
        //        recetaId = entrada.arguments?.getInt("id") ?: 1,
        //        onAtras = { nav.popBackStack() },
        //        onIniciar = { nav.navigate(Ruta.Preparacion.conId(it)) }
        //    )
        // }

        composable(
            route = Ruta.Preparacion.ruta,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            PantallaPreparacion(
                recetaId = entrada.arguments?.getInt("id") ?: 1,
                onAtras = { nav.popBackStack() },
                onFinalizar = { nav.popBackStack(Ruta.Inicio.ruta, false) }
            )
        }
    }
}