package com.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddatabaselesson8onlinecourses.databinding.ItemCourseModuleRvBinding
import com.example.room.entity.CourseWithModule
import com.example.room.entity.Module

class CourseModuleAdapter(var list: ArrayList<CourseWithModule>) :
    RecyclerView.Adapter<CourseModuleAdapter.CourseModuleViewHolder>() {
    lateinit var adapter: OnItemClickListener
    lateinit var moduleAdapter: OnClickModule

    fun interface OnClickModule {
        fun onClick(moduleId: Int)
    }

    fun setOnClickModule(listener: OnClickModule) {
        moduleAdapter = listener
    }

    fun interface OnItemClickListener {
        fun onClick(courseId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        adapter = listener
    }

    inner class CourseModuleViewHolder(var binding: ItemCourseModuleRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(courseWithModule: CourseWithModule) {
            with(binding)
            {
                courseName.text = courseWithModule.course.courseName
                var moduleList = ArrayList<Module>()
                moduleList.addAll(courseWithModule.module)
                val moduleInCourseAdapter = ModuleInCourseAdapter(moduleList)
                moduleRV.adapter = moduleInCourseAdapter
                moduleInCourseAdapter.setOnClickModule {
                    moduleAdapter.onClick(it)
                }
                binding.allLayout.setOnClickListener {
                    adapter.onClick(courseWithModule.course.courseId!!)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseModuleViewHolder {
        return CourseModuleViewHolder(ItemCourseModuleRvBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: CourseModuleViewHolder, position: Int) {
        val courseWithModule = list[position]
        holder.onBind(courseWithModule)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}