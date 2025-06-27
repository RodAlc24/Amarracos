package com.rodalc.amarracos.comun

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.rodalc.amarracos.main.ToastRateLimiter

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
    )
}

