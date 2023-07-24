package com.example.geolocation.activities.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.geolocation.classes.profile.UserProfile

class UserViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is user Fragment"
    }
    val text: LiveData<String> = _text
}