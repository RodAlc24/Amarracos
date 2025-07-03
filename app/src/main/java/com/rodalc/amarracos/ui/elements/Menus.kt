package com.rodalc.amarracos.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Undo
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.SsidChart
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.rodalc.amarracos.data.generico.GenericoViewModel

/**
 * Displays a dropdown menu with options to show results and undo the last action.
 *
 * @param undoEnabled Whether the undo option should be enabled.
 * @param undo Callback to be invoked when the undo option is selected.
 * @param showResults Callback to be invoked when the show results option is selected.
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
        ) {
            DropdownMenuItem(
                leadingIcon = @Composable {
                    Icon(Icons.Outlined.SsidChart, contentDescription = "Ver resultados")
                },
                text = { Text("Ver resultados") },
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
 * Menu with sorting options.
 *
 * @param sortBy Function to call when a sort option is selected.
 * @param modifier Modifier to apply to the menu component.
 */
@Composable
fun SortMenu(
    sortBy: (GenericoViewModel.SortType) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
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
                text = { Text("Predeterminado") },
                onClick = {
                    expanded = false
                    sortBy(GenericoViewModel.SortType.ID)
                }
            )
            DropdownMenuItem(
                text = { Text("Por nombre") },
                onClick = {
                    expanded = false
                    sortBy(GenericoViewModel.SortType.NAME)
                }
            )
            DropdownMenuItem(
                text = { Text("Por puntos") },
                onClick = {
                    expanded = false
                    sortBy(GenericoViewModel.SortType.POINTS)
                }
            )
        }
    }
}
