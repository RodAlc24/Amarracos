package com.rodalc.amarracos.storage

import android.content.Context

/**
 * Clase abstracta para guardar y cargar el estado de un juego.
 *
 * TODO: ¿Mejor una interfaz? No se hacerlo en Kotlin
 *
 * Esta clase se encarga de manejar la persistencia de los datos en un archivo JSON.
 */
abstract class StateSaver(internal val filename: String) {

    /**
     * Verifica si el archivo de estado existe en el contexto dado.
     *
     * @param context El contexto de la aplicación o actividad.
     * @return true si el archivo existe, false en caso contrario.
     */
    fun fileExist(context: Context): Boolean {
        val file = context.getFileStreamPath(filename)
        return file != null && file.exists()
    }

    /**
     * Elimina el archivo de estado en el contexto dado.
     *
     * @param context El contexto de la aplicación o actividad.
     * @return true si el archivo se eliminó con éxito, false en caso contrario.
     */
    fun deleteFile(context: Context): Boolean {
        return context.deleteFile(filename)
    }

    /**
     * Guarda el estado en un archivo JSON.
     *
     * @param context El contexto de la aplicación o actividad.
     */
    abstract fun saveState(context: Context)

    /**
     * Carga el estado desde un archivo JSON.
     *
     * @param context El contexto de la aplicación o actividad.
     * @return La lista de jugadores cargados, o una lista vacía si el archivo no existe o hay un error al leerlo.
     */
    abstract fun loadState(context: Context)
}