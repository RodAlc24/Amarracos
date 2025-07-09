package com.rodalc.amarracos.ui.mus

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodalc.amarracos.R
import com.rodalc.amarracos.ui.theme.AmarracosTheme

/**
 * Composable function that displays the score of a pair in a Mus game.
 *
 * @param name The name of the pair.
 * @param juegos The number of games won by the pair.
 * @param puntos The current points of the pair.
 * @param modifier The modifier to be applied to the layout.
 * @param increment A callback function to increment or decrement the points.
 * @param landscape A boolean indicating whether the layout should be optimized for landscape mode.
 * @param extraContent An optional composable function to display additional content.
 */
@Composable
fun Pareja(
    name: String,
    juegos: Int,
    puntos: Int,
    modifier: Modifier = Modifier,
    increment: (Int) -> Unit = {},
    landscape: Boolean = false,
    extraContent: @Composable () -> Unit = {}
) {
    val display = @Composable {
        TextButton(
            onClick = { increment(1) },
            modifier = Modifier.width(110.dp)
        ) {
            Text(
                text = puntos.toString(),
                fontSize = 60.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "$name: $juegos", fontSize = 25.sp)
        Spacer(modifier = Modifier.height(10.dp))
        if (landscape) {
            display()
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(
                onClick = { increment(-1) },
                enabled = puntos > 0,
                modifier = Modifier
                    .padding(5.dp)
                    .size(60.dp),
            ) {
                Icon(
                    Icons.Rounded.Remove,
                    contentDescription = stringResource(R.string.desc_remove_point_format, name),
                    Modifier.size(40.dp),
                )
            }
            if (!landscape) {
                display()
            }
            TextButton(
                onClick = { increment(1) },
                modifier = Modifier
                    .padding(5.dp)
                    .size(60.dp),
            ) {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.desc_add_point_format, name),
                    Modifier.size(40.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        extraContent()
    }
}


@Preview(apiLevel = 35, showSystemUi = false, showBackground = true)
@Composable
fun ParejaPortraitPreview() {
    AmarracosTheme {
        Pareja(name = "Buenos", juegos = 2, puntos = 24, landscape = false)
    }
}

@Preview(apiLevel = 35, showSystemUi = false, showBackground = true)
@Composable
fun ParejaLandscapePreview() {
    AmarracosTheme {
        Pareja(name = "Buenos", juegos = 2, puntos = 24, landscape = true)
    }
}
