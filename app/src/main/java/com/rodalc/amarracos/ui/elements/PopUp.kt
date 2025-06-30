package com.rodalc.amarracos.ui.elements

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rodalc.amarracos.ui.theme.AmarracosTheme

/**
 * Composable function that displays a dialog with a title and two options.
 *
 * @param title The title of the dialog.
 * @param optionA The text for the first option button.
 * @param optionB The text for the second option button.
 * @param onClickA The callback function to be executed when the first option button is clicked.
 * @param onClickB The callback function to be executed when the second option button is clicked.
 * @param modifier The modifier for the dialog's layout.
 * @param preferredOptionB A boolean indicating whether the second option should be styled as the preferred option (e.g., using a filled button). Defaults to false.
 * @param onDismiss The callback function to be executed when the dialog is dismissed (e.g., by clicking outside the dialog or pressing the back button). Defaults to an empty lambda.
 */
@Composable
fun PopUp(
    title: String,
    optionA: String,
    optionB: String,
    onClickA: () -> Unit,
    onClickB: () -> Unit,
    modifier: Modifier = Modifier,
    preferredOptionB: Boolean = false,
    onDismiss: () -> Unit = {}
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Box(modifier = modifier) {
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

@Preview
@Composable
fun PreviewPopUp() {
    AmarracosTheme {
        PopUp(
            title = "Título",
            optionA = "Opción A",
            optionB = "Opción B",
            onClickA = {},
            onClickB = {},
        )
    }
}

@Preview
@Composable
fun PreviewPopUp_preferredOptionB() {
    AmarracosTheme {
        PopUp(
            title = "Título",
            optionA = "Opción A",
            optionB = "Opción B",
            preferredOptionB = true,
            onClickA = {},
            onClickB = {},
        )
    }
}
