package com.rodalc.amarracos.main

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

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
                        OutlinedButton(onClick = { onClickA() }) { Text(text = optionA) }
                        Spacer(modifier = Modifier.width(20.dp))
                        if (preferredOptionB) {
                            Button(onClick = { onClickB() }) { Text(text = optionB) }
                        } else {
                            OutlinedButton(onClick = { onClickB() }) { Text(text = optionB) }
                        }
                    }
                }
            }
        }
    }
}
