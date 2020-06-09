package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val repository: InternalRepository): ViewModel() {

    private val _userLiveData: MutableLiveData<User> = MutableLiveData()
    val userLiveData: LiveData<User> get() = _userLiveData

    private val shelfsLiveData: LiveData<List<Shelf>> = Transformations
        .switchMap(_userLiveData){ user: User? -> repository.getUserShelfs(user?.id!!) }

}
