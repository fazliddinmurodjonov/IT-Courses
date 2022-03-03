package com.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddatabaselesson8onlinecourses.databinding.ItemLessonBinding
import com.example.room.entity.Lesson

class LessonAdapter(val list: ArrayList<Lesson>) :
    RecyclerView.Adapter<LessonAdapter.ViewHolder>() {
    lateinit var itemClick: OnItemClickListener

    fun interface OnItemClickListener {
        fun onClick(lessonId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClick = listener
    }

    inner class ViewHolder(var binding: ItemLessonBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(lesson: Lesson) {
            with(binding)
            {
                lessonButton.text = lesson.lessonNumber.toString()
                lessonButton.setOnClickListener {
                    itemClick.onClick(lesson.lessonId!!)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLessonBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lesson = list[position]
        holder.onBind(lesson)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}