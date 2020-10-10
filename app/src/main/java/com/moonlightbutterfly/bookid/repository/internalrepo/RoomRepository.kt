package com.moonlightbutterfly.bookid.repository.internalrepo

import android.content.Context
import com.moonlightbutterfly.bookid.repository.database.AppDatabase
import com.moonlightbutterfly.bookid.repository.database.daos.ShelfDao
import com.moonlightbutterfly.bookid.repository.database.daos.UserDao
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject

class RoomRepository @Inject constructor(
    context: Context
) : InternalRepository {

    private val shelfDao: ShelfDao
    private val userDao: UserDao

    init {
        val db = AppDatabase.getInstance(context = context)
        shelfDao = db.shelfDao()
        userDao = db.userDao()
    }

    override fun insertShelf(shelf: Shelf): Completable = shelfDao.insertShelf(shelf)

    override fun updateShelf(shelf: Shelf): Completable = shelfDao.updateShelf(shelf)

    override fun deleteShelf(shelf: Shelf): Completable = shelfDao.deleteShelf(shelf)

    override fun getShelfById(id: Int, userId: String): Flowable<Shelf?> = shelfDao.getShelfById(id, userId)

    override fun getShelfByName(name: String, userId: String): Single<Shelf?> = shelfDao.getShelfByName(name, userId)

    override fun getShelfByBaseId(id: Int, userId: String): Flowable<Shelf?> = shelfDao.getShelfByBaseId(id, userId)

    override fun doBaseShelfsExist(id: Int, userId: String): Single<Shelf> = shelfDao.doBaseShelfsExist(id, userId)

    override fun getShelfes(): Single<List<Shelf>?> = shelfDao.getShelfs()

    override fun getUserShelfs(userId: String?): Flowable<List<Shelf>?> = shelfDao.getUserShelfs(userId)

    override fun getUser(): Flowable<User?> = userDao.getUser()

    override fun getUserById(id: String): Flowable<List<User>> = userDao.getUserById(id)

    override fun insertUser(user: User) = userDao.insertUser(user)

    override fun updateUser(user: User) = userDao.updateUser(user)

    override fun deleteUser(user: User) = userDao.deleteUser(user)
}