package com.moonlightbutterfly.bookid.repository.internalrepo

import android.content.Context
import com.moonlightbutterfly.bookid.repository.database.AppDatabase
import com.moonlightbutterfly.bookid.repository.database.daos.ShelfDao
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import io.reactivex.Completable
import io.reactivex.Single

class RoomRepository constructor(context: Context): InternalRepository{

    private val shelfDao: ShelfDao

    init {
        val db = AppDatabase.getInstance(context = context)
        shelfDao = db.shelfDao()
    }

    override fun insertShelf(shelf: Shelf): Completable = shelfDao.insertShelf(shelf)

    override fun updateShelf(shelf: Shelf): Completable = shelfDao.updateShelf(shelf)

    override fun getShelfById(id: Int): Single<Shelf> = shelfDao.getShelfById(id)

    override fun getShelfByName(name: String): Single<Shelf> = shelfDao.getShelfByName(name)


}