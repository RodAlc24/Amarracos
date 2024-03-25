package com.rodalc.amarracos.ronda

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.rodalc.amarracos.Ganador
import com.rodalc.amarracos.Partida
import com.rodalc.amarracos.Ronda

@Composable
fun MarcadorPuntos(puntos: Int, pareja: String, juegos: Int) {
    Column(
        modifier = Modifier.padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
fun Envite(
    juego: MutableState<Partida>,
    ronda: Ronda,
    cortar: (Ronda) -> Unit,
    dialog: (Boolean) -> Unit
) {
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
                        cortar(
                            when (ronda) {
                                Ronda.GRANDE -> Ronda.CHICA
                                Ronda.CHICA -> Ronda.PARES
                                Ronda.PARES -> Ronda.JUEGO
                                else -> Ronda.MUS
                            }
                        )
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Ver envite")
                }
            }
        }
    }
}

@Composable
fun EnviteNoVisto(
    juego: MutableState<Partida>,
    ronda: Ronda,
    cortar: (Ronda) -> Unit,
    dialog: (Boolean) -> Unit
) {
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
                            Ronda.GRANDE -> {
                                juego.value.rondaActual.grande.envite = number.toInt()
                                juego.value.rondaActual.grande.ganador = Ganador.BUENOS
                                cortar(Ronda.CHICA)
                            }

                            Ronda.CHICA -> {
                                juego.value.rondaActual.chica.envite = number.toInt()
                                juego.value.rondaActual.chica.ganador = Ganador.BUENOS
                                cortar(Ronda.PARES)
                            }

                            Ronda.PARES -> {
                                juego.value.rondaActual.pares.envite = number.toInt()
                                juego.value.rondaActual.pares.ganador = Ganador.BUENOS
                                cortar(Ronda.JUEGO)
                            }

                            else -> {
                                juego.value.rondaActual.juego.envite = number.toInt()
                                juego.value.rondaActual.juego.ganador = Ganador.BUENOS
                                cortar(Ronda.MUS)
                            }
                        }
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(juego.value.nombrePareja1)
                }
                Button(
                    onClick = {
                        dialog(false)
                        when (ronda) {
                            Ronda.GRANDE -> {
                                juego.value.rondaActual.grande.envite = number.toInt()
                                juego.value.rondaActual.grande.ganador = Ganador.MALOS
                                cortar(Ronda.CHICA)
                            }

                            Ronda.CHICA -> {
                                juego.value.rondaActual.chica.envite = number.toInt()
                                juego.value.rondaActual.chica.ganador = Ganador.MALOS
                                cortar(Ronda.PARES)
                            }

                            Ronda.PARES -> {
                                juego.value.rondaActual.pares.envite = number.toInt()
                                juego.value.rondaActual.pares.ganador = Ganador.MALOS
                                cortar(Ronda.JUEGO)
                            }

                            else -> {
                                juego.value.rondaActual.juego.envite = number.toInt()
                                juego.value.rondaActual.juego.ganador = Ganador.MALOS
                                cortar(Ronda.MUS)
                            }
                        }
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
fun Ordago(juego: MutableState<Partida>, cortar: (Ronda) -> Unit, dialog: (Boolean) -> Unit) {
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
                        cortar(Ronda.MUS)
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
                        cortar(Ronda.MUS)
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(juego.value.nombrePareja2)
                }
            }
        }
    }
}
