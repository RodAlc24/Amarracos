package com.rodalc.amarracos.mus

object Mus {
    private var buenos: Pareja = Pareja("Buenos")
    private var malos: Pareja = Pareja("Malos")
    private var envites: Envites = Envites()
    private var puntos = 30

    fun getBuenos(): Pareja {
        return this.buenos
    }

    fun setBuenos(buenos: Pareja) {
        this.buenos = buenos
    }

    fun getMalos(): Pareja {
        return this.malos
    }

    fun setMalos(malos: Pareja) {
        this.malos = malos
    }

    fun getPuntos(): Int {
        return this.puntos
    }

    fun setPuntos(puntos: Int) {
        this.puntos = puntos
    }

    fun getEnvites(): Envites {
        return this.envites
    }

    fun setEnvites(envites: Envites) {
        this.envites = envites
    }

    fun resolverEnvite(ronda: RondasMus, pareja: Pareja) {
        pareja.puntos += this.envites.getEnvite(ronda)
        this.envites.setEnvite(ronda, 0)
    }


}
