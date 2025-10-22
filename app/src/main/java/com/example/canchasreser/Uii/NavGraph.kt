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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize

@Composable
fun AppNavHost(viewModel: CatalogoViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "catalogo") {
        composable("catalogo") {
            CatalogoScreen(navController, viewModel)
        }
        composable(
            route = "detalle/{canchaId}",
            arguments = listOf(navArgument("canchaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("canchaId")
            if (id != null) {
                DetalleCanchaScreen(canchaId = id, viewModel = viewModel)
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
