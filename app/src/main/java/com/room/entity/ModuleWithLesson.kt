package com.example.room.entity

import androidx.room.Embedded
import androidx.room.Relation

data class  ModuleWithLesson (
    @Embedded
    val module: Module,
    @Relation(
        parentColumn = "moduleId",
        entityColumn = "lessonModuleId"
    )
    val lesson: List<Lesson>,
)