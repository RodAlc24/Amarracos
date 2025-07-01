package com.rodalc.amarracos.ui.mus

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodalc.amarracos.ui.theme.AmarracosTheme

@Composable
fun Pareja(
    name: String,
    juegos: Int,
    puntos: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "$name: $juegos", fontSize = 25.sp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = {},
                enabled = puntos > 0,
                modifier = Modifier
                    .padding(5.dp)
                    .size(60.dp)
            ) { Icon(Icons.Rounded.Remove, "Quitar un punto", Modifier.size(40.dp)) }
            TextButton(
                onClick = { },
                modifier = Modifier.width(110.dp)
            ) {
                Text(
                    text = puntos.toString(),
                    fontSize = 60.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            IconButton(
                onClick = {},
                modifier = Modifier
                    .padding(5.dp)
                    .size(60.dp)
            ) { Icon(Icons.Rounded.Add, "Añadir un punto", Modifier.size(40.dp)) }
        }
    }
}


@Preview
@Composable
fun ParejaPreview() {
    AmarracosTheme {
        Pareja(name = "Buenos", juegos = 2, puntos = 24)
    }
}