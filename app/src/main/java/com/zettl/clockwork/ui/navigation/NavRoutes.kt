package com.zettl.clockwork.ui.navigation

object NavRoutes {
    const val HOME = "home"
    const val LESSONS = "lessons"
    const val LEVEL = "level/{lessonId}/{levelIndex}"

    fun level(lessonId: String, levelIndex: Int) = "level/$lessonId/$levelIndex"
}
