package com.moonlightbutterfly.bookid.repository.internalrepo

import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import io.reactivex.Completable
import io.reactivex.Single

interface InternalRepository {

    fun insertShelf(shelf: Shelf): Completable
    fun updateShelf(shelf: Shelf): Completable
    fun getShelfById(id: Int): Single<Shelf>
    fun getShelfByName(name: String): Single<Shelf>

}