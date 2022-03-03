package com.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddatabaselesson8onlinecourses.databinding.ItemLessonAddBinding
import com.example.room.entity.Lesson

class LessonFlowableAdapter(var imagePath: String) :
    ListAdapter<Lesson, LessonFlowableAdapter.ViewHolder>(MyDiffUtil()) {
    lateinit var editAdapter: EditClickListener
    lateinit var deleteAdapter: DeleteClickListener


    fun interface EditClickListener {
        fun onClick(lessonId: Int)
    }

    fun interface DeleteClickListener {
        fun onClick(lesson: Lesson)
    }



    fun setEditClickListener(listener: EditClickListener) {
        editAdapter = listener
    }

    fun setDeleteClickListener(listener: DeleteClickListener) {
        deleteAdapter = listener
    }


    inner class ViewHolder(var binding: ItemLessonAddBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(lesson: Lesson) {
            binding.lessonName.text = lesson.lessonName
            binding.lessonAbout.text = lesson.lessonInformation
            binding.courseImage.setImageURI(Uri.parse(imagePath))
            binding.moduleEdit.setOnClickListener {
                editAdapter.onClick(lesson.lessonId!!)
            }
            binding.moduleDelete.setOnClickListener {
                deleteAdapter.onClick(lesson)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLessonAddBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class MyDiffUtil : DiffUtil.ItemCallback<Lesson>() {
        override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem.lessonId == newItem.lessonId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem == newItem
        }

    }

}