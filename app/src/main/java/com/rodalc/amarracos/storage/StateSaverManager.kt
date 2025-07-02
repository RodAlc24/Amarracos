package com.rodalc.amarracos.storage

import android.content.Context
import java.io.FileNotFoundException
import java.io.IOException

object StateSaverManager {
    fun fileExists(filename: String, context: Context): Boolean {
        val file = context.getFileStreamPath(filename)
        return file != null && file.exists()
    }

    fun deleteFile(filename: String, context: Context): Boolean {
        return context.deleteFile(filename)
    }

    fun writteFile(filename: String, content: String, context: Context) {
        try {
            context.openFileOutput(filename, Context.MODE_PRIVATE).use {
                it.write(content.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun readFile(filename: String, context: Context): String? {
        try {
            val inputStream = context.openFileInput(filename)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            return String(buffer, Charsets.UTF_8)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}