package com.example.canchasreser.Screen

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoraSelector(
    hora: String,
    onHoraSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val horasDisponibles = listOf(
        "10:00",
        "11:00", "12:00", "13:00",
        "14:00", "15:00", "16:00",
        "17:00", "18:00", "19:00",
        "20:00", "21:00"
    )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        OutlinedTextField(
            value = hora,
            onValueChange = {},
            readOnly = true,
            label = { Text("Hora") },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            horasDisponibles.forEach { h ->
                DropdownMenuItem(
                    text = { Text(h) },
                    onClick = {
                        onHoraSelected(h)
                        expanded = false
                    }
                )
            }
        }
    }
}