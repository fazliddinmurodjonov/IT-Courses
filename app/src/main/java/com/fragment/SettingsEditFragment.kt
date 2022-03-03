package com.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androiddatabaselesson8onlinecourses.BuildConfig
import com.example.androiddatabaselesson8onlinecourses.R
import com.example.androiddatabaselesson8onlinecourses.databinding.CustomPermissionDialogBinding
import com.example.androiddatabaselesson8onlinecourses.databinding.FragmentSettingsEditBinding
import com.example.room.database.CourseDatabase
import com.example.room.entity.Course
import com.util.Empty
import io.reactivex.rxjava3.core.Observable
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class SettingsEditFragment : Fragment(R.layout.fragment_settings_edit) {
    var imagePath: String? = null
    var photoURI: Uri? = null
    lateinit var courseDatabase: CourseDatabase
    private val binding: FragmentSettingsEditBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val courseId = arguments?.getInt("courseId")
        courseDatabase = CourseDatabase.getInstance(requireContext())
        var course = courseDatabase.courseDao().getCourseById(courseId!!)
        loadActionBar(course.courseName!!)
        with(binding)
        {
            courseImage.setImageURI(Uri.parse(course.courseImage))
            imagePath = course.courseImage
            courseName.setText(course.courseName)
            courseImage.setOnClickListener {
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

            saveEditedCourseName.setOnClickListener {
                val courseName = courseName.text.toString()
                val courseNameBol = Empty.empty(courseName)
                val courseNameSpace = Empty.space(courseName)
                var unique = true
                var courseItem = Course()
                val courseList = courseDatabase.courseDao().getAllCourse()
                for (item in courseList) {
                    if (item.courseName == courseName) {
                        unique = false
                        courseItem = item
                        break
                    }
                }
                if (courseItem.courseId == course.courseId) {
                    unique = true
                }

                if (courseNameBol && courseNameSpace && imagePath != null && unique) {

                    course.courseName = binding.courseName.text.toString()
                    course.courseImage = imagePath
                    courseDatabase.courseDao().updateCourse(course)

                    closeKeyboard()
                    findNavController().popBackStack()
                }

            }

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
        inputManager.hideSoftInputFromWindow(view!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS)
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
                binding.courseImage.setImageURI(photoURI)
                val openInputStream =
                    requireActivity().contentResolver?.openInputStream(photoURI!!)
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
            binding.courseImage.setImageURI(uri)
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