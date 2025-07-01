package com.rodalc.amarracos.ui.main

/**
 * This enums defines the routes of the tabs navigation.
 *
 * @param label The label of the tab.
 */
enum class Tabs(val label: String) {
    TAB_MUS(label = "Mus"),
    TAB_POCHA(label = "Pocha"),
    TAB_GENERICO(label = "Generico")
}

/**
 * This enums defines the routes of the screens navigation.
 *
 * @param label The label of the screen.
 */
enum class Screens(val label: String) {
    SCREEN_START(label = "Amarracos"),
    SCREEN_CONFIG(label = "Ajustes"),
    SCREEN_MUS(label = "Mus"),
    SCREEN_POCHA(label = "Pocha"),
    SCREEN_GENERICO(label = "Genérico"),
    SCREEN_RES_POCHA(label = "Resultados"),
    SCREEN_RES_GEN(label = "Resultados"),
}

