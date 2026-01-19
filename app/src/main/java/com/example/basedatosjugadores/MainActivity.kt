package com.example.basedatosjugadores

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.basedatosjugadores.screens.*
import com.example.basedatosjugadores.screens.estadisticas.EstadisticasEquipoScreen
import com.example.basedatosjugadores.screens.estadisticas.PlantillaEquipoScreen
import com.example.basedatosjugadores.server2.TablaScreen2
import com.example.basedatosjugadores.server2.estadisticas2.EstadisticasEquipoScreen2
import com.example.basedatosjugadores.server2.estadisticas2.PlantillaEquipoScreen2
import com.example.basedatosjugadores.server3.TablaScreen3
import com.example.basedatosjugadores.server4.TablaScreen4
import com.example.basedatosjugadores.server5.TablaScreen5
import com.example.basedatosjugadores.server5.estadisticas5.EstadisticasEquipoScreen5
import com.example.basedatosjugadores.server5.estadisticas5.PlantillaEquipoScreen5
import com.example.basedatosjugadores.server6.TablaScreen6
import com.example.basedatosjugadores.server7.TablaScreen7
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val startDestination = if (auth.currentUser != null) {
            "menu" // Si el usuario está autenticado, empieza en el menú
        } else {
            "login_screen" // Si no está autenticado, empieza en la pantalla de login
        }

        setContent {
            NavigationWrapper(startDestination = startDestination)
        }
    }
}

@Composable
fun NavigationWrapper(startDestination: String) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login_screen") {
            LoginScreen(navController)
        }
        composable("register_screen") {
            RegisterScreen(navController)
        }
        composable("menu") {
            MenuScreen(navController)
        }
        composable("session_verification") { // 👈 Verificación de sesión
            SessionVerificationScreen(navController)
        }
        composable("favoritos_screen") { // ✅ NUEVA PANTALLA DE FAVORITOS
            FavoritosScreen(navController)
        }
        composable("tabla1") {
            TablaScreen(navController)
        }
        composable("tabla2") {
            TablaScreen2(navController)
        }
        composable("tabla3") {
            TablaScreen3(navController)
        }
        composable("tabla4") {
            TablaScreen4(navController)
        }
        composable("tabla5") {
            TablaScreen5(navController)
        }
        composable("tabla6") {
            TablaScreen6(navController)
        }
        composable("tabla7") {
            TablaScreen7(navController)
        }
        composable(
            "estadisticas/{equipo}",
            arguments = listOf(navArgument("equipo") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedName = backStackEntry.arguments?.getString("equipo").orEmpty()
            val teamName = java.net.URLDecoder.decode(encodedName, "UTF-8")
            EstadisticasEquipoScreen(nombreEquipo = teamName, navController = navController)
        }
        composable(
            "plantillaPremier/{nombreEquipo}",
            arguments = listOf(navArgument("nombreEquipo") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedName = backStackEntry.arguments?.getString("nombreEquipo").orEmpty()
            val teamName = java.net.URLDecoder.decode(encodedName, "UTF-8")
            PlantillaEquipoScreen(nombreEquipo = teamName, navController = navController)
        }
        composable(
            "estadisticas2/{equipo}",
            arguments = listOf(navArgument("equipo") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedTeam = backStackEntry.arguments?.getString("equipo").orEmpty()
            val teamName = java.net.URLDecoder.decode(encodedTeam, "UTF-8")
            EstadisticasEquipoScreen2(nombreEquipo = teamName, navController = navController)
        }
        composable(
            "plantillaLaLiga/{nombreEquipo}",
            arguments = listOf(navArgument("nombreEquipo") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedName = backStackEntry.arguments?.getString("nombreEquipo").orEmpty()
            val teamName = java.net.URLDecoder.decode(encodedName, "UTF-8")
            PlantillaEquipoScreen2(nombreEquipo = teamName, navController = navController)
        }
        composable(
            "estadisticas5/{equipo}",
            arguments = listOf(navArgument("equipo") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedTeam = backStackEntry.arguments?.getString("equipo").orEmpty()
            val teamName = java.net.URLDecoder.decode(encodedTeam, "UTF-8")
            EstadisticasEquipoScreen5(nombreEquipo = teamName, navController = navController)
        }
        composable(
            "plantillaLigue1/{nombreEquipo}",
            arguments = listOf(navArgument("nombreEquipo") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedName = backStackEntry.arguments?.getString("nombreEquipo").orEmpty()
            val teamName = java.net.URLDecoder.decode(encodedName, "UTF-8")
            PlantillaEquipoScreen5(nombreEquipo = teamName, navController = navController)
        }
        composable(
            route = "estadisticas_jugador/{jugadorId}",
            arguments = listOf(navArgument("jugadorId") { type = NavType.StringType })
        ) { backStackEntry ->
            val jugadorId = backStackEntry.arguments?.getString("jugadorId") ?: ""
            EstadisticasJugadorScreen(playerId = jugadorId, navController = navController)
        }


    }
}
