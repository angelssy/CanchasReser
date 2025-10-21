package com.example.canchasreser


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.canchasreser.Uii.CatalogoScreen
import com.example.canchasreser.Uii.DetalleProductoScreen
import com.example.canchasreser.ViewModel.CatalogoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel = remember { CatalogoViewModel() }

            NavHost(navController = navController, startDestination = "catalogo") {
                composable("catalogo") {
                    CatalogoScreen(navController = navController, viewModel = viewModel)
                }
                composable("detalle/{productoId}") { backStack ->
                    val idStr = backStack.arguments?.getString("productoId")
                    val id = idStr?.toIntOrNull() ?: -1
                    DetalleProductoScreen(productoId = id, viewModel = viewModel)
                }
            }
        }
    }
}