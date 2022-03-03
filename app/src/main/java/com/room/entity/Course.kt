package com.example.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Course {
    @PrimaryKey(autoGenerate = true)
    var courseId: Int? = null

    @ColumnInfo(name = "courseName")
    var courseName: String? = null

    @ColumnInfo(name = "courseImage")

    var courseImage: String? = null

    constructor()
    constructor(courseName: String?, courseImage: String?) {
        this.courseName = courseName
        this.courseImage = courseImage
    }

}
