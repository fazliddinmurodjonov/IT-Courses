package com.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddatabaselesson8onlinecourses.R
import com.example.androiddatabaselesson8onlinecourses.databinding.ItemCourseModuleBinding
import com.example.room.entity.Module

class ModuleInCourseAdapter(var list: ArrayList<Module>) :
    RecyclerView.Adapter<ModuleInCourseAdapter.ModuleInCourseViewHolder>() {
    lateinit var moduleAdapter: OnClickModule

    fun interface OnClickModule {
        fun onClick(moduleId: Int)
    }

    fun setOnClickModule(listener: OnClickModule) {
        moduleAdapter = listener
    }

    inner class ModuleInCourseViewHolder(var binding: ItemCourseModuleBinding) :

        RecyclerView.ViewHolder(binding.root) {
        fun onBind(module: Module) {
            binding.moduleButton.text = module.moduleName
            binding.moduleButton.setOnClickListener {
                moduleAdapter.onClick(module.moduleId!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleInCourseViewHolder {
        return ModuleInCourseViewHolder(ItemCourseModuleBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ModuleInCourseViewHolder, position: Int) {
        val module = list[position]
        holder.onBind(module)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}