package com.rodalc.amarracos.main

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Un objeto que limita la frecuencia de mostrar toasts en la aplicación.
 *
 * Este objeto asegura que los toasts no se muestren con demasiada frecuencia, limitando la frecuencia a un toast por segundo.
 */
object ToastRateLimiter {
    private var lastToastTime = 0L

    /**
     * Muestra un toast con un mensaje específico si ha pasado más de un segundo desde el último toast.
     *
     * @param context El contexto de la aplicación o actividad.
     * @param message El mensaje a mostrar en el toast.
     */
    fun showToast(context: Context, message: String) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastToastTime > 1000) { // Check if more than 1 second has passed
            lastToastTime = currentTime
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}

/**
 * Pop-Up con dos opciones.
 *
 * @param title El título del pop-Up
 * @param optionA El texto de la primera opción
 * @param optionB El texto de la segunda opción
 * @param onClickA La aacción del primer botón
 * @param onClickB La acción del segundo botón
 * @param preferredOptionB Si el segundo botón se debe resaltar o no
 * @param onDismiss Acción al hacer click fuera del pop-Up
 */
@Composable
fun PopUp(
    title: String,
    optionA: String,
    optionB: String,
    onClickA: () -> Unit,
    onClickB: () -> Unit,
    preferredOptionB: Boolean = false,
    onDismiss: () -> Unit = {}
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Box(modifier = Modifier) {
            Surface(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(10.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = title)
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        OutlinedButton(onClick = { onClickA() }) {
                            Text(
                                text = optionA,
                                maxLines = 1
                            )
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        if (preferredOptionB) {
                            Button(onClick = { onClickB() }) { Text(text = optionB, maxLines = 1) }
                        } else {
                            OutlinedButton(onClick = { onClickB() }) {
                                Text(
                                    text = optionB,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun Modifier.repeatingClickable(
    interactionSource: InteractionSource,
    enabled: Boolean = true,
    maxDelayMillis: Long = 500,
    minDelayMillis: Long = 130,
    delayDecayFactor: Float = .20f,
    onClick: () -> Unit
): Modifier = composed {
    val currentClickListener by rememberUpdatedState(onClick)
    val isEnabled by rememberUpdatedState(enabled)

    pointerInput(interactionSource, isEnabled) {
        coroutineScope {
            awaitEachGesture {
                val down = awaitFirstDown(requireUnconsumed = false)
                val job = launch {
                    var currentDelayMillis = maxDelayMillis
                    while (isEnabled && down.pressed) {
                        delay(currentDelayMillis)
                        currentClickListener()
                        val nextMillis =
                            currentDelayMillis - (currentDelayMillis * delayDecayFactor)
                        currentDelayMillis = nextMillis.toLong().coerceAtLeast(minDelayMillis)
                    }
                }
                waitForUpOrCancellation()
                job.cancel()
            }
        }
    }
}

/**
 * Pop-Up para introducir valores numéricos.
 *
 * @param title El título del pop-Up
 * @param value El valor actual
 * @param minValue El valor mínimo
 * @param maxValue El valor máximo
 * @param onValueChange La acción a realizar cuando se cambie el valor
 * @param onDismiss Acción al hacer click fuera del pop-Up
 */
@Composable
fun NumberInput(
    title: String,
    value: Int,
    minValue: Int = -9999,
    maxValue: Int = 9999,
    onValueChange: (Int) -> Unit,
    onDismiss: () -> Unit = {}
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        var valueText by rememberSaveable { mutableStateOf(value.toString())}
        if (valueText == "0") valueText = ""

        Box(modifier = Modifier) {
            Surface(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(10.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = title)
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        TextField(
                            modifier = Modifier.fillMaxWidth(0.7f),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Number
                            ),
                            value = valueText,
                            onValueChange = {
                                valueText = it
                            },
                            maxLines = 1,
                            placeholder = { Text(text = title) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            )
                        )
                    }
                    val newValue = valueText.toIntOrNull()
                    val valid = newValue != null && newValue >= minValue && newValue <= maxValue
                    Button(
                        onClick = {
                            onValueChange(newValue ?: value)
                            onDismiss()
                        },
                        enabled = valid
                    ) {
                        Text(text = "Aceptar")
                    }
                }
            }
        }
    }
}
