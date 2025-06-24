package com.rodalc.amarracos.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rodalc.amarracos.generico.PantallaGenerico
import com.rodalc.amarracos.comun.PantallaResultados
import com.rodalc.amarracos.mus.PantallaMus
import com.rodalc.amarracos.pocha.PantallaPocha
import com.rodalc.amarracos.ui.theme.AmarracosTheme

/**
 * Punto de inicio de la aplicación
 */
@ExperimentalMaterial3Api
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


/**
 * Función que gestiona el controlador para la navegación entre pantallas.
 */
@ExperimentalMaterial3Api
@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "pantallaInicial") {
        composable("pantallaInicial") { PantallaInicio(navController) }
        composable("pantallaMus") { PantallaMus(navController) }
        composable("pantallaPocha") { PantallaPocha(navController) }
        composable("pantallaMarcador") { PantallaGenerico(navController) }
        composable("pantallaAjustes") { PantallaAjustes(navController) }
        composable("pantallaResultadosPocha") { PantallaResultados(pocha = true, navController) }
        composable("pantallaResultadosGenerico") { PantallaResultados(pocha = false, navController) }
    }
}

/**
 * Función auxiliar para Preview
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    showBackground = true,
    device = "id:pixel", backgroundColor = 0xFFFFFFFF
)
@Composable
fun GreetingPreview() {
    AmarracosTheme {
        MyApp()
    }
}