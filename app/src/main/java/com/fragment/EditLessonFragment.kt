package com.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androiddatabaselesson8onlinecourses.R
import com.example.androiddatabaselesson8onlinecourses.databinding.FragmentEditLessonBinding
import com.example.room.database.CourseDatabase
import com.example.room.entity.Lesson
import com.util.Empty
import io.reactivex.rxjava3.core.Observable

class EditLessonFragment : Fragment(R.layout.fragment_edit_lesson) {
    lateinit var courseDatabase: CourseDatabase
    private val binding: FragmentEditLessonBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val lessonId = arguments?.getInt("lessonId")
        courseDatabase = CourseDatabase.getInstance(requireContext())
        var lesson = courseDatabase.lessonDao().getLessonById(lessonId!!)
        loadActionBar(lesson.lessonName!!)
        with(binding)
        {
            lessonNameEdited.setText(lesson.lessonName)
            lessonInformationEdited.setText(lesson.lessonInformation)
            lessonNumberEdited.setText(lesson.lessonNumber.toString())
            saveEditedLesson.setOnClickListener {
                val name = lessonNameEdited.text.toString()
                val information = lessonInformationEdited.text.toString()
                val number = lessonNumberEdited.text.toString()
                val nameEmpty = Empty.empty(name)
                val informationEmpty = Empty.empty(information)
                val numberEmpty = Empty.empty(number)
                val nameSpace = Empty.space(name)
                val informationSpace = Empty.space(information)
                val numberSpace = Empty.space(number)
                val empty = nameEmpty && informationEmpty && numberEmpty
                val space = nameSpace && informationSpace && numberSpace

                val lessonList = courseDatabase.lessonDao().getAllLesson(lesson.lessonModuleId!!)
                var uniqueName = true
                var uniqueNumber = true

                for (lesson in lessonList) {
                    if (lesson.lessonName == name) {
                        uniqueName = false
                    }
                    if (lesson.lessonNumber.toString() == number) {
                        uniqueNumber = false
                    }
                }
                var unique = uniqueName && uniqueNumber

                if (lesson.lessonName == name && lesson.lessonNumber.toString() == number ||
                    (lesson.lessonName  == name && uniqueNumber) || (uniqueName && lesson.lessonNumber.toString() == number)
                ) {
                    unique = true
                }

                if (empty && space && unique) {
                    lesson.lessonName = name
                    lesson.lessonInformation = information
                    lesson.lessonNumber = number.toInt()
                    courseDatabase.lessonDao().updateLesson(lesson)
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun loadActionBar(lessonName: String) {
        (activity as AppCompatActivity).supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_arrow)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.title = lessonName

    }
}

