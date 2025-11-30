package com.example.canchasreser.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Inicio : BottomNavItem("inicio", Icons.Default.Home, "Inicio")
    object Catalogo : BottomNavItem("catalogo", Icons.Default.List, "Catálogo")
    object About : BottomNavItem("about", Icons.Default.Info, "Acerca")
    object Contact : BottomNavItem("contact", Icons.Default.Phone, "Contacto")
}