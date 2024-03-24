package com.rodalc.amarracos.ronda

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rodalc.amarracos.Partida
import com.rodalc.amarracos.Ronda

@Composable
fun GrandeChica(ronda: Ronda, juego: MutableState<Partida>, cortar: (Ronda) -> Unit) {
    val ordago = remember { mutableStateOf(false) }
    val envite = remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { envite.value = true }) {
            Text("Envidar")
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
        Dialog(onDismissRequest = { ordago.value = false }) {
            Ordago(juego) { ordago.value = false }
        }
    }

    if (envite.value) {
        Dialog(onDismissRequest = { envite.value = false }) {
            Envite(juego, ronda) { envite.value = false }
        }
    }

}

@Composable
fun Envite(juego: MutableState<Partida>, ronda: Ronda, dialog: (Boolean) -> Unit) {
    Box(modifier = Modifier) {
        Surface(
            modifier = Modifier.align(Alignment.Center),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                var number by remember {
                    mutableStateOf(
                        when (ronda) {
                            Ronda.GRANDE -> juego.value.rondaActual.grande.envite.toString()
                            Ronda.CHICA -> juego.value.rondaActual.chica.envite.toString()
                            Ronda.PARES -> juego.value.rondaActual.pares.envite.toString()
                            else -> juego.value.rondaActual.juego.envite.toString()
                        }
                    )
                }
                TextField(
                    value = number,
                    onValueChange = { number = it },
                    label = { Text("Enter a number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        dialog(false)
                        when (ronda) {
                            Ronda.GRANDE -> juego.value.rondaActual.grande.envite = number.toInt()
                            Ronda.CHICA -> juego.value.rondaActual.chica.envite = number.toInt()
                            Ronda.PARES -> juego.value.rondaActual.pares.envite = number.toInt()
                            else -> juego.value.rondaActual.juego.envite = number.toInt()
                        }
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Ver envite")
                }
            }
        }
    }
}
