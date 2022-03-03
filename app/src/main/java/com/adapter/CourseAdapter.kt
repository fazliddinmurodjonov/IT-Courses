package com.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddatabaselesson8onlinecourses.databinding.ItemCourseInSettingsBinding
import com.example.room.entity.Course

class CourseAdapter : ListAdapter<Course, CourseAdapter.ViewHolder>(MyDiffUtil()) {
    lateinit var editAdapter: EditClickListener
    lateinit var deleteAdapter: DeleteClickListener
    lateinit var itemAdapter: OnItemClickListener


    fun interface EditClickListener {
        fun onClick(courseId: Int)
    }

    fun interface DeleteClickListener {
        fun onClick(course: Course)
    }
    fun interface OnItemClickListener {
        fun onClick(courseId: Int)
    }

    fun setEditClickListener(listener: EditClickListener) {
        editAdapter = listener
    }

    fun setDeleteClickListener(listener: DeleteClickListener) {
        deleteAdapter = listener
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemAdapter = listener
    }
    inner class ViewHolder(var binding: ItemCourseInSettingsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(course: Course) {
            binding.courseName.text = course.courseName
            binding.courseImage.setImageURI(Uri.parse(course.courseImage))
            binding.root.setOnClickListener {
                itemAdapter.onClick(course.courseId!!)
            }
            binding.courseEdit.setOnClickListener {
                editAdapter.onClick(course.courseId!!)
            }
            binding.courseDelete.setOnClickListener {
                deleteAdapter.onClick(course)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCourseInSettingsBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class MyDiffUtil : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.courseId == newItem.courseId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem == newItem
        }

    }
}