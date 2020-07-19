package com.moonlightbutterfly.bookid.repository.internalrepo

import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import kotlinx.coroutines.flow.Flow

interface InternalRepository {

    suspend fun insertShelf(shelf: Shelf)
    suspend fun updateShelf(shelf: Shelf)
    suspend fun deleteShelf(shelf: Shelf)
    suspend fun getShelfById(id: Int): Shelf
    suspend fun getShelfByName(name: String): Shelf
    suspend fun getShelfes(): List<Shelf>
    fun getUserShelfs(userId: String): Flow<List<Shelf>>
    fun getLoggedUser(): Flow<User>
    suspend fun insertLoggedUser(user: User)
    suspend fun deleteLoggedUser(user: User)
}