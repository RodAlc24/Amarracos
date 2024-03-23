package com.rodalc.amarracos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rodalc.amarracos.ronda.PantallaJuego
import com.rodalc.amarracos.ui.theme.AmarracosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AmarracosTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "pantallaInicial") {
        composable("pantallaInicial") { PantallaInicio(navController) }
        composable("pantallaJuego") { PantallaJuego(navController) }
        composable("pantallaConteo") { PantallaInicio(navController) }
        composable("pantallaAjustes") { PantallaVacia(navController) }
        composable("pantallaCreditos") { PantallaVacia(navController) }

    }
}

@Preview(showBackground = true, widthDp = 640, heightDp = 360)
@Composable
fun GreetingPreview() {
    AmarracosTheme {
        MyApp()
    }
}