package com.rodalc.amarracos.main

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

// PANTALLA AUXILIAR, VA A SER APISONADA
@Composable
fun PantallaVacia(navController: NavController) {
    Button(onClick = { navController.navigate("pantallaInicial") }) {
        Text(text = "Volver");
    }
}
