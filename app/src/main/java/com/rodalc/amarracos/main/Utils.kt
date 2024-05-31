package com.rodalc.amarracos.main

import android.content.Context
import android.widget.Toast

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