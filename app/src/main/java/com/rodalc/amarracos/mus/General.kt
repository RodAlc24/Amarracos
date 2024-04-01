package com.rodalc.amarracos.mus

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MarcadorPuntos(puntos: Int, pareja: String, juegos: Int) {
    Column {
        Text(text = "$pareja: $juegos")
        Box(
            modifier = Modifier
                .size(150.dp)
                .background(Color.LightGray)
        ) {
            Text(
                text = puntos.toString(),
                modifier = Modifier.align(Alignment.Center),
                fontSize = 50.sp
            )
        }
    }
}

@Composable
fun Ganan(
    juego: MutableState<Partida>,
    ronda: MutableState<Ronda>,
    ganador: Ganador
) {
    val nombre =
        if (ganador == Ganador.BUENOS) juego.value.nombrePareja1 else juego.value.nombrePareja2
    Box(modifier = Modifier) {
        Surface(
            modifier = Modifier.align(Alignment.Center),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Gana: $nombre")
                Button(
                    onClick = {
                        ronda.value = Ronda.GRANDE
                        juego.value.reiniciar()
                        juego.value =
                            if (ganador == Ganador.BUENOS)
                                juego.value.copy(juegosPareja1 = juego.value.juegosPareja1 + 1)
                            else
                                juego.value.copy(juegosPareja2 = juego.value.juegosPareja2 + 1)
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Aceptar")
                }
            }
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

@Composable
fun Conteo(juego: MutableState<Partida>, salir: (Ronda) -> Unit) {
    var ronda by remember { mutableStateOf(Ronda.GRANDE) }
    if (juego.value.rondaActual.getGanador(ronda) != Ganador.POR_VER && ronda == Ronda.GRANDE) ronda =
        Ronda.CHICA
    if (juego.value.rondaActual.getGanador(ronda) != Ganador.POR_VER && ronda == Ronda.CHICA) ronda =
        Ronda.PARES

    Envites(juego, ronda)
    val botones = remember { mutableStateOf(false) }
    when (ronda) {
        Ronda.GRANDE -> {
            Button(onClick = {
                juego.value =
                    juego.value.copy(puntosPareja1 = juego.value.puntosPareja1 + juego.value.rondaActual.grande.envite)
                ronda = Ronda.CHICA
            }) {
                Text(text = "Buenos")
            }

            Button(onClick = {
                juego.value =
                    juego.value.copy(puntosPareja2 = juego.value.puntosPareja2 + juego.value.rondaActual.grande.envite)
                ronda = Ronda.CHICA
            }) {
                Text(text = "Malos")
            }
        }

        Ronda.CHICA -> {
            Button(onClick = {
                juego.value =
                    juego.value.copy(puntosPareja1 = juego.value.puntosPareja1 + juego.value.rondaActual.chica.envite)
                ronda = Ronda.PARES
            }) {
                Text(text = "Buenos")
            }
            Button(onClick = {
                juego.value =
                    juego.value.copy(puntosPareja2 = juego.value.puntosPareja2 + juego.value.rondaActual.chica.envite)
                ronda = Ronda.PARES
            }) {
                Text(text = "Malos")
            }
        }

        Ronda.PARES -> {
            botones.value = juego.value.rondaActual.pares.ganador == Ganador.POR_VER

            if (botones.value) {
                Button(onClick = {
                    juego.value.rondaActual.pares.ganador = Ganador.BUENOS
                    botones.value = false
                }) {
                    Text(text = "Buenos")
                }
                Button(onClick = {
                    juego.value.rondaActual.pares.ganador = Ganador.MALOS
                    botones.value = false
                }) {
                    Text(text = "Malos")
                }
            } else {
                Button(onClick = {
                    juego.value =
                        if (juego.value.rondaActual.getGanador(ronda) == Ganador.BUENOS) {
                            juego.value.copy(puntosPareja1 = juego.value.puntosPareja1 + 1)
                        } else {
                            juego.value.copy(puntosPareja2 = juego.value.puntosPareja2 + 1)
                        }
                    ronda = Ronda.JUEGO

                }) {
                    Text(text = "pares")
                }
            }
        }

        Ronda.JUEGO -> {
            Button(onClick = {
                juego.value =
                    juego.value.copy(puntosPareja1 = juego.value.puntosPareja1 + juego.value.rondaActual.juego.envite)
                ronda = Ronda.CONTEO
            }) {
                Text(text = "Buenos")
            }
            Button(onClick = {
                juego.value =
                    juego.value.copy(puntosPareja2 = juego.value.puntosPareja2 + juego.value.rondaActual.juego.envite)
                ronda = Ronda.CONTEO
            }) {
                Text(text = "Malos")
            }
        }

        else -> {
            juego.value.rondaActual.reiniciar()
            salir(Ronda.GRANDE)
        }
    }
}