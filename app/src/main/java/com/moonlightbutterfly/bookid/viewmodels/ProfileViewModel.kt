package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.*
import com.moonlightbutterfly.bookid.UserManager
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val userManager: UserManager,
                                           private val repository: InternalRepository): ViewModel() {

    private val _userLiveData: MutableLiveData<User> = Transformations.switchMap(userManager.getUserFromDatabase())
    { MutableLiveData(it) } as MutableLiveData<User>
    val userLiveData: LiveData<User> get() = _userLiveData

    val shelfsLiveData: LiveData<List<Shelf>> = Transformations
        //TODO Wywalić Elvisa
        .switchMap(_userLiveData){ user: User? -> repository.getUserShelfs(user?.id ?: "1") }
}
