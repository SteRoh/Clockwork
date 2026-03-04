package com.zettl.clockwork.domain.level

/**
 * Liefert die Level-Definitionen für alle Lektionen.
 * Aufgaben sind auf Deutsch (Volle Stunde, Viertel nach, etc.).
 */
class LevelRepository {

    fun getLevel(lessonId: String, levelIndex: Int): LevelDefinition? {
        val list = levelsByLesson[lessonId] ?: return null
        return list.getOrNull(levelIndex)
    }

    private val levelsByLesson: Map<String, List<LevelDefinition>> = mapOf(
        "1" to listOf(
            LevelDefinition("1", 0, LevelTaskType.SET_CLOCK, 3, 0, "3 Uhr"),
            LevelDefinition("1", 1, LevelTaskType.SET_CLOCK, 5, 0, "5 Uhr"),
            LevelDefinition("1", 2, LevelTaskType.SET_CLOCK, 8, 0, "8 Uhr"),
            LevelDefinition("1", 3, LevelTaskType.SET_CLOCK, 0, 0, "12 Uhr"),
            LevelDefinition("1", 4, LevelTaskType.SET_CLOCK, 1, 0, "1 Uhr")
        ),
        "2" to listOf(
            LevelDefinition("2", 0, LevelTaskType.SET_CLOCK, 2, 30, "halb drei"),
            LevelDefinition("2", 1, LevelTaskType.SET_CLOCK, 4, 30, "halb fünf"),
            LevelDefinition("2", 2, LevelTaskType.SET_CLOCK, 7, 30, "halb acht"),
            LevelDefinition("2", 3, LevelTaskType.SET_CLOCK, 11, 30, "halb zwölf"),
            LevelDefinition("2", 4, LevelTaskType.SET_CLOCK, 6, 30, "halb sieben")
        ),
        "3" to listOf(
            LevelDefinition("3", 0, LevelTaskType.SET_CLOCK, 4, 15, "Viertel nach 4"),
            LevelDefinition("3", 1, LevelTaskType.SET_CLOCK, 2, 15, "Viertel nach 2"),
            LevelDefinition("3", 2, LevelTaskType.SET_CLOCK, 9, 15, "Viertel nach 9"),
            LevelDefinition("3", 3, LevelTaskType.SET_CLOCK, 0, 15, "Viertel nach 12"),
            LevelDefinition("3", 4, LevelTaskType.SET_CLOCK, 6, 15, "Viertel nach 6")
        ),
        "4" to listOf(
            LevelDefinition("4", 0, LevelTaskType.SET_CLOCK, 3, 45, "Viertel vor 4"),
            LevelDefinition("4", 1, LevelTaskType.SET_CLOCK, 5, 45, "Viertel vor 6"),
            LevelDefinition("4", 2, LevelTaskType.SET_CLOCK, 8, 45, "Viertel vor 9"),
            LevelDefinition("4", 3, LevelTaskType.SET_CLOCK, 11, 45, "Viertel vor 12"),
            LevelDefinition("4", 4, LevelTaskType.SET_CLOCK, 7, 45, "Viertel vor 8")
        ),
        "5" to listOf(
            LevelDefinition("5", 0, LevelTaskType.SET_CLOCK, 2, 5, "5 nach 2"),
            LevelDefinition("5", 1, LevelTaskType.SET_CLOCK, 4, 20, "20 nach 4"),
            LevelDefinition("5", 2, LevelTaskType.SET_CLOCK, 10, 35, "25 vor 11"),
            LevelDefinition("5", 3, LevelTaskType.SET_CLOCK, 7, 40, "20 vor 8"),
            LevelDefinition("5", 4, LevelTaskType.SET_CLOCK, 11, 55, "5 vor 12"),
            LevelDefinition("5", 5, LevelTaskType.SET_CLOCK, 3, 25, "25 nach 3")
        ),
        "6" to listOf(
            LevelDefinition("6", 0, LevelTaskType.WHICH_TIME, 9, 0, null),
            LevelDefinition("6", 1, LevelTaskType.WHICH_TIME, 2, 30, null),
            LevelDefinition("6", 2, LevelTaskType.WHICH_TIME, 6, 15, null),
            LevelDefinition("6", 3, LevelTaskType.WHICH_TIME, 0, 45, null),
            LevelDefinition("6", 4, LevelTaskType.WHICH_TIME, 5, 40, null)
        ),
        "7" to listOf(
            LevelDefinition("7", 0, LevelTaskType.SET_CLOCK, 7, 30, "Schulbeginn (halb acht)"),
            LevelDefinition("7", 1, LevelTaskType.SET_CLOCK, 0, 0, "Mittagessen (12 Uhr)"),
            LevelDefinition("7", 2, LevelTaskType.SET_CLOCK, 3, 0, "Nachmittag (3 Uhr)"),
            LevelDefinition("7", 3, LevelTaskType.SET_CLOCK, 7, 0, "Abendessen (7 Uhr)"),
            LevelDefinition("7", 4, LevelTaskType.SET_CLOCK, 8, 0, "Schlafenszeit (8 Uhr)")
        )
    )
}
