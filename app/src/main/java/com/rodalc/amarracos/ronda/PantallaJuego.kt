package com.rodalc.amarracos.ronda

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rodalc.amarracos.Partida
import com.rodalc.amarracos.Ronda
import com.rodalc.amarracos.ui.theme.AmarracosTheme


@Composable
fun PantallaJuego(navController: NavController) {
    var juego = remember { mutableStateOf(Partida()) }
    var ronda = remember { mutableStateOf(Ronda.MUS) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MarcadorPuntos(
            juego.value.puntosPareja1,
            juego.value.nombrePareja1,
            juego.value.juegosPareja1
        )
        Column {
            Text(text = ronda.value.toString())

            when (ronda.value) {
                Ronda.MUS -> Mus(juego) { ronda.value = it }
                Ronda.GRANDE -> GrandeChica(Ronda.GRANDE, juego) { ronda.value = it }
                else -> GrandeChica(Ronda.CHICA, juego) { ronda.value = it }
            }
        }

        MarcadorPuntos(
            juego.value.puntosPareja2,
            juego.value.nombrePareja2,
            juego.value.juegosPareja2
        )
    }
}

@Preview(showBackground = true, widthDp = 640, heightDp = 360)
@Composable
fun JuegoPreview() {
    val navController = rememberNavController()

    AmarracosTheme {
        PantallaJuego(navController)
    }
}


@Preview
@Composable
fun MainScreen() {
    // Mutable state to control the visibility of the dialog
}
