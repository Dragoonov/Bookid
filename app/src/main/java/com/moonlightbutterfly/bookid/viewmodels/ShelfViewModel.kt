package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import javax.inject.Inject

class ShelfViewModel @Inject constructor(val repository: InternalRepository): ViewModel() {

    private val _userLiveData: MutableLiveData<User> = Transformations.switchMap(repository.getLoggedUser())
    { MutableLiveData(it) } as MutableLiveData<User>
    val userLiveData: LiveData<User> get() = _userLiveData

    val shelfsLiveData: LiveData<List<Shelf>> = Transformations
        //TODO Wywalić Elvisa, dodac UserManager
        .switchMap(_userLiveData){ user: User? -> repository.getUserShelfs(user?.id ?: 1) }
}