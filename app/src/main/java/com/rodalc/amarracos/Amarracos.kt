package com.rodalc.amarracos

/**
 * Clase que guarda los valores actuales de la partida
 */
data class Amarracos(
    var nombrePareja1: String = "Buenos",
    var nombrePareja2: String = "Malos",

    var puntosPareja1: Int = 0,
    var puntosPareja2: Int = 0,

    var juegosPareja1: Int = 0,
    var juegosPareja2: Int = 0,

    var partidaActual: Jugada = Jugada()
) {
}


