package com.rodalc.amarracos.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rodalc.amarracos.R
import com.rodalc.amarracos.ui.theme.AmarracosTheme
import com.rodalc.amarracos.ui.theme.Playfair

/**
 * Primera pantalla de la aplicación, contiene el menú para navegar por ella.
 *
 * @param navController El controlador usado para cambiar entre pantallas.
 */
@ExperimentalMaterial3Api
@Composable
fun PantallaInicio(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "Amarracos",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = Playfair,
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate(("pantallaAjustes")) }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Go to settings",
                            tint = MaterialTheme.colorScheme.primary

                        )
                    }
                },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(vertical = 100.dp, horizontal = 50.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_transparente),
                contentDescription = "Logo amarracos",

            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Spacer(modifier = Modifier.weight(0.05f))
            GameButton(icon = Icons.AutoMirrored.Rounded.ArrowForward, text = "Mus") {
                navController.navigate("pantallaMus")
            }
            Spacer(modifier = Modifier.height(10.dp))
            GameButton(icon = Icons.AutoMirrored.Rounded.ArrowForward, text = "Pocha") {
                navController.navigate("pantallaPocha")
            }
            Spacer(modifier = Modifier.height(10.dp))
            GameButton(icon = Icons.AutoMirrored.Rounded.ArrowForward, text = "Genérico") {
                navController.navigate("pantallaMarcador")
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
fun GameButton(icon: ImageVector, text: String, onClick: () -> Unit) {
    Button(onClick = onClick, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
        Row {
            Text(text, fontSize = 20.sp, modifier = Modifier.fillMaxWidth(0.3f), color = MaterialTheme.colorScheme.primary)
            Icon(imageVector = icon, contentDescription = icon.name, tint = MaterialTheme.colorScheme.primary)
        }
    }
}

/**
 * Función auxiliar para Preview
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    showBackground = true, device = "spec:parent=pixel_5,orientation=landscape",
)
@Composable
fun InicioPreview() {
    val navController = rememberNavController()
    AmarracosTheme {
        PantallaInicio(navController)
    }
}
