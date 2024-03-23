package com.rodalc.amarracos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodalc.amarracos.ui.theme.AmarracosTheme

@Composable
fun MarcadorPuntos(puntos: Int) {
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
@Composable
fun FaseJuego() {
    var puntosBuenos: Int by remember { mutableIntStateOf(0) }
    var puntosMalos: Int by remember { mutableIntStateOf(0) }

    Row {
        MarcadorPuntos(puntos = puntosBuenos)
        MarcadorPuntos(puntos = puntosMalos)
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun CustomInterface() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Primera caja con margen a la izquierda
        Box(
            modifier = Modifier
                .padding(16.dp) // Ajusta el margen según sea necesario
        ) {
            MarcadorPuntos(puntos = 0)
        }

        // Botones en una columna centrada
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { /* Acción del botón */ }) {
                Text("Botón 1asdfasfd")
            }
            Button(onClick = { /* Acción del botón */ }) {
                Text("Botón 2")
            }
        }

        // Última caja con margen a la derecha
        Box(
            modifier = Modifier
                .padding(16.dp) // Ajusta el margen según sea necesario
        ) {
            MarcadorPuntos(puntos = 0)
        }
    }
}
@Preview(showBackground = true, widthDp = 640, heightDp = 360)
@Composable
fun JuegoPreview() {
    AmarracosTheme {
        CustomInterface()
    }
}
