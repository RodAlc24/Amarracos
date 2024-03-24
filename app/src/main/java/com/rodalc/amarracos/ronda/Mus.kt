package com.rodalc.amarracos.ronda

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rodalc.amarracos.Partida
import com.rodalc.amarracos.Ronda

@Composable
fun Mus(juego: MutableState<Partida>, cortar: (Ronda) -> Unit) {
    val showDialog = remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { cortar(Ronda.GRANDE) }) {
            Text("Cortar")
        }
        Button(onClick = { showDialog.value = true }) {
            Text("Perete")
        }
    }

    if (showDialog.value) {
        Dialog(onDismissRequest = { showDialog.value = false }) {
            Perete(juego) { showDialog.value = false }
        }
    }

}

@Composable
fun Perete(juego: MutableState<Partida>, dialog: (Boolean) -> Unit) {
    Box(modifier = Modifier) {
        Surface(
            modifier = Modifier.align(Alignment.Center),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = {
                        dialog(false)
                        juego.value =
                            juego.value.copy(puntosPareja1 = juego.value.puntosPareja1 + 1)
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(juego.value.nombrePareja1)
                }

                Button(
                    onClick = {
                        dialog(false)
                        juego.value =
                            juego.value.copy(puntosPareja2 = juego.value.puntosPareja2 + 1)
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(juego.value.nombrePareja2)
                }
            }
        }
    }
}
