package com.example.room.dao

import androidx.room.*
import com.example.room.entity.Lesson
import io.reactivex.rxjava3.core.Flowable

@Dao
interface LessonDao {
    @Insert
    fun insertLesson(lesson: Lesson)

    @Update
    fun updateLesson(lesson: Lesson)

    @Delete
    fun deleteLesson(lesson: Lesson)

    @Query("select *from Lesson where lessonModuleId = :moduleId")
    fun getAllLessonFlowable(moduleId: Int): Flowable<List<Lesson>>

    @Query("select *from Lesson where lessonModuleId = :moduleId")
    fun getAllLesson(moduleId: Int): List<Lesson>

    @Query("select *from Lesson where lessonId = :lessonId")
    fun getLessonById(lessonId: Int): Lesson

}