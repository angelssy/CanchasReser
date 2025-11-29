package com.example.canchasreser.Screen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.canchasreser.viewmodel.AuthViewModel
import com.example.canchasreser.viewmodel.CanchasViewModel
import com.example.canchasreser.viewmodel.CarritoViewModel

@Composable
fun NavGraph(
    authViewModel: AuthViewModel,
    canchasViewModel: CanchasViewModel,
    carritoViewModel: CarritoViewModel
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // LOGIN
        composable("login") {
            LoginScreen(navController, authViewModel)
        }

        // REGISTRO
        composable("register") {
            RegisterScreen(navController, authViewModel)
        }

        // CATÁLOGO
        composable("catalogo") {

            if (authViewModel.usuarioActual.value == null) {
                navController.navigate("login")
            } else {
                CatalogoScreen(
                    navController = navController,
                    viewModel = canchasViewModel
                )
            }
        }

        // DETALLE
        composable(
            route = "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->

            val id = backStackEntry.arguments?.getInt("id")

            if (id != null) {
                DetalleCanchaScreen(
                    canchaId = id,
                    viewModel = canchasViewModel,
                    carritoViewModel = carritoViewModel,
                    navController = navController
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { Text("ID inválido") }
            }
        }

        // CARRITO
        composable("carrito") {
            CarritoScreen(navController, carritoViewModel)
        }

        // FORMULARIO RESERVA
        composable("reservaForm") {
            ReservaFormScreen(navController, carritoViewModel)
        }

        // COMPRA EXITOSA
        composable("compraExitosa") {
            CompraExitosaScreen(navController)
        }

        // COMPRA RECHAZADA
        composable(
            "compraRechazada/{msg}",
            arguments = listOf(navArgument("msg") {
                defaultValue = "Hubo un error en la compra"
            })
        ) { entry ->
            CompraRechazadaScreen(
                navController,
                mensajeError = entry.arguments?.getString("msg") ?: ""
            )
        }
    }
}