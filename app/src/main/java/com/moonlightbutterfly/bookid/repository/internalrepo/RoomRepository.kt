package com.moonlightbutterfly.bookid.repository.internalrepo

import android.content.Context
import com.moonlightbutterfly.bookid.repository.database.AppDatabase
import com.moonlightbutterfly.bookid.repository.database.daos.ShelfDao
import com.moonlightbutterfly.bookid.repository.database.daos.UserDao
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomRepository @Inject constructor(context: Context) : InternalRepository {

    private val shelfDao: ShelfDao
    private val userDao: UserDao

    init {
        val db = AppDatabase.getInstance(context = context)
        shelfDao = db.shelfDao()
        userDao = db.userDao()
    }

    override suspend fun insertShelf(shelf: Shelf): Unit = shelfDao.insertShelf(shelf)

    override suspend fun updateShelf(shelf: Shelf): Unit = shelfDao.updateShelf(shelf)

    override suspend fun deleteShelf(shelf: Shelf): Unit = shelfDao.deleteShelf(shelf)

    override suspend fun getShelfById(id: Int): Shelf = shelfDao.getShelfById(id)

    override suspend fun getShelfByName(name: String): Shelf = shelfDao.getShelfByName(name)

    override suspend fun getShelfes(): List<Shelf>? = shelfDao.getShelfs()

    override fun getUserShelfs(userId: String): Flow<List<Shelf>?> = shelfDao.getUserShelfs(userId)

    override fun getUser(): Flow<User?> = userDao.getUser()

    override suspend fun insertUser(user: User) = userDao.insertUser(user)

    override suspend fun deleteUser(user: User) = userDao.deleteUser(user)
}