package com.enigmacamp.mysimpleupload.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enigmacamp.mysimpleupload.data.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class MainActivityViewModel(private val repository: PostRepository) : ViewModel() {

    fun upload(imageFile: List<File?>, postId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.uploadFile(imageFile, postId)
        }
    }
}