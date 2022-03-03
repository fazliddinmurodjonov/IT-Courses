package com.example.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.room.entity.CourseWithModule

@Dao
interface CourseWithModule {
    @Transaction
    @Query("select *from Course")
    fun getCourseWithModules(): List<CourseWithModule>

}