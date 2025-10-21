package com.example.canchasreser.Uii

import androidx.compose.foundation.layout.*
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

@Composable
fun AppNavHost(viewModel: CatalogoViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "catalogo") {
        composable("catalogo") {
            CatalogoScreen(navController, viewModel)
        }
        composable(
            route = "detalle/{productoId}",
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("productoId")
            if (id != null) {
                DetalleProductoScreen(productoId = id, viewModel = viewModel)
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Producto no encontrado")
                }
            }
        }
    }
}
