package com.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddatabaselesson8onlinecourses.databinding.ItemModuleBinding
import com.example.room.entity.Module


class ModuleAdapter(var list: ArrayList<Module>, var imagePath: String) :
    RecyclerView.Adapter<ModuleAdapter.ViewHolder>() {
    lateinit var adapter: OnItemClickListener

    fun interface OnItemClickListener {
        fun onClick(moduleId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        adapter = listener
    }

    inner class ViewHolder(var binding: ItemModuleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(module: Module) {
            with(binding)
            {
                courseImage.setImageURI(Uri.parse(imagePath))
                courseName.text = module.moduleCourseName
                moduleName.text = module.moduleName
                lessonQuantity.text = module.moduleLessonQuantity.toString()
                root.setOnClickListener {
                    adapter.onClick(module.moduleId!!)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemModuleBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val module = list[position]
        holder.onBind(module)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}