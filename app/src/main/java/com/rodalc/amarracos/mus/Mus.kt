package com.rodalc.amarracos.mus

import com.rodalc.amarracos.storage.UndoStack

object Mus {
    private var buenos: Pareja = Pareja("Buenos")
    private var malos: Pareja = Pareja("Malos")
    private var envites: Envites = Envites()
    private var puntos = 30

    private var stack = UndoStack<List<Any>>()

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

    /**
     * Almacena en el stack el estado actual de la partida.
     */
    fun pushState() {
        this.stack.push(
            listOf(
                this.buenos.copy(),
                this.malos.copy(),
                this.envites.copy(),
                this.puntos
            )
        )
    }

    /**
     * Recupera el último estado de la partida a partir del stack.
     * Si hay un error (no hay nada que recuperar, por ejemplo) no hace nada.
     */
    fun popState() {
        val temp = this.stack.pop()
        this.buenos = (temp?.get(0) ?: Pareja("Buenos")) as Pareja
        this.malos = (temp?.get(1) ?: Pareja("Malos")) as Pareja
        this.envites = (temp?.get(2) ?: Envites()) as Envites
        this.puntos = (temp?.get(3) ?: 30) as Int
    }

    /**
     * Indica si se puede deshacer o no.
     *
     * @return Si se puede deshacer o no
     */
    fun canUndo(): Boolean {
        return this.stack.size() > 0
    }

    fun deleteStack() {
        this.stack = UndoStack()
    }


}
