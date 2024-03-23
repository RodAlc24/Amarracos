package com.rodalc.amarracos

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun PantallaVacia(navController: NavController) {
    Button(onClick = { navController.navigate("pantallaInicial") }) {
        Text(text = "Volver");
    }
}
