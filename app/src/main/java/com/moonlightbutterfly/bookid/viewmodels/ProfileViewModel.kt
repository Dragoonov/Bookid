package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.*
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val repository: InternalRepository): ViewModel() {

    private val _userLiveData: MutableLiveData<User> = MutableLiveData()
    val userLiveData: LiveData<User> get() = _userLiveData

    val shelfsLiveData: LiveData<List<Shelf>> = Transformations
        .switchMap(_userLiveData){ user: User? -> repository.getUserShelfs(user?.id!!) }

    private val userObserver = Observer<User> {_userLiveData.value = it}

    init {
        repository.getLoggedUser().observeForever { userObserver }
    }

    override fun onCleared() {
        super.onCleared()
        repository.getLoggedUser().removeObserver(userObserver)
    }

}
