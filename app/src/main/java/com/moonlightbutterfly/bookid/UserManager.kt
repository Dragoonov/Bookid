package com.moonlightbutterfly.bookid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(private val internalRepository: InternalRepository) {

    val user: LiveData<User> get() = _user
    private val _user: MutableLiveData<User> = Transformations.switchMap(internalRepository.getLoggedUser()) {
        MutableLiveData(it)
    } as MutableLiveData<User>

    fun singOutUser() {
        if (user.value != null) {
            internalRepository.deleteLoggedUser(user.value!!)
            _user.value = null
        }
    }
    fun signInUser(user: User) = internalRepository.insertLoggedUser(user)
}
