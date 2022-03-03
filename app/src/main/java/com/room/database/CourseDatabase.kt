package com.example.room.database

import android.content.Context
import androidx.room.RoomDatabase
import com.example.room.entity.Course
import androidx.room.Database
import androidx.room.Room
import com.example.room.dao.*
import com.example.room.entity.Lesson
import com.example.room.entity.Module

@Database(entities = [Course::class, Lesson::class, Module::class], version = 1)
abstract class CourseDatabase : RoomDatabase() {

    abstract fun courseDao(): CourseDao
    abstract fun moduleDao(): ModuleDao
    abstract fun lessonDao(): LessonDao
    abstract fun courseWithLesson(): CourseWithLesson
    abstract fun courseWithModule(): CourseWithModule
    abstract fun moduleWithLesson(): ModuleWithLesson

    companion object {
        private var instance: CourseDatabase? = null

        @Synchronized
        fun getInstance(context: Context): CourseDatabase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context, CourseDatabase::class.java, "course_database")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
            }
            return instance!!
        }
    }
}