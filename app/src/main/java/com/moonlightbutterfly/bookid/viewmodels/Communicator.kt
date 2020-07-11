package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Communicator @Inject constructor() {
    val message: LiveData<String> get() = _message
    private var _message: MutableLiveData<String> = MutableLiveData()

    fun postMessage(message: String) = run { _message.value = message }
}