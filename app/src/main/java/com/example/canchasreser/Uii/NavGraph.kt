package com.example.canchasreser.Uii

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.canchasreser.ViewModel.CatalogoViewModel
import com.example.canchasreser.viewmodel.AuthViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.viewmodel.compose.viewModel  // Asegúrate de importar viewModel

@Composable
fun NavGraph(catalogoViewModel: CatalogoViewModel, authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController, viewModel = authViewModel)
        }

        composable("register") {
            RegisterScreen(navController = navController, viewModel = authViewModel)
        }

        composable("catalogo") {
            if (authViewModel.usuarioActual.value == null) {
                navController.navigate("login") // Redirige a login si no está autenticado
            } else {
                CatalogoScreen(navController = navController, viewModel = catalogoViewModel)
            }
        }

        composable(
            route = "detalle/{canchaId}",
            arguments = listOf(navArgument("canchaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("canchaId")
            if (id != null) {
                DetalleCanchaScreen(canchaId = id, viewModel = catalogoViewModel)
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Cancha no encontrada")
                }
            }
        }
    }
}
