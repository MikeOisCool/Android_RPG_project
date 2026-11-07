package com.ouazzaniapps.felixquest

sealed class SaveSlotState {
    object Empty : SaveSlotState()
    data class Occupied(val gameSave: GameSave) : SaveSlotState()
    data class Error(val message: String) : SaveSlotState()
}