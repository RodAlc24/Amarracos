package com.rodalc.amarracos.ronda

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.rodalc.amarracos.Ronda

@Composable
fun MarcadorPuntos(puntos: Int, pareja: String, juegos: Int) {
    Column(
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

@Composable
fun Ordago(juego: MutableState<Partida>, ronda: MutableState<Ronda>, dialog: (Boolean) -> Unit) {
    Box(modifier = Modifier) {
        Surface(
            modifier = Modifier.align(Alignment.Center),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = {
                        ronda.value = Ronda.GRANDE
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
                        ronda.value = Ronda.GRANDE
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

@Composable
fun Envites(juego: MutableState<Partida>, ronda: Ronda) {
    Column {
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            CajaEnvite(juego.value.rondaActual.grande.envite, ronda == Ronda.GRANDE)
            Spacer(Modifier.width(10.dp))
            CajaEnvite(juego.value.rondaActual.chica.envite, ronda == Ronda.CHICA)
        }
        Spacer(Modifier.height(10.dp))
        Row {
            CajaEnvite(juego.value.rondaActual.pares.envite, ronda == Ronda.PARES)
            Spacer(Modifier.width(10.dp))
            CajaEnvite(juego.value.rondaActual.juego.envite, ronda == Ronda.JUEGO)
        }
    }
}

@Composable
fun CajaEnvite(value: Int, focus: Boolean) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(if (focus) Color.LightGray else Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value.toString()
        )
    }

}

