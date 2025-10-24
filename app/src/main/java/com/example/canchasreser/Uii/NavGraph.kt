package com.example.canchasreser.Uii

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.canchasreser.viewmodel.CatalogoViewModel
import com.example.canchasreser.viewmodel.AuthViewModel
import com.example.canchasreser.viewmodel.CarritoViewModel

@Composable
fun NavGraph(
    catalogoViewModel: CatalogoViewModel,
    authViewModel: AuthViewModel,
    carritoViewModel: CarritoViewModel = viewModel()
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        // Pantalla de login
        composable("login") {
            LoginScreen(navController = navController, viewModel = authViewModel)
        }

        // Pantalla de registro
        composable("register") {
            RegisterScreen(navController = navController, viewModel = authViewModel)
        }

        // Pantalla de catálogo
        composable("catalogo") {
            if (authViewModel.usuarioActual.value == null) {
                navController.navigate("login")
            } else {
                CatalogoScreen(navController = navController, viewModel = catalogoViewModel)
            }
        }

        // Pantalla de detalle de cancha
        composable(
            route = "detalle/{canchaId}",
            arguments = listOf(navArgument("canchaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("canchaId")
            if (id != null) {
                DetalleCanchaScreen(
                    canchaId = id,
                    viewModel = catalogoViewModel,
                    carritoViewModel = carritoViewModel,
                    navController = navController
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Cancha no encontrada")
                }
            }
        }

        // Pantalla de carrito
        composable("carrito") {
            CarritoScreen(navController = navController, viewModel = carritoViewModel)
        }

        // Pantalla de formulario de reserva
        composable("reservaForm") {
            ReservaFormScreen(navController = navController, carritoViewModel = carritoViewModel)
        }

        // Pantalla de compra exitosa
        composable("compraExitosa") {
            CompraExitosaScreen(navController = navController)
        }
    }
}
