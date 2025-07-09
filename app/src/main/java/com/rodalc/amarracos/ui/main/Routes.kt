package com.rodalc.amarracos.ui.main

import com.rodalc.amarracos.R

/**
 * This enums defines the routes of the tabs navigation.
 *
 * @param title The title of the tab.
 */
enum class Tabs(val title: Int) {
    TAB_MUS(title = R.string.title_mus),
    TAB_POCHA(title = R.string.title_pocha),
    TAB_GENERICO(title = R.string.title_generico)
}

/**
 * This enums defines the routes of the screens navigation.
 *
 * @param title The title of the screen.
 */
enum class Screens(val title: Int) {
    SCREEN_START(title = R.string.app_name),
    SCREEN_CONFIG(title = R.string.title_config),
    SCREEN_MUS(title = R.string.title_mus),
    SCREEN_POCHA(title = R.string.title_pocha),
    SCREEN_GENERICO(title = R.string.title_generico),
    SCREEN_RES_POCHA(title = R.string.title_results),
    SCREEN_RES_GEN(title = R.string.title_results),
}

