package com.rodalc.amarracos.mus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.window.Dialog

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
    var dialogPares by remember { mutableStateOf(false) }
    var dialogJuego by remember { mutableStateOf(false) }
    var ronda by remember { mutableStateOf(Ronda.GRANDE) }
    var botones by remember { mutableStateOf(true) }

    if (juego.value.rondaActual.getGanador(ronda) != Ganador.POR_VER && ronda == Ronda.GRANDE) ronda =
        Ronda.CHICA
    if (juego.value.rondaActual.getGanador(ronda) != Ganador.POR_VER && ronda == Ronda.CHICA) ronda =
        Ronda.PARES

    Envites(juego, ronda)
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
            botones = juego.value.rondaActual.pares.ganador == Ganador.POR_VER
            dialogPares = !botones

            if (botones) {
                Button(onClick = {
                    juego.value.rondaActual.pares.ganador = Ganador.BUENOS
                    botones = false
                }) {
                    Text(text = "Buenos")
                }
                Button(onClick = {
                    juego.value.rondaActual.pares.ganador = Ganador.MALOS
                    botones = false
                }) {
                    Text(text = "Malos")
                }
                Button(onClick = {
                    ronda = Ronda.JUEGO
                }) {
                    Text(text = "Nadie")
                }
            }
        }

        Ronda.JUEGO -> {
            botones = juego.value.rondaActual.juego.ganador == Ganador.POR_VER
            dialogJuego = !botones

            if (botones) {
                Button(onClick = {
                    juego.value.rondaActual.juego.ganador = Ganador.BUENOS
                    botones = false
                }) {
                    Text(text = "Buenos")
                }
                Button(onClick = {
                    juego.value.rondaActual.juego.ganador = Ganador.MALOS
                    botones = false
                }) {
                    Text(text = "Malos")
                }
            }
        }

        else -> {
            juego.value.rondaActual.reiniciar()
            salir(Ronda.GRANDE)
        }
    }
    if (dialogPares) {
        Dialog(onDismissRequest = {}) {
            PuntosPares(juego) {
                dialogPares = false
                ronda = Ronda.JUEGO
            }
        }
    }
    if (dialogJuego) {
        Dialog(onDismissRequest = {}) {
            PuntosJuego(juego) {
                dialogPares = false
                ronda = Ronda.CONTEO
            }
        }
    }
}

@Composable
fun PuntosPares(juego: MutableState<Partida>, dialog: (Boolean) -> Unit) {
    var pares1: Pares by remember { mutableStateOf(Pares.NADA) }
    var pares2: Pares by remember { mutableStateOf(Pares.NADA) }

    Box(modifier = Modifier) {
        Surface(
            modifier = Modifier.align(Alignment.Center),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Pares")
                        RadioButton(
                            selected = pares1 == Pares.PAR,
                            onClick = { pares1 = Pares.PAR })
                        RadioButton(
                            selected = pares2 == Pares.PAR,
                            onClick = { pares2 = Pares.PAR })
                    }
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Medias")
                        RadioButton(
                            selected = pares1 == Pares.MEDIAS,
                            onClick = { pares1 = Pares.MEDIAS })
                        RadioButton(
                            selected = pares2 == Pares.MEDIAS,
                            onClick = { pares2 = Pares.MEDIAS })
                    }
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Duples")
                        RadioButton(
                            selected = pares1 == Pares.DUPLES,
                            onClick = { pares1 = Pares.DUPLES })
                        RadioButton(
                            selected = pares2 == Pares.DUPLES,
                            onClick = { pares2 = Pares.DUPLES })
                    }
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Nada")
                        RadioButton(
                            selected = pares1 == Pares.NADA,
                            onClick = { pares1 = Pares.NADA })
                        RadioButton(
                            selected = pares2 == Pares.NADA,
                            onClick = { pares2 = Pares.NADA })
                    }
                }
                Button(
                    enabled = pares1 != Pares.NADA || pares2 != Pares.NADA,
                    onClick = {
                        var totales = juego.value.rondaActual.pares.envite
                        totales += when (pares1) {
                            Pares.PAR -> 1
                            Pares.MEDIAS -> 2
                            Pares.DUPLES -> 3
                            else -> 0
                        }
                        totales += when (pares2) {
                            Pares.PAR -> 1
                            Pares.MEDIAS -> 2
                            Pares.DUPLES -> 3
                            else -> 0
                        }
                        if (juego.value.rondaActual.pares.ganador == Ganador.BUENOS) {
                            juego.value =
                                juego.value.copy(puntosPareja1 = juego.value.puntosPareja1 + totales)
                        } else {
                            juego.value =
                                juego.value.copy(puntosPareja2 = juego.value.puntosPareja2 + totales)
                        }
                        dialog(false)
                    }) {
                    Text(text = "Ok")
                }
            }
        }
    }
}

@Composable
fun PuntosJuego(juego: MutableState<Partida>, dialog: (Boolean) -> Unit) {
    var juego1: Juego by remember { mutableStateOf(Juego.NADA) }
    var juego2: Juego by remember { mutableStateOf(Juego.NADA) }
    var punto: Boolean by remember { mutableStateOf(false) }

    Box(modifier = Modifier) {
        Surface(
            modifier = Modifier.align(Alignment.Center),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Juego")
                        RadioButton(
                            enabled = !punto,
                            selected = juego1 == Juego.JUEGO,
                            onClick = { juego1 = Juego.JUEGO })
                        RadioButton(
                            enabled = !punto,
                            selected = juego2 == Juego.JUEGO,
                            onClick = { juego2 = Juego.JUEGO })
                    }
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "31")
                        RadioButton(
                            enabled = !punto,
                            selected = juego1 == Juego.LA_UNA,
                            onClick = { juego1 = Juego.LA_UNA })
                        RadioButton(
                            enabled = !punto,
                            selected = juego2 == Juego.LA_UNA,
                            onClick = { juego2 = Juego.LA_UNA })
                    }
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Nada")
                        RadioButton(
                            enabled = !punto,
                            selected = juego1 == Juego.NADA,
                            onClick = { juego1 = Juego.NADA })
                        RadioButton(
                            enabled = !punto,
                            selected = juego2 == Juego.NADA,
                            onClick = { juego2 = Juego.NADA })
                    }
                }

                Checkbox(checked = punto, onCheckedChange = { punto = !punto })

                Button(
                    enabled = punto || juego1 != Juego.NADA || juego2 != Juego.NADA,
                    onClick = {
                        var totales = juego.value.rondaActual.juego.envite
                        totales += when (juego1) {
                            Juego.JUEGO -> 2
                            Juego.LA_UNA -> 3
                            else -> 0
                        }
                        totales += when (juego2) {
                            Juego.JUEGO -> 2
                            Juego.LA_UNA -> 3
                            else -> 0
                        }

                        totales = if (punto) 1 else totales

                        if (juego.value.rondaActual.juego.ganador == Ganador.BUENOS) {
                            juego.value =
                                juego.value.copy(puntosPareja1 = juego.value.puntosPareja1 + totales)
                        } else {
                            juego.value =
                                juego.value.copy(puntosPareja2 = juego.value.puntosPareja2 + totales)
                        }
                        dialog(false)
                    }) {
                    Text(text = "Ok")
                }
            }
        }
    }
}
