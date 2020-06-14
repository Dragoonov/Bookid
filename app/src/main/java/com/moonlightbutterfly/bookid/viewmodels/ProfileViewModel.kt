package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.*
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val repository: InternalRepository): ViewModel() {

    private val _userLiveData: MutableLiveData<User> = Transformations.switchMap(repository.getLoggedUser())
    { MutableLiveData(it) } as MutableLiveData<User>
    val userLiveData: LiveData<User> get() = _userLiveData

    val shelfsLiveData: LiveData<List<Shelf>> = Transformations
        //TODO Wywalić Elvisa
        .switchMap(_userLiveData){ user: User? -> repository.getUserShelfs(user?.id ?: 1) }

    private val _allDataLoaded: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(_userLiveData) { value = updateDataLoaded() }
        addSource(shelfsLiveData) { value = updateDataLoaded() }
    }
    val allDataLoaded: LiveData<Boolean> get() = _allDataLoaded

    init {
        refreshData()
    }

    private fun updateDataLoaded(): Boolean =
    //TODO odkomentować _userLiveData.value != null &&
            shelfsLiveData.value != null

    fun refreshData() {
        clearCurrentData()
        repository.getLoggedUser()
    }

    private fun clearCurrentData() {
        _userLiveData.value = null
    }

}
