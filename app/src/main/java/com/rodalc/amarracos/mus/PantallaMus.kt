package com.rodalc.amarracos.mus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * Gestiona la pantalla del mus.
 */
@Preview(
    showBackground = true,
    device = "spec:parent=pixel,orientation=landscape", backgroundColor = 0xFFFFFFFF
)
@Composable
fun PantallaMus() {
    val juego = remember { mutableStateOf(Partida()) }
    val ronda = remember { mutableStateOf(Ronda.GRANDE) }
    val ordago = remember { mutableStateOf(false) }
    val botones = remember { mutableStateOf(true) }
    val envite = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.padding(40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.fillMaxHeight(0.15f))
            MarcadorPuntos(
                juego.value.puntosPareja1,
                juego.value.nombrePareja1,
                juego.value.juegosPareja1
            )
            Spacer(Modifier.fillMaxHeight(0.1f))
            if (botones.value) BotonesEnviteNoVisto(
                juego,
                ronda.value,
                Ganador.BUENOS,
                { botones.value = it },
                { envite.value = it })
        }

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.fillMaxHeight(.15f))
            if (ronda.value == Ronda.CONTEO) Conteo(juego) {
                ronda.value = it
                botones.value = true
            } else {
                Envites(juego, ronda.value)
            }
            Spacer(Modifier.fillMaxHeight(.15f))

            if (botones.value) BototesJuego(
                juego,
                ronda,
                { ordago.value = it },
                { botones.value = it },
                { envite.value = it })
            if (envite.value) Envite(juego, ronda, { botones.value = it }, { envite.value = it })

            Spacer(Modifier.fillMaxHeight(.15f))
        }

        Column(
            modifier = Modifier.padding(40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.fillMaxHeight(0.15f))
            MarcadorPuntos(
                juego.value.puntosPareja2,
                juego.value.nombrePareja2,
                juego.value.juegosPareja2
            )
            Spacer(Modifier.fillMaxHeight(0.1f))
            if (botones.value) BotonesEnviteNoVisto(
                juego,
                ronda.value,
                Ganador.MALOS,
                { botones.value = it },
                { envite.value = it })
        }
    }

    if (ronda.value == Ronda.CONTEO) botones.value = false

    if (ordago.value) {
        Dialog(onDismissRequest = {
            ordago.value = false
        }) {
            Ordago(juego, ronda) { ordago.value = false }
        }
    }

    if (juego.value.puntosPareja1 >= 30) {
        Dialog(onDismissRequest = {}) {
            Ganan(juego, ronda, Ganador.BUENOS)
        }
    }

    if (juego.value.puntosPareja2 >= 30) {
        Dialog(onDismissRequest = {}) {
            Ganan(juego, ronda, Ganador.MALOS)
        }
    }
}

/**
 * Se encarga de mostrar los botones de las distintas rondas.
 */
@Composable
fun BototesJuego(
    juego: MutableState<Partida>,
    ronda: MutableState<Ronda>,
    ordago: (Boolean) -> Unit,
    botones: (Boolean) -> Unit,
    envite: (Boolean) -> Unit
) {
    Button(onClick = {
        botones(false)
        envite(true)
    }) {
        Text("Envite")
    }

    val alPaso = if (ronda.value == Ronda.GRANDE || ronda.value == Ronda.CHICA) 1 else 0

    Button(onClick = {
        juego.value.rondaActual.setEnvite(ronda.value, alPaso)
        when (ronda.value) {
            Ronda.GRANDE -> ronda.value = Ronda.CHICA
            Ronda.CHICA -> ronda.value = Ronda.PARES
            Ronda.PARES -> ronda.value = Ronda.JUEGO
            Ronda.JUEGO -> ronda.value = Ronda.CONTEO
            else -> ronda.value = Ronda.GRANDE
        }
    }) {
        Text("Al paso")
    }
    Button(onClick = { ordago(true) }) {
        Text("Órdago")
    }
}

/**
 * Muestra los botones del envite no visto.
 */
@Composable
fun BotonesEnviteNoVisto(
    juego: MutableState<Partida>,
    ronda: Ronda,
    ganador: Ganador,
    botones: (Boolean) -> Unit,
    envite: (Boolean) -> Unit
) {
    Button(onClick = {
        juego.value.rondaActual.setGanador(ronda, ganador)
        botones(false)
        envite(true)
    }) {
        Text(text = "Envite no visto")
    }
}

/**
 * Gestiona los envites.
 */
@Composable
fun Envite(
    juego: MutableState<Partida>,
    ronda: MutableState<Ronda>,
    botones: (Boolean) -> Unit,
    envite: (Boolean) -> Unit
) {
    val piedras =
        remember { mutableIntStateOf(if (juego.value.rondaActual.getGanador(ronda.value) == Ganador.POR_VER) 2 else 1) }

    Row {
        Button(onClick = { piedras.intValue -= 1 }) {
            Text(text = "-1")
        }
        Button(onClick = { piedras.intValue += 1 }) {
            Text(text = "+1")
        }
        Button(onClick = { piedras.intValue += 5 }) {
            Text(text = "+5")
        }
    }
    Button(onClick = {
        when (juego.value.rondaActual.getGanador(ronda.value)) {
            Ganador.POR_VER -> juego.value.rondaActual.setEnvite(
                ronda.value,
                piedras.intValue
            )

            Ganador.BUENOS -> juego.value.puntosPareja1 += piedras.intValue
            Ganador.MALOS -> juego.value.puntosPareja2 += piedras.intValue
        }
        botones(true)
        envite(false)
        ronda.value = when (ronda.value) {
            Ronda.GRANDE -> Ronda.CHICA
            Ronda.CHICA -> Ronda.PARES
            Ronda.PARES -> Ronda.JUEGO
            Ronda.JUEGO -> Ronda.CONTEO
            else -> Ronda.GRANDE
        }
    }) {
        Text(text = "Se ven " + piedras.intValue)
    }
}
