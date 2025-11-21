package com.example.canchasreser.Screen
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.canchasreser.viewmodel.CatalogoViewModel
import com.example.canchasreser.model.Cancha
import com.example.canchasreser.Utils.formatPrecio
import androidx.compose.foundation.background


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(navController: NavController, viewModel: CatalogoViewModel) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.cargarProductos(context)
    }

    val canchas by viewModel.productos.collectAsState()
    val loading by viewModel.loading.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Catálogo de Canchas",
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0A6E2F)
                )
            )
        },

        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ExtendedFloatingActionButton(
                    onClick = { navController.navigate("agregarProducto") },
                    containerColor = Color(0xFF0A6E2F)
                ) {
                    Text("Agregar Producto", color = Color.White)
                }

                ExtendedFloatingActionButton(
                    onClick = { navController.navigate("backOffice") },
                    containerColor = Color(0xFF0A6E2F)
                ) {
                    Text("Back Office", color = Color.White)
                }
            }
        }
    ) { padding ->

        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .background(Color(0xFFE8F5E9)))
        {
            if (loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF0A6E2F))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(canchas, key = { it.id }) { cancha ->
                        CanchaCard(cancha) {
                            navController.navigate("detalle/${cancha.id}")
                        }
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
            .clickable { onClick() }
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {

        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val painter = rememberAsyncImagePainter(
                model = cancha.imagen?.let {
                    if (it.startsWith("http"))
                        it
                    else
                        "android.resource://com.example.canchasreser/drawable/$it"
                }
            )

            Image(
                painter = painter,
                contentDescription = cancha.nombre,
                modifier = Modifier
                    .size(95.dp)
                    .padding(4.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = cancha.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF0A6E2F)
                )

                Text(
                    text = formatPrecio(cancha.precioHora),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF2E7D32)
                )

                cancha.descripcion?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
