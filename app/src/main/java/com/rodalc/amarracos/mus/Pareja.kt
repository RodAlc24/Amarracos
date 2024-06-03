package com.rodalc.amarracos.mus


data class Pareja(
    var nombre: String,
    var puntos: Int = 0,
    var victorias: Int = 0
)

data class Envites(
    var grande: Int = 0,
    var chica: Int = 0,
    var pares: Int = 0,
    var juego: Int = 0,
) {
    fun vacio(): Boolean {
        return (this.grande + this.chica + this.pares + this.juego) == 0
    }
}