package com.example.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Module {
    @PrimaryKey(autoGenerate = true)
    var moduleId: Int? = null

    @ColumnInfo(name = "moduleName")
    var moduleName: String? = null

    @ColumnInfo(name = "moduleNumber")

    var moduleNumber: Int? = null

    @ColumnInfo(name = "moduleCourseName")

    var moduleCourseName: String? = null

    @ColumnInfo(name = "moduleLessonQuantity")

    var moduleLessonQuantity: Int? = null

    @ColumnInfo(name = "moduleCourseId")

    var moduleCourseId: Int? = null

    constructor()
    constructor(
        moduleName: String?,
        moduleNumber: Int?,
        moduleCourseName: String?,
        moduleLessonQuantity: Int?,
        moduleCourseId: Int?,
    ) {
        this.moduleName = moduleName
        this.moduleNumber = moduleNumber
        this.moduleCourseName = moduleCourseName
        this.moduleLessonQuantity = moduleLessonQuantity
        this.moduleCourseId = moduleCourseId
    }

}