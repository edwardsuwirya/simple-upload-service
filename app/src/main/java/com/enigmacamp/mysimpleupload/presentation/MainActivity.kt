package com.enigmacamp.mysimpleupload.presentation

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.enigmacamp.mysimpleupload.data.api.RetrofitInstance
import com.enigmacamp.mysimpleupload.data.repository.PostRepository
import com.enigmacamp.mysimpleupload.databinding.ActivityMainBinding
import com.enigmacamp.mysimpleupload.utils.MediaPath
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    var idCardImagePath: File? = null
    var selfieImagePath: File? = null

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) Log.i(TAG, "onRequestPermissionsResult: Granted")
            else Log.i(TAG, "onRequestPermissionsResult: Denied")
        }

    private val openImageGalleryForIdCard =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            Log.d(TAG, "onActivityResult: $imageUri")
            binding.idCardImageView.setImageURI(imageUri)
            imageUri?.let {
                val file = MediaPath.getRealPathFromURI(contentResolver, imageUri)
                file?.let {
                    idCardImagePath = File(it)
                }
            }
        }
    private val openImageGalleryForSelfie =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            Log.d(TAG, "onActivityResult: $imageUri")
            binding.selfieImageView.setImageURI(imageUri)
            imageUri?.let {
                val file = MediaPath.getRealPathFromURI(contentResolver, imageUri)
                file?.let {
                    selfieImagePath = File(it)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()

        setupPermission()

        binding.apply {
            pickIdCardButton.setOnClickListener {
                openImageGalleryForIdCard.launch("image/*")
            }
            pickSelfieButton.setOnClickListener {
                openImageGalleryForSelfie.launch("image/*")
            }

            uploadButton.setOnClickListener {
                viewModel.upload(listOf(idCardImagePath, selfieImagePath), 1)
            }
        }
    }

    private fun setupPermission() {
        requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val repository = PostRepository(RetrofitInstance.postApi)
                return MainActivityViewModel(repository) as T
            }

        }).get(MainActivityViewModel::class.java)
    }

    companion object {
        const val TAG = "MainActivity"
    }
}