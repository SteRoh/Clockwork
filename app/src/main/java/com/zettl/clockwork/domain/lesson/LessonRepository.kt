package com.zettl.clockwork.domain.lesson

class LessonRepository {
    private val lessons = listOf(
        Lesson(id = "1", levelCount = 5),
        Lesson(id = "2", levelCount = 5),
        Lesson(id = "3", levelCount = 5),
        Lesson(id = "4", levelCount = 5),
        Lesson(id = "5", levelCount = 6),
        Lesson(id = "6", levelCount = 5),
        Lesson(id = "7", levelCount = 5)
    )

    fun getAllLessons(): List<Lesson> = lessons

    fun getLesson(id: String): Lesson? = lessons.find { it.id == id }
}
