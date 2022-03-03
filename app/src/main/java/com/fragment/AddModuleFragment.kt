package com.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.ModuleFlowableAdapter
import com.example.androiddatabaselesson8onlinecourses.R
import com.example.androiddatabaselesson8onlinecourses.databinding.FragmentAddModuleBinding
import com.example.room.database.CourseDatabase
import com.example.room.entity.Module
import com.util.Empty
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.function.Consumer


class AddModuleFragment : Fragment(R.layout.fragment_add_module) {
    private val binding: FragmentAddModuleBinding by viewBinding()
    lateinit var courseDatabase: CourseDatabase
    lateinit var moduleFlowableAdapter: ModuleFlowableAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        with(binding)
        {
            val courseId = arguments?.getInt("courseId")
            courseDatabase = CourseDatabase.getInstance(requireContext())
            val course = courseDatabase.courseDao().getCourseById(courseId!!)
            loadActionBar(course.courseName!!)
            moduleFlowableAdapter = ModuleFlowableAdapter(course.courseImage!!)
            courseDatabase.moduleDao().getAllModulesFlowable(courseId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(@SuppressLint("NewApi")
                object : Consumer<List<Module>>,
                    @NonNull io.reactivex.rxjava3.functions.Consumer<List<Module>> {

                    override fun accept(t: List<Module>) {
                        moduleFlowableAdapter.submitList(t)
                    }

                },
                    @SuppressLint("NewApi")
                    object : Consumer<Throwable>,
                        @NonNull io.reactivex.rxjava3.functions.Consumer<Throwable> {
                        override fun accept(t: Throwable) {

                        }
                    })
            moduleRV.adapter = moduleFlowableAdapter
            moduleAdapterProcess()
            addModule.setOnClickListener {
                val moduleName = moduleName.text.toString()
                val moduleNumber = moduleNumber.text.toString()
                val nameEmpty = Empty.empty(moduleName)
                val numberEmpty = Empty.empty(moduleNumber)
                val nameSpace = Empty.space(moduleName)
                val numberSpace = Empty.space(moduleNumber)
                val moduleList = courseDatabase.moduleDao().getAllModules(courseId)
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

                if (nameEmpty && numberEmpty && nameSpace && numberSpace && unique) {
                    Observable.fromCallable {
                        val module =
                            Module(moduleName, moduleNumber.toInt(), course.courseName, 0, courseId)
                        courseDatabase.moduleDao().insertModule(module)
                    }.subscribe()
                    binding.moduleName.text?.clear()
                    binding.moduleNumber.text?.clear()
                    closeKeyboard()
                }
            }
        }

    }

    private fun moduleAdapterProcess() {
        moduleFlowableAdapter.setOnItemClickListener { moduleId ->
            val bundleOf = bundleOf("moduleId" to moduleId)
            findNavController().navigate(R.id.addLessonFragment, bundleOf)
        }
        moduleFlowableAdapter.setEditClickListener { moduleId ->
            val bundleOf = bundleOf("moduleId" to moduleId)
            findNavController().navigate(R.id.editModuleFragment, bundleOf)
        }
        moduleFlowableAdapter.setDeleteClickListener { module ->
            val dialog = AlertDialog.Builder(requireContext())
            dialog.setMessage("This module contains some lessons.Do you agree that it will be deleted with the lessons ?")
            dialog.setPositiveButton("Yes") { dialog, which ->
                courseDatabase.moduleDao().deleteModule(module)
                val allLesson = courseDatabase.lessonDao().getAllLesson(module.moduleId!!)
                for (lesson in allLesson) {
                    courseDatabase.lessonDao().deleteLesson(lesson)
                    Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            dialog.setNegativeButton("No"
            ) { dialog, which ->
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