package com.ouazzaniapps.felixquest

enum class DayPhase {
    DAY,
    DUSK,
    NIGHT,
    DAWN
}

fun calculateDayPhase(elapsedSeconds: Int): DayPhase {
    val cycleSeconds = elapsedSeconds % 600

    return when (cycleSeconds) {
        in 0..299 -> DayPhase.DAY
        in 300..359 -> DayPhase.DUSK
        in 360..539 -> DayPhase.NIGHT
        in 540..599 -> DayPhase.DAWN
        else -> DayPhase.DAY
    }
}