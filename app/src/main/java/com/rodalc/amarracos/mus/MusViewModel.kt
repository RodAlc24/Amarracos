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

    private val _canUndo = MutableStateFlow(Mus.canUndo())
    val canUndo: StateFlow<Boolean> = _canUndo

    fun update() {
        _buenos.value = Mus.getBuenos()
        _malos.value = Mus.getMalos()
        _envites.value = Mus.getEnvites()
        _canUndo.value = Mus.canUndo()
    }

    fun updateBuenos(buenos: Pareja) {
        Mus.setBuenos(buenos)
        _buenos.value = buenos
        _canUndo.value = Mus.canUndo()
    }

    fun updateMalos(malos: Pareja) {
        Mus.setMalos(malos)
        _malos.value = malos
        _canUndo.value = Mus.canUndo()
    }

    fun updateEnvites(envites: Envites) {
        Mus.setEnvites(envites)
        _envites.value = envites
        _canUndo.value = Mus.canUndo()
    }
}