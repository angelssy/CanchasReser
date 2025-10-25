package com.example.canchasreser.Screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.canchasreser.viewmodel.CatalogoViewModel
import com.example.canchasreser.viewmodel.AuthViewModel
import com.example.canchasreser.viewmodel.CarritoViewModel
import androidx.compose.ui.Modifier

@Composable
fun NavGraph(
    catalogoViewModel: CatalogoViewModel,
    authViewModel: AuthViewModel,
    carritoViewModel: CarritoViewModel = viewModel()
) {
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
                navController.navigate("login")
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
                DetalleCanchaScreen(
                    canchaId = id,
                    viewModel = catalogoViewModel,
                    carritoViewModel = carritoViewModel,
                    navController = navController
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text("Cancha no encontrada")
                }
            }
        }

        composable("carrito") {
            CarritoScreen(navController = navController, viewModel = carritoViewModel)
        }

        composable("reservaForm") {
            ReservaFormScreen(navController = navController, carritoViewModel = carritoViewModel)
        }

        composable("compraExitosa") {
            CompraExitosaScreen(navController = navController)
        }

        // Compra Rechazada con mensaje dinámico
        composable(
            route = "compraRechazada/{mensajeError}",
            arguments = listOf(navArgument("mensajeError") { defaultValue = "Hubo un error con tu compra ❌" })
        ) { backStackEntry ->
            val mensajeError = backStackEntry.arguments?.getString("mensajeError") ?: ""
            CompraRechazadaScreen(navController = navController, mensajeError = mensajeError)
        }

        // Pantalla Back Office
        composable("backOffice") {
            BackOfficeScreen(navController = navController, viewModel = catalogoViewModel)
        }

        // Pantalla Agregar Producto
        composable("agregarProducto") {
            AgregarProductoScreen(navController = navController)
        }
    }
}
