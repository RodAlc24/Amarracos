package com.rodalc.amarracos.ui.mus

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodalc.amarracos.data.mus.MusViewModel
import com.rodalc.amarracos.ui.theme.AmarracosTheme
import com.rodalc.amarracos.utils.repeatingClickable

/**
 * Composable function to display and manage the envites (bets) in a Mus game.
 *
 * This function provides the UI elements for increasing, decreasing, and assigning the winner of an envite.
 * It can be displayed either as a row or a column.
 *
 * @param value The current value of the envite.
 * @param increment A lambda function to be called when the envite value is incremented or decremented. It takes an integer representing the amount to increment (can be negative). Defaults to an empty lambda.
 * @param updateEnvite A lambda function to be called when a team wins the envite. It takes a [MusViewModel.Teams] enum indicating the winning team. Defaults to an empty lambda.
 * @param row A boolean indicating whether the envite controls should be displayed in a row (true) or a column (false). Defaults to true.
 */
@Composable
private fun ContentEnvites(
    value: Int,
    increment: (Int) -> Unit = {},
    updateEnvite: (MusViewModel.Teams) -> Unit = { _ -> },
    row: Boolean = true,
) {

    val buttonRemove = @Composable {
        IconButton(
            onClick = { increment(-1) },
            enabled = value > 0,
            modifier = Modifier
                .padding(8.dp)
                .repeatingClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    enabled = value > 0,
                    onClick = { increment(-1) }
                )
        ) {
            Icon(Icons.Rounded.Remove, contentDescription = "Incrementar envite")
        }
    }

    val buttonAdd = @Composable {
        IconButton(
            onClick = { increment(1) },
            enabled = value < 100,
            modifier = Modifier
                .padding(8.dp)
                .repeatingClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    enabled = value < 100,
                    onClick = { increment(1) }
                )
        ) {
            Icon(Icons.Rounded.Add, contentDescription = "Decrementar envite")
        }
    }

    FilledIconButton(
        onClick = { updateEnvite(MusViewModel.Teams.BUENOS) },
        enabled = value > 0
    ) {
        Icon(
            imageVector = if (row) Icons.AutoMirrored.Rounded.ArrowBack else Icons.Rounded.ArrowUpward,
            contentDescription = "Buenos ganan el envite"
        )
    }
    if (row) {
        buttonRemove()
    } else {
        buttonAdd()
    }
    Box(
        modifier = Modifier.width(50.dp),
        contentAlignment = Alignment.Center
    ) {
        TextButton(
            onClick = { increment(2) },
            enabled = value < 98
        ) {
            Text(
                text = value.toString(),
                fontSize = 20.sp,
            )
        }
    }
    if (row) {
        buttonAdd()
    } else {
        buttonRemove()
    }
    FilledIconButton(
        onClick = { updateEnvite(MusViewModel.Teams.MALOS) },
        enabled = value > 0
    ) {
        Icon(
            imageVector = if (row) Icons.AutoMirrored.Rounded.ArrowForward else Icons.Rounded.ArrowDownward,
            contentDescription = "Malos ganan el envite"
        )
    }
}

/**
 * Composable function to display a row of envites.
 *
 * @param value The current value of the envite.
 * @param modifier The modifier to be applied to the row.
 * @param increment A function to increment the envite value.
 * @param updateEnvite A function to update the envite when a team wins.
 */
@Composable
private fun RowEnvite(
    value: Int,
    modifier: Modifier = Modifier,
    increment: (Int) -> Unit = {},
    updateEnvite: (MusViewModel.Teams) -> Unit = { _ -> },
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ContentEnvites(
            value = value,
            increment = increment,
            updateEnvite = updateEnvite,
            row = true
        )
    }
}

/**
 * Composable function to display a column of envites.
 *
 * @param value The current value of the envite.
 * @param modifier The modifier to be applied to the layout.
 * @param increment A lambda function to increment the envite value.
 * @param updateEnvite A lambda function to update the envite with the winning team.
 */
@Composable
private fun ColumnEnvite(
    value: Int,
    modifier: Modifier = Modifier,
    increment: (Int) -> Unit = {},
    updateEnvite: (MusViewModel.Teams) -> Unit = { _ -> },
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ContentEnvites(
            value = value,
            increment = increment,
            updateEnvite = updateEnvite,
            row = false
        )
    }
}

/**
 * Composable function that displays the "envites" (bets) in a Mus game.
 * It shows the current value of each envite (Grande, Chica, Pares, Juego)
 * and provides buttons to increment/decrement the value and assign the envite to a team.
 *
 * @param viewModel The [MusViewModel] that holds the game state and logic.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun Envites(
    viewModel: MusViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState = viewModel.uiState.collectAsState()
    Column(modifier = modifier) {
        RowEnvite(
            value = uiState.value.enviteGrande,
            modifier = modifier,
            increment = {
                viewModel.incrementEnvite(
                    envite = MusViewModel.Envites.GRANDE,
                    increment = it
                )
            },
            updateEnvite = {
                viewModel.updateEnvite(
                    envite = MusViewModel.Envites.GRANDE,
                    team = it
                )
            }
        )
        RowEnvite(
            value = uiState.value.enviteChica,
            modifier = modifier,
            increment = {
                viewModel.incrementEnvite(
                    envite = MusViewModel.Envites.CHICA,
                    increment = it
                )
            },
            updateEnvite = {
                viewModel.updateEnvite(
                    envite = MusViewModel.Envites.CHICA,
                    team = it
                )
            }
        )
        RowEnvite(
            value = uiState.value.envitePares,
            modifier = modifier,
            increment = {
                viewModel.incrementEnvite(
                    envite = MusViewModel.Envites.PARES,
                    increment = it
                )
            },
            updateEnvite = {
                viewModel.updateEnvite(
                    envite = MusViewModel.Envites.PARES,
                    team = it
                )
            }
        )
        RowEnvite(
            value = uiState.value.enviteJuego,
            modifier = modifier,
            increment = {
                viewModel.incrementEnvite(
                    envite = MusViewModel.Envites.JUEGO,
                    increment = it
                )
            },
            updateEnvite = {
                viewModel.updateEnvite(
                    envite = MusViewModel.Envites.JUEGO,
                    team = it
                )
            }
        )
    }
}


@Preview
@Composable
fun RowEnvitesPreview() {
    AmarracosTheme() {
        RowEnvite(15)
    }
}

@Preview
@Composable
fun ColumnEnvitesPreview() {
    AmarracosTheme() {
        ColumnEnvite(15)
    }
}

@Preview
@Composable
fun EnvitesPreview() {
    AmarracosTheme {
        Envites(MusViewModel())
    }
}