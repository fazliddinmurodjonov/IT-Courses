package com.example.room.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CourseWithModule(
    @Embedded
    val course: Course,
    @Relation(
        parentColumn = "courseId",
        entityColumn = "moduleCourseId"
    )
    val module: List<Module>,
)