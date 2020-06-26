package com.moonlightbutterfly.bookid

import androidx.lifecycle.LiveData
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(private val internalRepository: InternalRepository) {

    var loggedUser: User? = null



    fun saveUserToDatabase(user: User) = internalRepository.insertLoggedUser(user)

    fun getUserFromDatabase(): LiveData<User> = internalRepository.getLoggedUser()

    fun deleteUserFromDatabase(user: User) = internalRepository.deleteLoggedUser(user)


}