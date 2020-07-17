package com.moonlightbutterfly.bookid.repository.internalrepo

import android.content.Context
import androidx.lifecycle.LiveData
import com.moonlightbutterfly.bookid.repository.database.AppDatabase
import com.moonlightbutterfly.bookid.repository.database.daos.ShelfDao
import com.moonlightbutterfly.bookid.repository.database.daos.UserDao
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.Executor
import javax.inject.Inject

class RoomRepository @Inject constructor(context: Context, executor: Executor): InternalRepository {

    private val shelfDao: ShelfDao
    private val userDao: UserDao
    private val executor: Executor

    init {
        val db = AppDatabase.getInstance(context = context)
        shelfDao = db.shelfDao()
        userDao = db.userDao()
        this.executor = executor
    }

    override suspend fun insertShelf(shelf: Shelf): Unit = shelfDao.insertShelf(shelf)

    override suspend fun updateShelf(shelf: Shelf): Unit = shelfDao.updateShelf(shelf)

    override suspend fun deleteShelf(shelf: Shelf): Unit = shelfDao.deleteShelf(shelf)

    override suspend fun getShelfById(id: Int): Shelf = shelfDao.getShelfById(id)

    override suspend fun getShelfByName(name: String): Shelf = shelfDao.getShelfByName(name)

    override suspend fun getShelfes(): List<Shelf> = shelfDao.getShelfs()

    override fun getUserShelfs(userId: String): Flow<List<Shelf>> = shelfDao.getUserShelfs(userId)

    override fun getLoggedUser(): Flow<User> = userDao.getLoggedUser()

    override suspend fun insertLoggedUser(user: User) = userDao.insertLoggedUser(user)

    override suspend fun deleteLoggedUser(user: User) = userDao.deleteLoggedUser(user)


}