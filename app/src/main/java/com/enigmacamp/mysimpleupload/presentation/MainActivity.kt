package com.enigmacamp.mysimpleupload.presentation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.enigmacamp.mysimpleupload.R
import com.enigmacamp.mysimpleupload.data.api.RetrofitInstance
import com.enigmacamp.mysimpleupload.data.repository.PostRepository
import com.enigmacamp.mysimpleupload.databinding.ActivityMainBinding
import com.enigmacamp.mysimpleupload.utils.MediaPath
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()

        binding.apply {
            uploadButton.setOnClickListener {
                setupPermission()
                val gallery = Intent(Intent.ACTION_PICK)
                gallery.type = "image/*"
                startActivityForResult(gallery, REQUEST_CODE)
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val repository = PostRepository(RetrofitInstance.postApi)
                return MainActivityViewModel(repository) as T
            }

        }).get(MainActivityViewModel::class.java)
    }

    private fun setupPermission() {
        val permission =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "onRequestPermissionsResult: Denied")
                } else {
                    Log.i(TAG, "onRequestPermissionsResult: Granted")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            val imageUri = data?.data
            Log.d(TAG, "onActivityResult: $imageUri")
            binding.imageView.setImageURI(imageUri)
            imageUri?.let {
                val file = MediaPath.getRealPathFromURI(contentResolver, imageUri)
                file?.let {
                    viewModel.upload(File(it), 1)
                }
            }
        }
    }

    companion object {
        const val REQUEST_CODE = 1000
        const val TAG = "MainActivity"
    }
}