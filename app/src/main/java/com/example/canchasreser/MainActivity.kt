package com.example.canchasreser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.canchasreser.Uii.AppNavHost
import com.example.canchasreser.ViewModel.CatalogoViewModel

class MainActivity : ComponentActivity() {

    // Instancia del ViewModel compartido
    private val viewModel: CatalogoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavHost(viewModel = viewModel)
            }
        }
    }
}