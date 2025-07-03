package com.rodalc.amarracos.ui.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodalc.amarracos.ui.theme.AmarracosTheme
import com.rodalc.amarracos.utils.ToastRateLimiter

/**
 * Displays the setup screen for a Mus game.
 *
 * This screen provides options to:
 * - Enter names for the two teams (e.g., "Buenos", "Malos").
 * - Select the target score for the game (30 or 40 points).
 * - Start a new game with the configured settings.
 * - Load a previously saved game (if available).
 *
 * @param modifier The [Modifier] to be applied to the layout.
 * @param canLoad A boolean indicating whether a saved game can be loaded. This controls the enabled state of the "Load saved game" button.
 * @param labelBuenos The default label for the "good" team's name input field.
 * @param labelMalos The default label for the "bad" team's name input field.
 * @param puntos30 A boolean indicating the default selection for the target score (true for 30 points, false for 40 points).
 * @param onStartClick A lambda function called when the "Start new game" button is clicked. It passes the names of the "good" and "bad" teams, and whether the target score is 30 points.
 * @param onLoadClick Lambda function to be invoked when the "Load saved game" button is clicked.
 */
@Composable
fun MusTabScreen(
    modifier: Modifier = Modifier,
    canLoad: Boolean = false,
    labelBuenos: String = "Buenos",
    labelMalos: String = "Malos",
    puntos30: Boolean = true,
    onStartClick: (buenos: String, malos: String, puntos30: Boolean) -> Unit = { _, _, _ -> },
    onLoadClick: () -> Unit = {},
) {
    var nombreBuenos by rememberSaveable { mutableStateOf("") }
    var nombreMalos by rememberSaveable { mutableStateOf("") }
    var puntos30 by rememberSaveable { mutableStateOf(puntos30) }
    val context = LocalContext.current


    AbstractTabScreen(
        modifier = modifier,
        canLoad = canLoad,
        onStartClick = {
            onStartClick(
                if (nombreBuenos != "") nombreBuenos else labelBuenos,
                if (nombreMalos != "") nombreMalos else labelMalos,
                puntos30
            )
        },
        onLoadClick = onLoadClick
    ) {
        OutlinedTextField(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(0.9f),
            value = nombreBuenos,
            onValueChange = {
                if (it.length <= 10) {
                    nombreBuenos = it
                } else ToastRateLimiter.showToast(context, "¡Pon un nombre más corto!")
            },
            maxLines = 1,
            placeholder = { Text(text = labelBuenos) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            value = nombreMalos,
            onValueChange = {
                if (it.length <= 10) {
                    nombreMalos = it
                } else ToastRateLimiter.showToast(context, "¡Pon un nombre más corto!")
            },
            maxLines = 1,
            placeholder = { Text(text = labelMalos) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        )
        Spacer(Modifier.height(20.dp))
        Text(text = "Puntos para ganar:", modifier = Modifier.padding(horizontal = 16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            RadioButton(
                selected = puntos30,
                onClick = { puntos30 = true }
            )
            Text(text = "30", modifier = Modifier.padding(end = 20.dp))
            RadioButton(
                selected = !puntos30,
                onClick = { puntos30 = false }
            )
            Text(text = "40", modifier = Modifier.padding(end = 20.dp))
        }
    }
}

@Preview(showSystemUi = false, showBackground = false)
@Composable
fun PreviewMusTabScreen() {
    AmarracosTheme(darkTheme = true) {
        MusTabScreen()
    }
}