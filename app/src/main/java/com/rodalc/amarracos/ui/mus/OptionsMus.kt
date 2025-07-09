package com.rodalc.amarracos.ui.mus

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodalc.amarracos.R
import com.rodalc.amarracos.ui.theme.AmarracosTheme

/**
 * Composable function that displays an "Órdago" button.
 * "Órdago" is a term used in the game of Mus, signifying an all-in bet.
 *
 * @param modifier The modifier to be applied to the button.
 * @param onClick Will be called when the user clicks the button.
 */
@Composable
fun ButtonOrdago(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Text(text = stringResource(R.string.text_ordago))
    }
}

/**
 * Composable function that displays an undo button.
 *
 * @param modifier The modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable.
 * @param onClick Will be called when the user clicks the button.
 */
@Composable
fun ButtonUndo(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    TextButton(
        modifier = modifier
            .size(80.dp)
            .padding(8.dp),
        onClick = onClick,
        enabled = enabled,
    ) {
        Icon(
            Icons.AutoMirrored.Rounded.Undo,
            contentDescription = stringResource(R.string.desc_undo),
            modifier = Modifier.size(30.dp),
        )
    }

}

@Preview(apiLevel = 35, showBackground = true)
@Composable
fun PreviewButtonOrdago() {
    AmarracosTheme {
        ButtonOrdago()
    }
}

@Preview(apiLevel = 35, showSystemUi = false, showBackground = true)
@Composable
fun PreviewButtonUndo() {
    AmarracosTheme {
        ButtonUndo()
    }
}