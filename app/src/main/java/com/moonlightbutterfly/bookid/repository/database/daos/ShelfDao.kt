package com.moonlightbutterfly.bookid.repository.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ShelfDao {

    @Insert
    fun insertShelf(shelf: Shelf)

    @Update
    fun updateShelf(shelf: Shelf)

    @Delete
    fun deleteShelf(shelf: Shelf)

    @Query("select * from shelfs where id = :id")
    fun getShelfById(id: Int): LiveData<Shelf>

    @Query("select * from shelfs where id = :name")
    fun getShelfByName(name: String): LiveData<Shelf>

    @Query("select * from shelfs")
    fun getShelfs(): LiveData<List<Shelf>>

    @Query("select * from shelfs where userId = :userId")
    fun getUserShelfs(userId: String): LiveData<List<Shelf>>
}