package com.rodalc.amarracos.ui.pocha

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodalc.amarracos.R
import com.rodalc.amarracos.ui.theme.AmarracosTheme
import com.rodalc.amarracos.utils.repeatingClickable

/**
 * A Composable function that displays a player card with their name, total points, and current points.
 * It also allows incrementing/decrementing points based on the game type (generic or Pocha).
 *
 * @param modifier Modifier for styling the card.
 * @param name The name of the player.
 * @param isLastPlayer Boolean indicating if this is the last player in a list.
 * @param totalPoints The total points accumulated by the player.
 * @param newPoints The points scored in the current round.
 * @param extraPoints Optional points for Pocha game (victories). If null, it's a generic game.
 * @param roundApuestas Boolean indicating if it's the betting round in Pocha.
 * @param changePoints Lambda function to handle point changes.
 */
@Composable
fun PlayerCard(
    modifier: Modifier = Modifier,
    name: String = "",
    isLastPlayer: Boolean = false,
    totalPoints: Int = 0,
    newPoints: Int = 0,
    extraPoints: Int? = null,
    roundApuestas: Boolean = true,
    changePoints: (Int) -> Unit = {},
) {
    val color =
        if (extraPoints == null || roundApuestas || extraPoints == newPoints) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error

    val myModifier = if (extraPoints != null) {
        modifier
            .fillMaxWidth(0.9f)
            .padding(8.dp)
            .clickable(
                onClick = { changePoints(if (roundApuestas) newPoints + 1 else (extraPoints + 1)) },
                enabled = if (roundApuestas) newPoints < 99 else extraPoints < 99
            )
    } else {
        modifier
            .fillMaxWidth(0.9f)
            .padding(8.dp)
            .clickable(
                onClick = { changePoints(newPoints + 1) },
                enabled = newPoints < 9999
            )
    }

    Card(
        modifier = myModifier
    ) {
        Column(
            modifier = Modifier
                .padding(start = 10.dp, top = 20.dp, bottom = 10.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$name: ",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        //                     .weight(1f) // Allocate space for the text
                        .clipToBounds(),
                    fontSize = 20.sp,
                    color = color
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = totalPoints.toString(),
                    fontSize = 20.sp,
                    color = color
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            if (extraPoints == null) { // Generico
                RowPointsWithTextField(
                    points = newPoints,
                    changePoints = { changePoints(it) },
                    playerName = name,
                    isLastPlayer = isLastPlayer,
                    removeEnabled = newPoints > -9999,
                    addEnabled = newPoints < 9999
                )
            } else { // Pocha
                RowPoints(
                    title = stringResource(R.string.text_bets),
                    points = newPoints,
                    changePoints = { changePoints(it) },
                    playerName = name,
                    removeEnabled = newPoints > 0 && roundApuestas,
                    addEnabled = newPoints < 99 && roundApuestas,
                    textColor = color
                )
                RowPoints(
                    title = stringResource(R.string.text_victories),
                    points = extraPoints,
                    changePoints = { changePoints(it) },
                    playerName = name,
                    removeEnabled = extraPoints > 0 && !roundApuestas,
                    addEnabled = extraPoints < 99 && !roundApuestas,
                    textColor = color
                )
            }
        }
    }
}


/**
 * A composable function that displays a row with a title, points, and buttons to increment/decrement the points.
 *
 * @param title The title to display for the points.
 * @param points The current points value.
 * @param changePoints A lambda function to be called when the increment/decrement buttons are clicked.
 * It receives an integer representing the amount to increment/decrement by (e.g., 1 for increment, -1 for decrement).
 * @param modifier Optional [Modifier] for this composable.
 * @param playerName The name of the player, used in content descriptions for accessibility.
 * @param removeEnabled A boolean indicating whether the remove button should be enabled. Defaults to true.
 * @param addEnabled A boolean indicating whether the add button should be enabled. Defaults to true.
 * @param textColor The color of the points text. Defaults to `MaterialTheme.colorScheme.onSurface`.
 */
@Composable
private fun RowPoints(
    title: String,
    points: Int,
    changePoints: (Int) -> Unit,
    modifier: Modifier = Modifier,
    playerName: String = stringResource(R.string.default_player_name),
    removeEnabled: Boolean = true,
    addEnabled: Boolean = true,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = modifier.fillMaxWidth(0.9f),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$title: ",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f) // Allocate space for the text
                .clipToBounds()
        )
        TextButton(
            onClick = { changePoints(points - 1) },
            enabled = removeEnabled
        ) {
            Icon(
                Icons.Rounded.Remove,
                contentDescription = stringResource(R.string.desc_remove_point_format, playerName)
            )
        }
        Box(
            modifier = Modifier.width(40.dp),
            contentAlignment = Alignment.Center
        ) { Text(text = points.toString(), color = textColor) }
        TextButton(
            onClick = { changePoints(points + 1) },
            enabled = addEnabled
        ) {
            Icon(
                Icons.Rounded.Add,
                contentDescription = stringResource(R.string.desc_add_point_format, playerName)
            )
        }
    }
}

/**
 * A Composable function that displays a row with an [OutlinedTextField] for points,
 * and buttons to increment/decrement the points.
 *
 * This composable is typically used when direct input of points is desired,
 * alongside button-based adjustments.
 *
 * @param points The current points value.
 * @param changePoints A lambda function to be called when the points are changed,
 * either via the text field or the increment/decrement buttons. It receives the new points value.
 * @param modifier Optional [Modifier] for this composable.
 * @param playerName The name of the player, used in content descriptions for accessibility.
 * @param isLastPlayer A boolean indicating if this is the last player in a list. This influences the keyboard's IME action (Done vs Next). Defaults to false.
 * @param removeEnabled A boolean indicating whether the remove button should be enabled. Defaults to true.
 * @param addEnabled A boolean indicating whether the add button should be enabled. Defaults to true.
 */
@Composable
private fun RowPointsWithTextField(
    points: Int,
    changePoints: (Int) -> Unit,
    modifier: Modifier = Modifier,
    playerName: String = stringResource(R.string.default_player_name),
    isLastPlayer: Boolean = false,
    removeEnabled: Boolean = true,
    addEnabled: Boolean = true,
) {
    Row(
        modifier = modifier.fillMaxWidth(0.9f),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var isError by rememberSaveable { mutableStateOf(false) }
        TextButton(
            onClick = { changePoints(points - 1) },
            enabled = removeEnabled,
            modifier = Modifier.repeatingClickable(
                interactionSource = remember { MutableInteractionSource() },
                minDelayMillis = 20,
                onClick = { changePoints(points - 1) },
                enabled = removeEnabled
            )
        ) {
            Icon(
                Icons.Rounded.Remove,
                contentDescription = stringResource(R.string.desc_remove_point_format, playerName)
            )
        }
        OutlinedTextField(
            value = if (points == 0) "" else points.toString(),
            placeholder = { Text(text = points.toString()) },
            isError = isError,
            onValueChange = {
                val newPoints = it.toIntOrNull() ?: points
                if (newPoints in -9999..9999) {
                    changePoints(newPoints)
                    isError = false
                } else {
                    isError = true
                }
            },
            modifier = Modifier.width(100.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = if (isLastPlayer) ImeAction.Done else ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
        )
        TextButton(
            onClick = { changePoints(points + 1) },
            enabled = addEnabled,
            modifier = Modifier.repeatingClickable(
                interactionSource = remember { MutableInteractionSource() },
                minDelayMillis = 20,
                onClick = { changePoints(points + 1) },
                enabled = addEnabled
            )
        ) {
            Icon(
                Icons.Rounded.Add,
                contentDescription = stringResource(R.string.desc_add_point_format, playerName)
            )
        }
    }

}

@Preview
@Composable
fun PreviewPlayerCard() {
    AmarracosTheme {
        PlayerCard(
            modifier = Modifier.width(350.dp),
            name = "mmmmmmmmmm",
            newPoints = 14,
            totalPoints = 10,
            extraPoints = 2,
            changePoints = {})
    }
}

@Preview
@Composable
fun PreviewRowPoints() {
    AmarracosTheme {
        RowPoints(
            title = "Puntos",
            points = 14,
            changePoints = {},
            modifier = Modifier.width(350.dp),
            playerName = "Jugador",
            removeEnabled = true,
            addEnabled = true
        )
    }
}

@Preview
@Composable
fun PreviewRowPointsWithTextField() {
    AmarracosTheme {
        RowPointsWithTextField(
            points = 14,
            changePoints = {},
            modifier = Modifier.width(350.dp),
            playerName = "Jugador",
            removeEnabled = true,
            addEnabled = true
        )
    }
}
