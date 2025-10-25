package com.example.canchasreser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import com.example.canchasreser.viewmodel.AuthViewModel

import com.example.canchasreser.viewmodel.CatalogoViewModel
import com.example.canchasreser.Screen.NavGraph

class MainActivity : ComponentActivity() {

    private val catalogoViewModel: CatalogoViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                NavGraph(catalogoViewModel = catalogoViewModel, authViewModel = authViewModel)
            }
        }
    }
}
