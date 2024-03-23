package com.rodalc.amarracos.ronda

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rodalc.amarracos.Amarracos
import com.rodalc.amarracos.ui.theme.AmarracosTheme

@Composable
fun Mus(juego: MutableState<Amarracos>) {
    val showDialog = remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { /* Acción del botón */ }) {
            Text("Cortar")
        }
        Button(onClick = { showDialog.value = true }) {
            Text("Perete")
        }
    }

    if (showDialog.value) {
        Dialog(onDismissRequest = { showDialog.value = false }) {
            Box(modifier = Modifier) {
                Surface(
                    modifier = Modifier.align(Alignment.Center),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
                ) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Button(
                            onClick = {
                                showDialog.value = false
                                juego.value =
                                    juego.value.copy(puntosPareja1 = juego.value.puntosPareja1 + 1)
                            },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(juego.value.nombrePareja1)
                        }

                        Button(
                            onClick = {
                                showDialog.value = false
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
    }
}

@Composable
fun PantallaJuego(navController: NavController) {
    var juego = remember { mutableStateOf(Amarracos()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MarcadorPuntos(juego.value.puntosPareja1, juego.value.nombrePareja1)
        Mus(juego)
        MarcadorPuntos(juego.value.puntosPareja2, juego.value.nombrePareja2)
    }
}

@Preview(showBackground = true, widthDp = 640, heightDp = 360)
@Composable
fun JuegoPreview() {
    val navController = rememberNavController()

    AmarracosTheme {
        PantallaJuego(navController)
    }
}


@Preview
@Composable
fun MainScreen() {
    // Mutable state to control the visibility of the dialog
}
