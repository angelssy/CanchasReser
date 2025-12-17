package com.example.canchasreser.Screen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.canchasreser.viewmodel.CanchasViewModel
import com.example.canchasreser.model.Cancha
import com.example.canchasreser.Utils.formatPrecio
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(navController: NavController, viewModel: CanchasViewModel ) {

    val canchas by viewModel.canchas
    val loading by viewModel.loading
    val error by viewModel.error

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Catálogo de Canchas", color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0A6E2F)
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .background(Color(0xFFE8F5E9))
                .fillMaxSize()
        ) {

            if (loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = Color(0xFF0A6E2F)) }
            }

            if (error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: $error", color = Color.Red)
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(canchas) { cancha ->
                    CanchaCard(cancha = cancha) {
                        navController.navigate("detalle/${cancha.id}")
                    }
                }
            }
        }
    }
}
@Composable
fun CanchaCard(cancha: Cancha, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {

            val painter = rememberAsyncImagePainter(
                model = cancha.imagen
            )

            Image(
                painter = painter,
                contentDescription = cancha.nombre,
                modifier = Modifier.size(90.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(cancha.nombre, style = MaterialTheme.typography.titleLarge)

                Text("Precio: ${formatPrecio(cancha.precioHora)}")
                cancha.descripcion?.let { Text(it, maxLines = 2) }
            }
        }
    }
}



