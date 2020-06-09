package com.moonlightbutterfly.bookid.repository.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ShelfDao {

    @Insert
    fun insertShelf(shelf: Shelf): Completable

    @Update
    fun updateShelf(shelf: Shelf): Completable

    @Query("select * from shelfs where id = :id")
    fun getShelfById(id: Int): Single<Shelf>

    @Query("select * from shelfs where id = :name")
    fun getShelfByName(name: String): Single<Shelf>

    @Query("select * from shelfs")
    fun getShelfs(): Single<List<Shelf>>

    @Query("select * from shelfs where userId = :userId")
    fun getUserShelfs(userId: Int): Single<List<Shelf>>
}