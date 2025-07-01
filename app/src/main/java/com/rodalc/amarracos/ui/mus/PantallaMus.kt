package com.rodalc.amarracos.ui.mus

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rodalc.amarracos.data.mus.MusViewModel
import com.rodalc.amarracos.ui.theme.AmarracosTheme

@Composable
fun PantallaMus(
    modifier: Modifier = Modifier,
    musViewModel: MusViewModel = MusViewModel(),
) {
    Envites(viewModel = musViewModel, modifier = modifier)
}


@Preview
@Composable
fun PantallaMusPreview() {
    AmarracosTheme {
        PantallaMus()
    }
}