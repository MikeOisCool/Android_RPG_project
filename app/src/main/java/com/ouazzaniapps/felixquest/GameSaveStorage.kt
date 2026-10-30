package com.ouazzaniapps.felixquest

import android.content.Context
import kotlinx.serialization.json.Json
import java.io.File
import android.util.AtomicFile
import androidx.core.util.writeText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import androidx.core.util.readText
import java.io.FileNotFoundException
import kotlinx.serialization.decodeFromString


class GameSaveStorage(context: Context) {

    private val directory = context.applicationContext.filesDir

    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    private fun slotFile(slot: Int): File {
        require(slot in 1..3) { "Speicherplatz muss zwischen 1 und 3 liegen" }

        return File(directory, "save$slot.json")
    }

    suspend fun save(slot: Int, gameSave: GameSave) {
        withContext(Dispatchers.IO) {
            val text = json.encodeToString(gameSave)
            AtomicFile(slotFile(slot)).writeText(text)
        }
    }

    suspend fun load(slot: Int): GameSave? {
        return withContext(Dispatchers.IO) {
            val file = slotFile(slot)

            val text = try {
                AtomicFile(file).readText()
            } catch (e: FileNotFoundException) {
                if (file.exists()) throw e
                return@withContext null
            }

            json.decodeFromString<GameSave>(text)
        }
    }
}