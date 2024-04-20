package com.rodalc.amarracos.pocha

import android.content.Context
import android.widget.Toast

object ToastRateLimiter {
    private var lastToastTime = 0L

    fun showToast(context: Context, message: String) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastToastTime > 1000) { // Check if more than 1 second has passed
            lastToastTime = currentTime
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}