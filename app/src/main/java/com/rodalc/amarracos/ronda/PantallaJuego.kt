package com.rodalc.amarracos.ronda

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.rodalc.amarracos.Partida
import com.rodalc.amarracos.Ronda


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
                Ronda.GRANDE -> BotonesJuego(Ronda.GRANDE, juego) { ronda.value = it }
                Ronda.CHICA -> BotonesJuego(Ronda.CHICA, juego) { ronda.value = it }
                Ronda.PARES -> BotonesJuego(Ronda.PARES, juego) { ronda.value = it }
                else -> BotonesJuego(Ronda.JUEGO, juego) { ronda.value = it }
            }
        }

        MarcadorPuntos(
            juego.value.puntosPareja2,
            juego.value.nombrePareja2,
            juego.value.juegosPareja2
        )
    }
}

@Composable
fun BotonesJuego(ronda: Ronda, juego: MutableState<Partida>, cortar: (Ronda) -> Unit) {
    val ordago = remember { mutableStateOf(false) }
    val envite = remember { mutableStateOf(false) }
    val enviteNoVisto = remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { envite.value = true }) {
            Text("Envidar")
        }
        Button(onClick = { enviteNoVisto.value = true }) {
            Text("Envite no visto")
        }
        Button(onClick = {
            when (ronda) {
                Ronda.GRANDE -> {
                    juego.value.rondaActual.grande.envite = 1
                    cortar(Ronda.CHICA)
                }

                Ronda.CHICA -> {
                    juego.value.rondaActual.chica.envite = 1
                    cortar(Ronda.PARES)
                }

                Ronda.PARES -> {
                    cortar(Ronda.JUEGO)
                }

                else -> {
                    cortar(Ronda.MUS)
                }
            }
        }) {
            if (ronda == Ronda.GRANDE || ronda == Ronda.CHICA) {
                Text("Al paso")

            } else {
                Text("Pasar")
            }
        }
        Button(onClick = { ordago.value = true }) {
            Text("Órdago")
        }
    }

    if (ordago.value) {
        Dialog(onDismissRequest = {
            ordago.value = false
        }) {
            Ordago(juego, cortar) { ordago.value = false }
        }
    }

    if (envite.value) {
        Dialog(onDismissRequest = { envite.value = false }) {
            Envite(juego, ronda, cortar) { envite.value = false }
        }
    }

    if (enviteNoVisto.value) {
        Dialog(onDismissRequest = { enviteNoVisto.value = false }) {
            EnviteNoVisto(juego, ronda, cortar) { enviteNoVisto.value = false }
        }
    }

}

