package com.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.CourseAdapter
import com.example.androiddatabaselesson8onlinecourses.BuildConfig
import com.example.androiddatabaselesson8onlinecourses.R
import com.example.androiddatabaselesson8onlinecourses.databinding.CustomPermissionDialogBinding
import com.example.androiddatabaselesson8onlinecourses.databinding.FragmentSettingsBinding
import com.example.room.database.CourseDatabase
import com.example.room.entity.Course
import com.util.Empty
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.Consumer

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val binding: FragmentSettingsBinding by viewBinding()
    lateinit var courseDatabase: CourseDatabase
    lateinit var courseAdapter: CourseAdapter
    var imagePath: String? = null
    var photoURI: Uri? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadActionBar()
        with(binding)
        {
            courseDatabase = CourseDatabase.getInstance(requireContext())
            courseAdapter = CourseAdapter()
            courseDatabase.courseDao().getAllCourses()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(@SuppressLint("NewApi")
                object : Consumer<List<Course>>,
                    @NonNull io.reactivex.rxjava3.functions.Consumer<List<Course>> {
                    override fun accept(t: List<Course>) {
                        courseAdapter.submitList(t)
                    }

                },
                    @SuppressLint("NewApi")
                    object : Consumer<Throwable>,
                        @NonNull io.reactivex.rxjava3.functions.Consumer<Throwable> {
                        override fun accept(t: Throwable) {
                        }

                    }
                )
            settingCourseRV.adapter = courseAdapter
            courseAdapterTasks()
            placeHolder.setOnClickListener {
                val dialog = Dialog(requireActivity())
                val dialogView =
                    CustomPermissionDialogBinding.inflate(LayoutInflater.from(requireContext()),
                        null,
                        false)
                dialog.setContentView(dialogView.root)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialogView.camera.setOnClickListener {
                    val imageFile = createImageFile()
                    photoURI =
                        FileProvider.getUriForFile(requireContext(),
                            BuildConfig.APPLICATION_ID,
                            imageFile)
                    getTakeImageContent.launch(photoURI)
                    dialog.dismiss()
                }
                dialogView.gallery.setOnClickListener {
                    pickImageFromGalleryNew()
                    dialog.dismiss()

                }
                dialog.show()
            }
            addCourse.setOnClickListener {
                val courseName = courseName.text.toString()
                val courseNameBol = Empty.empty(courseName)
                val courseNameSpace = Empty.space(courseName)
                var unique = true
                val courseList = courseDatabase.courseDao().getAllCourse()
                for (course in courseList) {
                    if (course.courseName == courseName) {
                        unique = false
                        break
                    }
                }
                if (courseNameBol && courseNameSpace && imagePath != null && unique) {
                    Observable.fromCallable {
                        val course = Course(courseName, imagePath)
                        courseDatabase.courseDao().insertCourse(course)
                    }.subscribe()
                    binding.courseName.text?.clear()
                    imagePath = ""
                    binding.placeHolder.setImageResource(R.drawable.place_holder)
                    closeKeyboard()
                }
            }


        }

    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun closeKeyboard() {
        val inputManager =
            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun courseAdapterTasks() {
        courseAdapter.setEditClickListener { courseId ->
            val bundleOf = bundleOf("courseId" to courseId)
            findNavController().navigate(R.id.settingEditFragment, bundleOf)
        }
        courseAdapter.setDeleteClickListener { course ->
            val dialog = AlertDialog.Builder(requireContext())
            dialog.setMessage("This course contains some modules.Do you agree that it will be deleted with the modules?")
            dialog.setPositiveButton("Yes") { dialog, which ->
                courseDatabase.courseDao().deleteCourse(course)
                val moduleWithLesson =
                    courseDatabase.moduleWithLesson().getModuleWithLessons(course.courseId!!)
                for (moduleWithLessons in moduleWithLesson) {
                    courseDatabase.moduleDao().deleteModule(moduleWithLessons.module)
                    val lessons = moduleWithLessons.lesson
                    for (lesson in lessons) {
                        courseDatabase.lessonDao().deleteLesson(lesson)
                    }
                }
                Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            dialog.setNegativeButton("No"
            ) { dialog, which ->
                dialog.dismiss()
            }
            dialog.show()

        }
        courseAdapter.setOnItemClickListener { courseId ->
            val bundleOf = bundleOf("courseId" to courseId)
            findNavController().navigate(R.id.addModuleFragment, bundleOf)
        }

    }

    private fun loadActionBar() {
        (activity as AppCompatActivity).supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_arrow)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val externalFilesDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", externalFilesDir).apply {

        }
    }

    private val getTakeImageContent =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                binding.placeHolder.setImageURI(photoURI)
                val openInputStream = requireActivity().contentResolver?.openInputStream(photoURI!!)
                var numberText = "1234567890"
                val toCharArray = numberText.toCharArray()
                toCharArray.shuffle()
                var imageName: String = ""
                for (c in toCharArray) {
                    imageName += c
                }
                val file = File(requireActivity().filesDir, "$imageName.jpg")
                val fileOutputStream = FileOutputStream(file)
                openInputStream?.copyTo(fileOutputStream)
                openInputStream?.close()
                fileOutputStream.close()
                imagePath = file.absolutePath
            }
        }

    private fun pickImageFromGalleryNew() {
        getImageContent.launch("image/*")
    }

    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri ?: return@registerForActivityResult
            binding.placeHolder.setImageURI(uri)
            val openInputStream = requireActivity().contentResolver?.openInputStream(uri)
            var numberText = "1234567890"
            val toCharArray = numberText.toCharArray()
            toCharArray.shuffle()

            var imageName: String = ""
            for (c in toCharArray) {
                imageName += c
            }
            val file = File(requireActivity().filesDir, "$imageName.jpg")
            val fileOutputStream = FileOutputStream(file)
            openInputStream?.copyTo(fileOutputStream)
            openInputStream?.close()
            fileOutputStream.close()
            val absolutePath = file.absolutePath
            imagePath = absolutePath
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().popBackStack()
        return super.onOptionsItemSelected(item)
    }

}