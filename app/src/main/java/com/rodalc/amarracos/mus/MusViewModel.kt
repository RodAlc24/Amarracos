package com.rodalc.amarracos.mus

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MusViewModel : ViewModel() {
    private val _buenos = MutableStateFlow(Mus.getBuenos())
    val buenos: StateFlow<Pareja> = _buenos

    private val _malos = MutableStateFlow(Mus.getMalos())
    val malos: StateFlow<Pareja> = _malos

    private val _envites = MutableStateFlow(Mus.getEnvites())
    val envites: StateFlow<Envites> = _envites

    private val _puntos = MutableStateFlow(Mus.getPuntos())
    val puntos: StateFlow<Int> = _puntos

    fun updateBuenos(buenos: Pareja) {
        Mus.setBuenos(buenos)
        _buenos.value = buenos
    }

    fun updateMalos(malos: Pareja) {
        Mus.setMalos(malos)
        _malos.value = malos
    }

    fun updateEnvites(envites: Envites) {
        Mus.setEnvites(envites)
        _envites.value = envites
    }

    fun updatePuntos(puntos: Int) {
        Mus.setPuntos(puntos)
        _puntos.value = puntos
    }

    fun resolverEnvite(ronda: RondasMus, pareja: Pareja) {
        Mus.resolverEnvite(ronda, pareja)
        _buenos.value = Mus.getBuenos()
        _malos.value = Mus.getMalos()
        _envites.value = Mus.getEnvites()
    }
}