package com.example.etelcom.ui.new_file

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewFileViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Client"
    }
    val text: LiveData<String> = _text
}