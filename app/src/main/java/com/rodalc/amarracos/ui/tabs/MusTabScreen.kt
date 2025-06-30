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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodalc.amarracos.mus.MusDefaultConfigManager
import com.rodalc.amarracos.storage.DataStoreManager
import com.rodalc.amarracos.ui.theme.AmarracosTheme
import com.rodalc.amarracos.utils.ToastRateLimiter
import kotlinx.coroutines.async

/**
 * Composable function for the Mus game setup screen.
 *
 * This screen allows users to configure and start a new Mus game or load a previously saved one.
 * It includes input fields for team names, a selection for the target score (30 or 40 points),
 * and buttons to start a new game or load an existing one.
 *
 * @param modifier Modifier for customizing the layout and appearance of the screen.
 * @param canLoad Boolean indicating whether a saved game is available to load.
 *                Controls the enabled state of the "Load saved game" button.
 * @param onStartClick Lambda function to be invoked when the "Start new game" button is clicked.
 *                     It receives the names of the "good" team, the "bad" team, and the target score.
 *                     Default names are used if the input fields are empty.
 * @param onLoadClick Lambda function to be invoked when the "Load saved game" button is clicked.
 */
@Composable
fun MusTabScreen(
    modifier: Modifier = Modifier,
    canLoad: Boolean = false,
    labelBuenos: String = "Buenos",
    labelMalos: String = "Malos",
    onStartClick: (buenos: String, malos: String, puntos: Int) -> Unit = { _, _, _ -> },
    onLoadClick: () -> Unit = {},
) {
    var nombreBuenos by remember { mutableStateOf("") }
    var nombreMalos by remember { mutableStateOf("") }
    val context = LocalContext.current
    val puntos30 by DataStoreManager.readDataStore(context, DataStoreManager.Key.MUS_A_30)
        .collectAsState(initial = true)
    val coreutineScope = rememberCoroutineScope()
    MusDefaultConfigManager.loadState(context)

    AbstractTabScreen(
        modifier = modifier,
        canLoad = canLoad,
        onStartClick = {
            onStartClick(
                if (nombreBuenos != "") nombreBuenos else labelBuenos,
                if (nombreMalos != "") nombreMalos else labelMalos,
                if (puntos30) 30 else 40
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
                onClick = {
                    coreutineScope.async {
                        DataStoreManager.setDataStore(
                            context,
                            DataStoreManager.Key.MUS_A_30,
                            true
                        )
                    }
                },
            )
            Text(text = "30", modifier = Modifier.padding(end = 20.dp))
            RadioButton(
                selected = !puntos30,
                onClick = {
                    coreutineScope.async {
                        DataStoreManager.setDataStore(
                            context,
                            DataStoreManager.Key.MUS_A_30,
                            false
                        )
                    }
                }
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