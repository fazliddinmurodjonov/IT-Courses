package com.example.room.dao

import androidx.room.*
import com.example.room.entity.Course
import io.reactivex.rxjava3.core.Flowable

@Dao
interface CourseDao {
    @Insert
    fun insertCourse(course: Course)

    @Update
    fun updateCourse(course: Course)

    @Delete
    fun deleteCourse(course: Course)

    @Query("select *from Course")
    fun getAllCourses(): Flowable<List<Course>>

    @Query("select *from Course")
    fun getAllCourse(): List<Course>

    @Query("select *from Course where courseId = :courseId")
    fun getCourseById(courseId: Int): Course
}