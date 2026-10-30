package com.ouazzaniapps.felixquest

enum class DayPhase {
    DAY,
    DUSK,
    NIGHT,
    DAWN
}

fun calculateDayPhase(elapsedSeconds: Int): DayPhase {
    val cycleSeconds = elapsedSeconds % 120

    return when (cycleSeconds) {
        in 0..49 -> DayPhase.DAY
        in 50..59 -> DayPhase.DUSK
        in 60..109 -> DayPhase.NIGHT
        in 110..119 -> DayPhase.DAWN
        else -> DayPhase.DAY
    }
}