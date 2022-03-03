package com.example.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
 class Lesson{
    @PrimaryKey(autoGenerate = true)
    var lessonId: Int? = null
    @ColumnInfo(name = "lessonName")
    var lessonName: String? = null

    @ColumnInfo(name = "lessonInformation")
    var lessonInformation: String? = null

    @ColumnInfo(name = "lessonNumber")
    var lessonNumber: Int? = null

    @ColumnInfo(name = "lessonCourseId")
    var lessonCourseId: Int? = null

    @ColumnInfo(name = "lessonModuleId")
    var lessonModuleId: Int? = null

    constructor()
    constructor(
        lessonName: String?,
        lessonInformation: String?,
        lessonNumber: Int?,
        lessonCourseId: Int?,
        lessonModuleId: Int?,
    ) {
        this.lessonName = lessonName
        this.lessonInformation = lessonInformation
        this.lessonNumber = lessonNumber
        this.lessonCourseId = lessonCourseId
        this.lessonModuleId = lessonModuleId
    }


}