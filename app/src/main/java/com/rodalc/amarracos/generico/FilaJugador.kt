package com.rodalc.amarracos.generico

import android.content.Context
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rodalc.amarracos.main.ToastRateLimiter
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
                    tint = ButtonDefaults.textButtonColors().contentColor
                )
            }
            Box(
                modifier = Modifier.width(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = valorState.toString(),
                )
            }
            IconButton(
                modifier = Modifier.repeatingClickable(
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
                    tint = ButtonDefaults.textButtonColors().contentColor
                )
            }
        }
    }
}


/**
 * Fila para poner los nombres de cada jugador.
 *
 * @param jugador El jugador en cuestión
 * @param numJugadores El número total de jugadores, útil para saber cuál es el último
 * @param context El contexto actual
 */
@Composable
fun FilaJugadorNombres(jugador: Jugador, numJugadores: Int, context: Context) {
    var nombreState by rememberSaveable { mutableStateOf(jugador.nombre) }
    TextField(
        modifier = Modifier.fillMaxWidth(0.9f),
        value = nombreState,
        onValueChange = {
            if (it.length <= 20) {
                nombreState = it
                jugador.nombre = nombreState
            } else ToastRateLimiter.showToast(context, "¡Pon un nombre más corto!")
        },
        maxLines = 1,
        label = { Text("Jugador ${jugador.id}") },
        keyboardOptions = KeyboardOptions(imeAction = if (jugador.id == numJugadores) ImeAction.Done else ImeAction.Next),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        )
    )
}

