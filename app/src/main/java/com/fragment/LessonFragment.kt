package com.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androiddatabaselesson8onlinecourses.R
import com.example.androiddatabaselesson8onlinecourses.databinding.FragmentLessonBinding
import com.example.room.database.CourseDatabase

class LessonFragment : Fragment(R.layout.fragment_lesson) {
    private val binding: FragmentLessonBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val lessonId = arguments?.getInt("lessonId")
        val courseDatabase = CourseDatabase.getInstance(requireContext())
        val lesson = courseDatabase.lessonDao().getLessonById(lessonId!!)
        with(binding)
        {
            lessonNumber.text = "Lesson ${lesson.lessonNumber}"
            lessonName.text = lesson.lessonName
            lessonText.text = lesson.lessonInformation
            back.setOnClickListener {
                findNavController().popBackStack()
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