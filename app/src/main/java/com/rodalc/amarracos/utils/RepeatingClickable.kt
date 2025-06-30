package com.rodalc.amarracos.utils

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A [Modifier] that makes a component clickable and repeatedly calls the [onClick] lambda while
 * the component is pressed.
 *
 * @param interactionSource [InteractionSource] that will be used to dispatch press events.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable.
 * @param maxDelayMillis The maximum delay between [onClick] calls in milliseconds.
 * @param minDelayMillis The minimum delay between [onClick] calls in milliseconds.
 * @param delayDecayFactor The factor by which the delay is reduced after each [onClick] call.
 * @param onClick The lambda to be executed when the component is clicked and held.
 */
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
