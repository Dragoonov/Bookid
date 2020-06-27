package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.*
import com.moonlightbutterfly.bookid.UserManager
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(userManager: UserManager,
                                           repository: InternalRepository): ViewModel() {

    val userLiveData: LiveData<User> = MutableLiveData(userManager.loggedUser)

    val shelfsLiveData: LiveData<List<Shelf>> = repository.getUserShelfs(userManager.loggedUser?.id!!)
}
