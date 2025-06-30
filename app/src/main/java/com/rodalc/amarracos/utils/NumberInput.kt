package com.rodalc.amarracos.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rodalc.amarracos.ui.theme.AmarracosTheme

/**
 * Pop-up dialog for numeric input.
 *
 * This composable function displays a dialog with a text field for entering numeric values.
 * It includes validation to ensure the input is within the specified minimum and maximum values.
 *
 * @param title The title of the pop-up dialog.
 * @param value The current value to be displayed and edited.
 * @param modifier The modifier for the dialog's layout.
 * @param minValue The minimum allowable value (inclusive). Defaults to -9999.
 * @param maxValue The maximum allowable value (inclusive). Defaults to 9999.
 * @param onValueChange A callback function invoked when the user confirms a valid new value.
 *                      It receives the new integer value as a parameter.
 * @param onDismiss A callback function invoked when the dialog is dismissed (e.g., by clicking outside).
 *                  Defaults to an empty lambda.
 */
@Composable
fun NumberInput(
    title: String,
    value: Int,
    modifier: Modifier = Modifier,
    minValue: Int = -9999,
    maxValue: Int = 9999,
    onValueChange: (Int) -> Unit,
    onDismiss: () -> Unit = {}
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        var valueText by rememberSaveable { mutableStateOf(value.toString()) }
        if (valueText == "0") valueText = ""

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

@Preview
@Composable
fun PreviewNumberInput() {
    AmarracosTheme {
        NumberInput(
            title = "Título",
            value = 0,
            modifier = Modifier.width(600.dp),
            onValueChange = {},
        )
    }
}