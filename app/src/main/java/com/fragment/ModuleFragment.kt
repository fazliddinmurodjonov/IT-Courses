package com.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.LessonAdapter
import com.example.androiddatabaselesson8onlinecourses.R
import com.example.androiddatabaselesson8onlinecourses.databinding.FragmentModuleBinding
import com.example.room.database.CourseDatabase
import com.example.room.entity.Lesson


class ModuleFragment : Fragment(R.layout.fragment_module) {
    private val binding: FragmentModuleBinding by viewBinding()
    lateinit var courseDatabase: CourseDatabase
    lateinit var lessonAdapter: LessonAdapter
    lateinit var lessonList: ArrayList<Lesson>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val moduleId = arguments?.getInt("moduleId")
        courseDatabase = CourseDatabase.getInstance(requireContext())
        val module = courseDatabase.moduleDao().getModuleById(moduleId!!)
        binding.moduleName.text = module.moduleName
        lessonList = ArrayList()
        lessonList.addAll(courseDatabase.lessonDao().getAllLesson(moduleId))
        lessonAdapter = LessonAdapter(lessonList)
        binding.lessonRV.adapter = lessonAdapter
        lessonAdapter.setOnItemClickListener { lessonId ->
            val bundleOf = bundleOf("lessonId" to lessonId)
            findNavController().navigate(R.id.lessonFragment, bundleOf)
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
