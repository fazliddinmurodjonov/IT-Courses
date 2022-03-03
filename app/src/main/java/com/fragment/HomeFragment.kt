package com.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.CourseModuleAdapter
import com.example.androiddatabaselesson8onlinecourses.R
import com.example.androiddatabaselesson8onlinecourses.databinding.FragmentHomeBinding
import com.example.room.database.CourseDatabase
import com.example.room.entity.CourseWithModule

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding: FragmentHomeBinding by viewBinding()
    lateinit var courseDatabase: CourseDatabase
    lateinit var courseModuleAdapter: CourseModuleAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding)
        {
            courseDatabase = CourseDatabase.getInstance(requireContext())
            val courseWithModulesList = courseDatabase.courseWithModule().getCourseWithModules()
            val list = ArrayList<CourseWithModule>()
            list.addAll(courseWithModulesList)
            courseModuleAdapter = CourseModuleAdapter(list)
            courseRV.adapter = courseModuleAdapter
            courseModuleAdapter.setOnItemClickListener { courseId ->
                val bundleOf = bundleOf("courseId" to courseId)
                findNavController().navigate(R.id.courseFragment, bundleOf)
            }
            courseModuleAdapter.setOnClickModule { moduleId ->
                val bundleOf = bundleOf("moduleId" to moduleId)
                findNavController().navigate(R.id.moduleFragment, bundleOf)
            }
            setting.setOnClickListener {
                val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate)
                setting.startAnimation(animation)
                findNavController().navigate(R.id.settingsFragment)
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