package com.zettl.clockwork.domain.level

/**
 * Aufgabentyp pro Level.
 */
enum class LevelTaskType {
    /** Uhr auf vorgegebene Zeit einstellen („Stelle Viertel nach 4 ein“). */
    SET_CLOCK,
    /** Welche Uhrzeit zeigt die Uhr? (Multiple Choice) */
    WHICH_TIME,
    /** Alltagssituation („Wann gehst du in die Schule?“) */
    EVERYDAY
}

/**
 * Definition einer Level-Aufgabe.
 * @param targetHour Stunde 0–11 (12 Uhr = 0)
 * @param targetMinute Ziel-Minute 0–59
 * @param taskArg Anzeigetext für die Aufgabe (z. B. „Viertel nach 4“). UI nutzt taskType + taskArg für den Satz.
 */
data class LevelDefinition(
    val lessonId: String,
    val levelIndex: Int,
    val taskType: LevelTaskType,
    val targetHour: Int,
    val targetMinute: Int,
    val taskArg: String? = null,
    val talersReward: Int = 5
)
