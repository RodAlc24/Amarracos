package com.rodalc.amarracos.ronda

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Dialog
import com.rodalc.amarracos.Partida
import com.rodalc.amarracos.Ronda

@Composable
fun GrandeChica(ronda: Ronda, juego: MutableState<Partida>, cortar: (Ronda) -> Unit) {
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
            if (ronda == Ronda.GRANDE) {
                juego.value.rondaActual.grande.envite = 1
                cortar(Ronda.CHICA)
            } else {
                juego.value.rondaActual.chica.envite = 1
                cortar(Ronda.PARES)
            }
        }) {
            Text("Al paso")
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

