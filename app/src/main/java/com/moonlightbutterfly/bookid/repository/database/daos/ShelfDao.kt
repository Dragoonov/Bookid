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

    @Query("select * from shelf where id = :id")
    fun getShelfById(id: Int): Single<Shelf>

    @Query("select * from shelf where id = :name")
    fun getShelfByName(name: String): Single<Shelf>
}