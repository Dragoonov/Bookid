package com.moonlightbutterfly.bookid.repository.internalrepo

import androidx.lifecycle.LiveData
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import io.reactivex.Completable
import io.reactivex.Single

interface InternalRepository {

    fun insertShelf(shelf: Shelf)
    fun updateShelf(shelf: Shelf)
    fun deleteShelf(shelf: Shelf)
    fun getShelfById(id: Int): LiveData<Shelf>
    fun getShelfByName(name: String): LiveData<Shelf>
    fun getShelfes(): LiveData<List<Shelf>>
    fun getUserShelfs(userId: String): LiveData<List<Shelf>>
    fun getLoggedUser(): LiveData<User>
    fun insertLoggedUser(user: User)
    fun deleteLoggedUser(user: User)
}