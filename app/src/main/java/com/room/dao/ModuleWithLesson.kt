package com.example.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.room.entity.ModuleWithLesson

@Dao
interface ModuleWithLesson {
    @Transaction
    @Query("select *from Module where moduleCourseId = :courseId")
    fun getModuleWithLessons(courseId: Int): List<ModuleWithLesson>
}