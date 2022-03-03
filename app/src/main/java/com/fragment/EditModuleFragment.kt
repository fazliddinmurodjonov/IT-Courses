package com.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androiddatabaselesson8onlinecourses.R
import com.example.androiddatabaselesson8onlinecourses.databinding.FragmentEditModuleBinding
import com.example.room.database.CourseDatabase
import com.example.room.entity.Module
import com.util.Empty


class EditModuleFragment : Fragment(R.layout.fragment_edit_module) {
    private val binding: FragmentEditModuleBinding by viewBinding()
    lateinit var courseDatabase: CourseDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val moduleId = arguments?.getInt("moduleId")
        courseDatabase = CourseDatabase.getInstance(requireContext())
        val module = courseDatabase.moduleDao().getModuleById(moduleId!!)
        loadActionBar(module.moduleName!!)
        with(binding)
        {

            moduleName.setText(module.moduleName)
            moduleNumber.setText(module.moduleNumber.toString())
            saveEditedModule.setOnClickListener {
                val moduleName = moduleName.text.toString()
                val moduleNumber = moduleNumber.text.toString()
                val nameEmpty = Empty.empty(moduleName)
                val numberEmpty = Empty.empty(moduleNumber)
                val nameSpace = Empty.space(moduleName)
                val numberSpace = Empty.space(moduleNumber)
                val moduleList = courseDatabase.moduleDao().getAllModules(module.moduleCourseId!!)
                var uniqueName = true
                var uniqueNumber = true

                for (module in moduleList) {
                    if (module.moduleName == moduleName) {
                        uniqueName = false
                    }
                    if (module.moduleNumber.toString() == moduleNumber) {
                        uniqueNumber = false
                    }
                }
                var unique = uniqueName && uniqueNumber

                if (module.moduleName == moduleName && module.moduleNumber.toString() == moduleNumber ||
                    (module.moduleName == moduleName && uniqueNumber) || (uniqueName && module.moduleNumber.toString() == moduleNumber)
                ) {
                    unique = true
                }

                if (nameEmpty && numberEmpty && nameSpace && numberSpace && unique) {
                    module.moduleName = moduleName
                    module.moduleNumber = moduleNumber.toInt()
                    courseDatabase.moduleDao().updateModule(module)
                    findNavController().popBackStack()
                }


            }
        }

    }

    private fun loadActionBar(moduleName: String) {
        (activity as AppCompatActivity).supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_arrow)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.title = moduleName
    }


}