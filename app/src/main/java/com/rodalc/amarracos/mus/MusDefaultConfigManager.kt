package com.rodalc.amarracos.mus

import android.content.Context
import com.rodalc.amarracos.storage.StateSaver
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.FileNotFoundException
import java.io.IOException

object MusDefaultConfigManager : StateSaver("mus_default_config.json") {
    private var buenos = "Buenos"
    private var malos = "Malos"

    fun getBuenos(): String {
        return this.buenos
    }

    fun setBuenos(buenos: String) {
        this.buenos = buenos
    }

    fun getMalos(): String {
        return this.malos
    }

    fun setMalos(malos: String) {
        this.malos = malos
    }

    fun reset() {
        this.buenos = "Buenos"
        this.malos = "Malos"
    }

    override fun saveState(context: Context) {
        val temp = listOf(this.buenos, this.malos)
        val json = Json.encodeToString(temp)
        println(json)
        context.openFileOutput(this.filename, Context.MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
    }

    override fun loadState(context: Context) {
        try {
            val inputStream = context.openFileInput(filename)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            val jsonString = String(buffer, Charsets.UTF_8)
            val temp = Json.decodeFromString<List<String>>(jsonString)
            this.buenos = temp.getOrElse(0) { "Buenos" }
            this.malos = temp.getOrElse(1) { "Malos" }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun canLoadState(context: Context): Boolean {
        return this.fileExist(context)
    }

    fun discardBackup(context: Context): Boolean {
        return this.deleteFile(context)
    }
}