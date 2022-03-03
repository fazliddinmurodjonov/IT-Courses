package com.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.ModuleAdapter
import com.example.androiddatabaselesson8onlinecourses.R
import com.example.androiddatabaselesson8onlinecourses.databinding.FragmentCourseBinding
import com.example.room.database.CourseDatabase
import com.example.room.entity.Module

class CourseFragment : Fragment(R.layout.fragment_course) {
    private val binding: FragmentCourseBinding by viewBinding()
    lateinit var moduleAdapter: ModuleAdapter
    lateinit var courseDatabase: CourseDatabase
    lateinit var list: ArrayList<Module>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding)
        {
            val courseId = arguments?.getInt("courseId")
            list = ArrayList()
            courseDatabase = CourseDatabase.getInstance(requireContext())
            list.addAll(courseDatabase.moduleDao().getAllModules(courseId!!))
            val course = courseDatabase.courseDao().getCourseById(courseId)
            courseName.text = course.courseName
            moduleAdapter = ModuleAdapter(list, course.courseImage!!)
            moduleRV.adapter = moduleAdapter
            moduleAdapter.setOnItemClickListener { moduleId ->
                val bundleOf = bundleOf("moduleId" to moduleId)
                findNavController().navigate(R.id.moduleFragment, bundleOf)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar!!.show()

    }
}
