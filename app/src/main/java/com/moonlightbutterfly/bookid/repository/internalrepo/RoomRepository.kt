package com.moonlightbutterfly.bookid.repository.internalrepo

import android.content.Context
import androidx.lifecycle.LiveData
import com.moonlightbutterfly.bookid.repository.database.AppDatabase
import com.moonlightbutterfly.bookid.repository.database.daos.ShelfDao
import com.moonlightbutterfly.bookid.repository.database.daos.UserDao
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class RoomRepository @Inject constructor(context: Context): InternalRepository {

    private val shelfDao: ShelfDao
    private val userDao: UserDao

    init {
        val db = AppDatabase.getInstance(context = context)
        shelfDao = db.shelfDao()
        userDao = db.userDao()
    }

    override fun insertShelf(shelf: Shelf): Unit = shelfDao.insertShelf(shelf)

    override fun updateShelf(shelf: Shelf): Unit = shelfDao.updateShelf(shelf)

    override fun getShelfById(id: Int): LiveData<Shelf> = shelfDao.getShelfById(id)

    override fun getShelfByName(name: String): LiveData<Shelf> = shelfDao.getShelfByName(name)

    override fun getShelfes(): LiveData<List<Shelf>> = shelfDao.getShelfs()

    override fun getUserShelfs(userId: Int): LiveData<List<Shelf>> = shelfDao.getUserShelfs(userId)

    override fun getLoggedUser(): LiveData<User> = userDao.getLoggedUser()


}