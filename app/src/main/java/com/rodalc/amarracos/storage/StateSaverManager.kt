package com.rodalc.amarracos.storage

import android.content.Context
import java.io.FileNotFoundException
import java.io.IOException

/**
 * Manages the saving and loading of application state to/from internal storage.
 * This object provides utility functions to check for file existence, delete files,
 * write content to files, and read content from files.
 */
object StateSaverManager {
    /**
     * Checks if a file exists in the application's internal storage.
     *
     * @param filename The name of the file to check.
     * @param context The application context.
     * @return `true` if the file exists, `false` otherwise.
     */
    fun fileExists(filename: String, context: Context): Boolean {
        val file = context.getFileStreamPath(filename)
        return file != null && file.exists()
    }

    /**
     * Deletes a file from the application's internal storage.
     *
     * @param filename The name of the file to delete.
     * @param context The application context.
     * @return `true` if the file was successfully deleted, `false` otherwise.
     */
    fun deleteFile(filename: String, context: Context): Boolean {
        return context.deleteFile(filename)
    }

    /**
     * Writes content to a file in the application's internal storage.
     * If the file does not exist, it will be created. If it exists, its content will be overwritten.
     * @param filename The name of the file to write to.
     * @param content The string content to write to the file.
     * @param context The application context.
     */
    fun writteFile(filename: String, content: String, context: Context) {
        try {
            context.openFileOutput(filename, Context.MODE_PRIVATE).use {
                it.write(content.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Reads the content of a file from the application's internal storage.
     *
     * @param filename The name of the file to read from.
     * @param context The application context.
     * @return The content of the file as a String, or `null` if the file does not exist or an error occurs.
     */
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