package com.example.canchasreser.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.canchasreser.viewmodel.AuthViewModel

@Composable
fun BottomBar(navController: NavController, authViewModel: AuthViewModel) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(containerColor = Color(0xFF1B5E20)) {

        NavigationBarItem(
            selected = currentRoute == "inicio",
            onClick = { navController.navigate("inicio") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio", tint = Color.White) },
            label = { Text("Inicio", color = Color.White) }
        )

        NavigationBarItem(
            selected = currentRoute == "catalogo",
            onClick = { navController.navigate("catalogo") },
            icon = { Icon(Icons.Default.SportsSoccer, contentDescription = "Canchas", tint = Color.White) },
            label = { Text("Canchas", color = Color.White) }
        )

        NavigationBarItem(
            selected = currentRoute == "about",
            onClick = { navController.navigate("about") },
            icon = { Icon(Icons.Default.Info, contentDescription = "Acerca", tint = Color.White) },
            label = { Text("Acerca", color = Color.White) }
        )

        NavigationBarItem(
            selected = currentRoute == "contact",
            onClick = { navController.navigate("contact") },
            icon = { Icon(Icons.Default.ContactMail, contentDescription = "Contacto", tint = Color.White) },
            label = { Text("Contacto", color = Color.White) }
        )

        // ⭐ SOLO ADMIN VE ESTA OPCIÓN ⭐
        if (authViewModel.esAdmin()) {
            NavigationBarItem(
                selected = currentRoute == "backoffice",
                onClick = { navController.navigate("backoffice") },
                icon = { Icon(Icons.Default.Settings, contentDescription = "Admin", tint = Color.White) },
                label = { Text("Admin", color = Color.White) }
            )
        }
    }
}
