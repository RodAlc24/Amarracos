package com.rodalc.amarracos.mus

import kotlinx.serialization.Serializable


@Serializable
data class Pareja(
    var nombre: String,
    var puntos: Int = 0,
    var victorias: Int = 0
)

@Serializable
data class Envites(
    var grande: Int = 0,
    var chica: Int = 0,
    var pares: Int = 0,
    var juego: Int = 0,
)

@Serializable
data class SerialicerMus(
    var buenos: Pareja,
    var malos: Pareja,
    var envites: Envites,
    var puntos: Int
)