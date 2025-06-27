/*
 * Copyright 2025 by Patryk Goworowski and Patrick Michalik.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This file corresponds to the following file:
 *   Repository: https://github.com/patrykandpatrick/
 *   File: sample/compose/src/main/kotlin/com/patrykandpatrick/vico/sample/compose/AITestScores.kt
 *   Commit: 7d7a1d6f1564640c747249bd23230fda7e5103ec
 *
 * It contains only the function previously known as JetpackComposeAITestScores.
 * The changes made are as follows:
 *   - Function name
 *   - Colors
 *   - VerticalLegend -> HorizontalLegend
 *   - When accessing the colors, use: index -> index % 20
 *   - Style: 4-space indentation, multiline function calls instead of a single long line.
 */

package com.rodalc.amarracos.resultados

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.point
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shapeComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.insets
import com.patrykandpatrick.vico.compose.common.rememberHorizontalLegend
import com.patrykandpatrick.vico.compose.common.vicoTheme
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.LegendItem
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shape.CorneredShape

val LegendLabelKey = ExtraStore.Key<Set<String>>()

@Composable
fun Puntuaciones(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier,
) {
    val lineColors = listOf(
        Color(0xFFFF5722),
        Color(0xFF4CAF50),
        Color(0xFF2196F3),
        Color(0xFFFFEB3B),
        Color(0xFF9C27B0),
        Color(0xFF009688),
        Color(0xFF795548),
        Color(0xFFE91E63),
        Color(0xFFFF9800),
        Color(0xFF00BCD4),
        Color(0xFF8BC34A),
        Color(0xFFFFC107),
        Color(0xFF673AB7),
        Color(0xFFCDDC39),
        Color(0xFF3F51B5),
        Color(0xFF03A9F4),
        Color(0xFFF44336),
        Color(0xFF607D8B),
        Color(0xFF00796B),
        Color(0xFFD32F2F)
    )
    val legendItemLabelComponent = rememberTextComponent(vicoTheme.textColor)
    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(
                LineCartesianLayer.LineProvider.series(
                    lineColors.map { color ->
                        LineCartesianLayer.rememberLine(
                            fill = LineCartesianLayer.LineFill.single(fill(color)),
                            areaFill = null,
                            pointProvider =
                                LineCartesianLayer.PointProvider.single(
                                    LineCartesianLayer.point(
                                        rememberShapeComponent(
                                            fill(color),
                                            CorneredShape.Pill
                                        )
                                    )
                                ),
                        )
                    }
                )
            ),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(),
            marker = rememberMarker(),
            legend =
                rememberHorizontalLegend(
                    items = { extraStore ->
                        extraStore[LegendLabelKey].forEachIndexed { index, label ->
                            add(
                                LegendItem(
                                    shapeComponent(
                                        fill(lineColors[index % 20]),
                                        CorneredShape.Pill
                                    ),
                                    legendItemLabelComponent,
                                    label,
                                )
                            )
                        }
                    },
                    padding = insets(top = 16.dp),
                ),
        ),
        modelProducer,
        modifier.height(300.dp),
        rememberVicoScrollState(scrollEnabled = true),
    )
}
