package com.example.room.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CourseWithLesson(
    @Embedded
    val course: Course,
    @Relation(
        parentColumn = "courseId",
        entityColumn = "lessonCourseId"
    )
    val lesson: List<Lesson>,
)