package com.example.room.dao

import androidx.room.*
import com.example.room.entity.Module
import io.reactivex.rxjava3.core.Flowable

@Dao
interface ModuleDao {

    @Insert
    fun insertModule(module: Module)

    @Update
    fun updateModule(module: Module)

    @Delete
    fun deleteModule(module: Module)

    @Query("select *from Module where moduleCourseId = :courseId")
    fun getAllModulesFlowable(courseId: Int): Flowable<List<Module>>

    @Query("select *from Module where moduleCourseId = :courseId")
    fun getAllModules(courseId: Int): List<Module>

    @Query("select *from Module where moduleId = :moduleId")
    fun getModuleById(moduleId: Int): Module
}