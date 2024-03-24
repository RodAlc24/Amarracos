package com.rodalc.amarracos.ronda

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MarcadorPuntos(puntos: Int, pareja: String, juegos: Int) {
    Column(
        modifier = Modifier.padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "$pareja: $juegos")
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color.LightGray)
        ) {
            Text(
                text = puntos.toString(),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
