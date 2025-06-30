package com.rodalc.amarracos.comun

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Undo
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.SsidChart
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.rodalc.amarracos.utils.ToastRateLimiter

/**
 * Menu con las opciones (deshacer, ver resultados, etc.)
 *
 * @param undoEnabled Si se puede deshacer
 * @param undo La acción de deshacer
 */
@Composable
fun OptionsMenu(
    undoEnabled: Boolean,
    undo: () -> Unit,
    showResults: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = "More options",
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            DropdownMenuItem(
                leadingIcon = @Composable {
                    Icon(Icons.Outlined.SsidChart, contentDescription = "Ver resultados")
                },
                text = { Text("Finalizar y ver resultados") },
                onClick = {
                    expanded = false
                    showResults()
                }
            )
            if (undoEnabled) {
                DropdownMenuItem(
                    leadingIcon = @Composable {
                        Icon(Icons.AutoMirrored.Outlined.Undo, contentDescription = "Deshacer")
                    },
                    text = { Text("Deshacer") },
                    onClick = {
                        undo()
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * Menu con la opción de ordenar.
 */
@Composable
fun SortMenu() {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                Icons.Outlined.SwapVert,
                contentDescription = "Ordenar",
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(
                leadingIcon = @Composable {
                    Icon(Icons.Outlined.RadioButtonChecked, contentDescription = "Predeterminado")
                },
                text = { Text("Predeterminado") },
                onClick = {
                    expanded = false
                    ToastRateLimiter.showToast(context, "Próximamente")
                }
            )
            DropdownMenuItem(
                leadingIcon = @Composable {
                    Icon(Icons.Outlined.RadioButtonUnchecked, contentDescription = "Nombre")
                },
                text = { Text("Por nombre") },
                onClick = {
                    expanded = false
                    ToastRateLimiter.showToast(context, "Próximamente")
                }
            )
            DropdownMenuItem(
                leadingIcon = @Composable {
                    Icon(Icons.Outlined.RadioButtonUnchecked, contentDescription = "Puntos")
                },
                text = { Text("Por puntos") },
                onClick = {
                    expanded = false
                    ToastRateLimiter.showToast(context, "Próximamente")
                }
            )
        }
    }
}
