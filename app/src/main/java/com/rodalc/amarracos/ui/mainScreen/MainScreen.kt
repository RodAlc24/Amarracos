@file:OptIn(ExperimentalMaterial3Api::class)

package com.rodalc.amarracos.ui.mainScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rodalc.amarracos.ui.elements.TitleTopBar
import com.rodalc.amarracos.ui.theme.AmarracosTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navigate: (String) -> Unit = {},
) {
    val navController = rememberNavController()
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TitleTopBar(
                title = "Amarracos",
                actions = {
                    IconButton(
                        onClick = { navigate(Screens.SCREEN_CONFIG.name) }
                    ) { Icon(Icons.Outlined.Settings, "Configuración") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navigate(
                    when (selectedIndex) {
                        1 -> Screens.SCREEN_POCHA.name
                        2 -> Screens.SCREEN_GENERICO.name
                        else -> Screens.SCREEN_MUS.name // 0 or any number out of range
                    }
                )
            }) {
                Icon(Icons.Outlined.Check, contentDescription = "Empezar el juego")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            PrimaryTabRow(
                selectedTabIndex = selectedIndex,
            ) {
                Tabs.entries.forEachIndexed { index, destination ->
                    Tab(
                        selected = index == selectedIndex,
                        onClick = {
                            navController.navigate(route = destination.name)
                            selectedIndex = index
                        },
                        text = {
                            Text(
                                text = destination.label,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
            NavHost(
                navController = navController,
                startDestination = Tabs.TAB_MUS.name,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(route = Tabs.TAB_MUS.name) {
                    Text("Mus")
                }
                composable(route = Tabs.TAB_POCHA.name) {
                    Text("Pocha")
                }
                composable(route = Tabs.TAB_GENERICO.name) {
                    Text("Generico")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMainScreen() {
    AmarracosTheme {
        MainScreen()
    }
}