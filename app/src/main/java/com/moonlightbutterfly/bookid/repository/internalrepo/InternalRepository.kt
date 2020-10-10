package com.moonlightbutterfly.bookid.repository.internalrepo

import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface InternalRepository {

    fun insertShelf(shelf: Shelf): Completable
    fun updateShelf(shelf: Shelf): Completable
    fun deleteShelf(shelf: Shelf): Completable
    fun getShelfById(id: Int, userId: String): Flowable<Shelf?>
    fun getShelfByName(name: String, userId: String): Single<Shelf?>
    fun getShelfByBaseId(id: Int, userId: String): Flowable<Shelf?>
    fun doBaseShelfsExist(id: Int, userId: String): Single<Shelf>
    fun getShelfes(): Single<List<Shelf>?>
    fun getUserShelfs(userId: String?): Flowable<List<Shelf>?>
    fun getUser(): Flowable<User?>
    fun getUserById(id: String): Flowable<List<User>>
    fun insertUser(user: User): Completable
    fun updateUser(user: User): Completable
    fun deleteUser(user: User): Completable
}