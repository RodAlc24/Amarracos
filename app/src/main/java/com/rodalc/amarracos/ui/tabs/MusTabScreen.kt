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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rodalc.amarracos.R
import com.rodalc.amarracos.data.tabs.TabViewModel
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
 * @param onStartClick A lambda function called when the "Start new game" button is clicked. It passes the names of the "good" and "bad" teams, and whether the target score is 30 points.
 * @param onLoadClick Lambda function to be invoked when the "Load saved game" button is clicked.
 */
@Composable
fun MusTabScreen(
    modifier: Modifier = Modifier,
    canLoad: Boolean = false,
    tabViewmodel: TabViewModel = viewModel(),
    labelBuenos: String = stringResource(R.string.default_buenos),
    labelMalos: String = stringResource(R.string.default_malos),
    onStartClick: (buenos: String, malos: String, puntos30: Boolean) -> Unit = { _, _, _ -> },
    onLoadClick: () -> Unit = {},
) {
    val uiState = tabViewmodel.uiState.collectAsState()
    val context = LocalContext.current
    val nameTooLong = stringResource(R.string.toast_name_too_long)


    AbstractTabScreen(
        modifier = modifier,
        canLoad = canLoad,
        onStartClick = {
            onStartClick(
                if (uiState.value.nombreBuenos == "") labelBuenos else uiState.value.nombreBuenos,
                if (uiState.value.nombreMalos == "") labelMalos else uiState.value.nombreMalos,
                uiState.value.puntos30
            )
        },
        onLoadClick = onLoadClick
    ) {
        OutlinedTextField(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(0.9f),
            value = uiState.value.nombreBuenos,
            onValueChange = {
                if (it.length <= 10) {
                    tabViewmodel.setNombreBuenos(it)
                } else ToastRateLimiter.showToast(context, nameTooLong)
            },
            maxLines = 1,
            placeholder = { Text(text = labelBuenos) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            value = uiState.value.nombreMalos,
            onValueChange = {
                if (it.length <= 10) {
                    tabViewmodel.setNombreMalos(it)
                } else ToastRateLimiter.showToast(context, nameTooLong)
            },
            maxLines = 1,
            placeholder = { Text(text = labelMalos) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.text_points_to_win),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            RadioButton(
                selected = uiState.value.puntos30,
                onClick = { tabViewmodel.setPuntos30(true) }
            )
            Text(text = "30", modifier = Modifier.padding(end = 20.dp))
            RadioButton(
                selected = !uiState.value.puntos30,
                onClick = { tabViewmodel.setPuntos30(false) }
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