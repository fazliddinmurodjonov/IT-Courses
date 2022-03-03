package com.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.LessonFlowableAdapter
import com.example.androiddatabaselesson8onlinecourses.R
import com.example.androiddatabaselesson8onlinecourses.databinding.FragmentAddLessonBinding
import com.example.room.database.CourseDatabase
import com.example.room.entity.Lesson
import com.example.room.entity.Module
import com.util.Empty
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.function.Consumer

class AddLessonFragment : Fragment(R.layout.fragment_add_lesson) {
    private val binding: FragmentAddLessonBinding by viewBinding()
    lateinit var courseDatabase: CourseDatabase
    lateinit var module: Module
    lateinit var lessonFlowableAdapter: LessonFlowableAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        with(binding)
        {
            val moduleId = arguments?.getInt("moduleId")
            courseDatabase = CourseDatabase.getInstance(requireContext())
            module = courseDatabase.moduleDao().getModuleById(moduleId!!)
            loadActionBar(module.moduleName!!)
            val course = courseDatabase.courseDao().getCourseById(module.moduleCourseId!!)
            lessonFlowableAdapter = LessonFlowableAdapter(course.courseImage!!)
            lessonRV.adapter = lessonFlowableAdapter
            courseDatabase.lessonDao().getAllLessonFlowable(moduleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(@SuppressLint("NewApi")
                object : Consumer<List<Lesson>>,
                    @NonNull io.reactivex.rxjava3.functions.Consumer<List<Lesson>> {
                    override fun accept(t: List<Lesson>) {
                        lessonFlowableAdapter.submitList(t)
                    }

                },
                    @SuppressLint("NewApi")
                    object : Consumer<Throwable>,
                        @NonNull io.reactivex.rxjava3.functions.Consumer<Throwable> {
                        override fun accept(t: Throwable) {

                        }
                    })
            lessonAdapterProcess()
            addLesson.setOnClickListener {
                val name = lessonName.text.toString()
                val information = lessonInformation.text.toString()
                val number = lessonNumber.text.toString()
                val nameEmpty = Empty.empty(name)
                val informationEmpty = Empty.empty(information)
                val numberEmpty = Empty.empty(number)
                val nameSpace = Empty.space(name)
                val informationSpace = Empty.space(information)
                val numberSpace = Empty.space(number)
                val empty = nameEmpty && informationEmpty && numberEmpty
                val space = nameSpace && informationSpace && numberSpace
                val lessonList = courseDatabase.lessonDao().getAllLesson(moduleId)
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

                if (empty && space && unique) {
                    if (module.moduleLessonQuantity != null) {
                        module.moduleLessonQuantity = module.moduleLessonQuantity!! + 1
                    } else {
                        module.moduleLessonQuantity = 1
                    }
                    Observable.fromCallable {
                        val lesson =
                            Lesson(name, information, number.toInt(), course.courseId, moduleId)
                        courseDatabase.lessonDao().insertLesson(lesson)
                    }.subscribe()

                    courseDatabase.moduleDao().updateModule(module)
                    closeKeyboard()
                    lessonName.text?.clear()
                    lessonInformation.text?.clear()
                    lessonNumber.text?.clear()
                }
            }

        }
    }

    private fun lessonAdapterProcess() {
        lessonFlowableAdapter.setEditClickListener { lessonId ->
            val bundleOf = bundleOf("lessonId" to lessonId)
            findNavController().navigate(R.id.editLessonFragment, bundleOf)
        }
        lessonFlowableAdapter.setDeleteClickListener { lesson ->
            val dialog = AlertDialog.Builder(requireContext())
            dialog.setMessage("Do you agree that lesson will be deleted ?")
            dialog.setPositiveButton("Yes"
            ) { dialog, which ->
                courseDatabase.lessonDao().deleteLesson(lesson)
                Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
                module.moduleLessonQuantity = module.moduleLessonQuantity!! - 1
                courseDatabase.moduleDao().updateModule(module)
                dialog.dismiss()
            }
            dialog.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun loadActionBar(courseName: String) {
        (activity as AppCompatActivity).supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_arrow)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.title = courseName

    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun closeKeyboard() {
        val inputManager =
            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }


}