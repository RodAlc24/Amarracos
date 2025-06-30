package com.rodalc.amarracos.utils

import android.content.Context
import android.widget.Toast

/**
 * A utility object to rate-limit toast messages.
 * This prevents multiple toasts from being shown in rapid succession,
 * ensuring a better user experience.
 */
object ToastRateLimiter {
    private var lastToastTime = 0L

    /**
     * Shows a toast message with a rate limit of 1 second.
     * This prevents multiple toasts from being shown in rapid succession.
     *
     * @param context The context to use.
     * @param message The message to show in the toast.
     * @param msDuration The duration of the toast in milliseconds.
     */
    fun showToast(context: Context, message: String, msDuration: Int = 1000) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastToastTime > msDuration) { // Check if more than 1 second has passed
            lastToastTime = currentTime
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}
