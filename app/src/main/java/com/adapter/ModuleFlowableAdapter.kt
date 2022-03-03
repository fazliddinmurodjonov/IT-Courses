package com.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddatabaselesson8onlinecourses.databinding.ItemModuleAddBinding
import com.example.room.entity.Module


class ModuleFlowableAdapter(var imagePath: String) :
    ListAdapter<Module, ModuleFlowableAdapter.ViewHolder>(MyDiffUtil()) {
    lateinit var editAdapter: EditClickListener
    lateinit var deleteAdapter: DeleteClickListener
    lateinit var itemAdapter: OnItemClickListener


    fun interface EditClickListener {
        fun onClick(moduleId: Int)
    }

    fun interface DeleteClickListener {
        fun onClick(module: Module)
    }

    fun interface OnItemClickListener {
        fun onClick(moduleId: Int)
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

    inner class ViewHolder(var binding: ItemModuleAddBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(module: Module) {
            binding.moduleName.text = module.moduleName
            binding.courseImage.setImageURI(Uri.parse(imagePath))
            binding.moduleNumber.text = module.moduleNumber.toString()
            binding.root.setOnClickListener {
                itemAdapter.onClick(module.moduleId!!)
            }
            binding.moduleEdit.setOnClickListener {
                editAdapter.onClick(module.moduleId!!)
            }
            binding.moduleDelete.setOnClickListener {
                deleteAdapter.onClick(module)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemModuleAddBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class MyDiffUtil : DiffUtil.ItemCallback<Module>() {
        override fun areItemsTheSame(oldItem: Module, newItem: Module): Boolean {
            return oldItem.moduleId == newItem.moduleId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Module, newItem: Module): Boolean {
            return oldItem == newItem
        }

    }
}