package com.rodalc.amarracos

import android.app.Application

/**
 * Clase para la aplicación, incluye variables globales de uso frecuente
 */
class Amarracos : Application() {
    var nombrePareja1 : String = "Buenos"
    var nombrePareja2 : String = "Malos"

    var puntosPareja1 : Int = 0
    var puntosPareja2 : Int = 0

    var juegosPareja1 : Int = 0
    var juegosPareja2 : Int = 0

    var partidaActual = Jugada()

}