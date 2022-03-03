package com.example.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.room.entity.CourseWithLesson

@Dao
interface CourseWithLesson {
    @Transaction
    @Query("select * from Course where courseId = :courseId")
    fun getAllLessonsByCourse(courseId: Int): CourseWithLesson
}