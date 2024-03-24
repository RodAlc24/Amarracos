package com.rodalc.amarracos.ronda

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rodalc.amarracos.Partida

@Composable
fun Ordago(juego: MutableState<Partida>, dialog: (Boolean) -> Unit) {
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
                        juego.value.reiniciar()
                        juego.value =
                            juego.value.copy(juegosPareja1 = juego.value.juegosPareja1 + 1)
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(juego.value.nombrePareja1)
                }

                Button(
                    onClick = {
                        dialog(false)
                        juego.value.reiniciar()
                        juego.value =
                            juego.value.copy(juegosPareja2 = juego.value.juegosPareja2 + 1)
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(juego.value.nombrePareja2)
                }
            }
        }
    }
}
