package com.rodalc.amarracos.generico

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rodalc.amarracos.comun.Jugador
import com.rodalc.amarracos.comun.Ronda
import com.rodalc.amarracos.main.NumberInput
import com.rodalc.amarracos.main.repeatingClickable

/**
 * Crea una fila con la información de un jugador, además de unos botones de - y + para aumentar y disminuir el valor correspondiente.
 *
 * @param jugador El jugador que se va a emplear
 * @param ronda La ronda actual
 */
@Composable
fun FilaJugador(
    jugador: Jugador,
    ronda: Ronda
) {
    var valorState by rememberSaveable { mutableIntStateOf(jugador.incremento) }
    val interactionSource = remember { MutableInteractionSource() }
    var showPopUp by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(50.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = jugador.toString(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f) // Allocate space for the text
                .clipToBounds()
        )

        if (ronda == Ronda.JUEGO) {
            Text(
                text = jugador.puntos.toString(),
                modifier = Modifier.padding(10.dp)
            )
        } else if (ronda == Ronda.CONTEO) {
            IconButton(
                modifier = Modifier.repeatingClickable(
                    maxDelayMillis = 500,
                    minDelayMillis = 10,
                    delayDecayFactor = .10f,
                    interactionSource = interactionSource,
                    onClick = {
                        valorState -= 1
                        jugador.incremento = valorState
                    },
                ),
                onClick = {
                    valorState -= 1
                    jugador.incremento = valorState
                },
            ) {
                Icon(
                    Icons.Rounded.Remove,
                    contentDescription = "Quitar 1 a $jugador",
                )
            }
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .clickable(onClick = {
                        showPopUp = true
                    }),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = valorState.toString(),
                )
            }
            IconButton(
                modifier = Modifier.repeatingClickable(
                    maxDelayMillis = 500,
                    minDelayMillis = 10,
                    delayDecayFactor = .10f,
                    interactionSource = interactionSource,
                    onClick = {
                        valorState += 1
                        jugador.incremento = valorState
                    },
                ),
                onClick = {
                    valorState += 1
                    jugador.incremento = valorState
                },

                ) {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = "Añadir uno a $jugador",
                )
            }
        }
    }

    if (showPopUp) {
        NumberInput(
            title = "Puntuación",
            value = valorState,
            onValueChange = {
                valorState = it
                jugador.incremento = valorState
            },
            onDismiss = { showPopUp = false },
        )
    }
}
