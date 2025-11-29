package com.example.canchasreser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.canchasreser.Screen.NavGraph
import com.example.canchasreser.viewmodel.AuthViewModel
import com.example.canchasreser.viewmodel.CanchasViewModel
import com.example.canchasreser.viewmodel.CarritoViewModel
import com.example.canchasreser.ui.theme.CanchasTheme

class MainActivity : ComponentActivity() {

    // ViewModels principales
    private val authViewModel: AuthViewModel by viewModels()
    private val canchasViewModel: CanchasViewModel by viewModels()
    private val carritoViewModel: CarritoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CanchasTheme {
                NavGraph(
                    authViewModel = authViewModel,
                    canchasViewModel = canchasViewModel,
                    carritoViewModel = carritoViewModel
                )
            }
        }
    }
}
