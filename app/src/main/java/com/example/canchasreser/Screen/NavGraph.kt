package com.example.canchasreser.Screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.canchasreser.navigation.BottomBar
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

        // INICIO (con barra)
        composable("inicio") {
            Scaffold(bottomBar = { BottomBar(navController, authViewModel) }) {
                InicioScreen(navController)
            }
        }

        // CATÁLOGO (con barra)
        composable("catalogo") {
            Scaffold(bottomBar = { BottomBar(navController, authViewModel) }) {
                CatalogoScreen(navController, canchasViewModel)
            }
        }

        // ACERCA DE (con barra)
        composable("about") {
            Scaffold(bottomBar = { BottomBar(navController, authViewModel) }) {
                AboutScreen(navController)
            }
        }

        // CONTACTO (con barra)
        composable("contact") {
            Scaffold(bottomBar = { BottomBar(navController, authViewModel) }) {
                ContactScreen(navController)
            }
        }

        // CARRITO (con tipo de cancha)
        composable(
            route = "carrito/{tipo}",
            arguments = listOf(navArgument("tipo") { type = NavType.StringType })
        ) { entry ->
            val tipo = entry.arguments?.getString("tipo") ?: "futbol"

            CarritoScreen(
                navController = navController,
                carritoViewModel = carritoViewModel,
                tipoCancha = tipo
            )
        }


        // BACKOFFICE (solo admin)
        composable("backoffice") {
            if (authViewModel.esAdmin()) {
                BackOfficeScreen(navController, authViewModel)
            } else {
                Text("Acceso Denegado")
            }
        }

        // AGREGAR PRODUCTO (solo admin)
        composable("agregarProducto") {
            if (authViewModel.esAdmin()) {
                AgregarProductoScreen(navController)
            } else {
                Text("Acceso Denegado")
            }
        }

        // DETALLE CANCHA (ID STRING)
        composable(
            route = "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { entry ->

            val id = entry.arguments?.getString("id")

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
                ) {
                    Text("ID inválido")
                }
            }
        }

        // FORMULARIO DE RESERVA
        composable("reservaForm") {
            ReservaFormScreen(navController, carritoViewModel)
        }

        // COMPRA EXITOSA
        composable(
            route = "compraExitosa/{resumen}",
            arguments = listOf(navArgument("resumen") { type = NavType.StringType })
        ) { entry ->
            CompraExitosaScreen(
                navController,
                entry.arguments?.getString("resumen") ?: ""
            )
        }

        // COMPRA RECHAZADA
        composable(
            route = "compraRechazada/{msg}",
            arguments = listOf(navArgument("msg") {
                defaultValue = "Hubo un error"
            })
        ) { entry ->
            CompraRechazadaScreen(
                navController,
                entry.arguments?.getString("msg") ?: ""
            )
        }
    }
}

