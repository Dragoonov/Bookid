package com.moonlightbutterfly.bookid.repository.internalrepo

import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import kotlinx.coroutines.flow.Flow

interface InternalRepository {

    suspend fun insertShelf(shelf: Shelf)
    suspend fun updateShelf(shelf: Shelf)
    suspend fun deleteShelf(shelf: Shelf)
    fun getShelfById(id: Int): Flow<Shelf>?
    suspend fun getShelfByName(name: String): Shelf?
    suspend fun getShelfes(): List<Shelf>?
    fun getUserShelfs(userId: String): Flow<List<Shelf>?>
    fun getUser(): Flow<User?>
    suspend fun insertUser(user: User)
    suspend fun deleteUser(user: User)
}