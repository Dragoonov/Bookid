package com.moonlightbutterfly.bookid.repository.database.daos

import androidx.lifecycle.LiveData
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
    fun insertShelf(shelf: Shelf)

    @Update
    fun updateShelf(shelf: Shelf)

    @Query("select * from shelfs where id = :id")
    fun getShelfById(id: Int): LiveData<Shelf>

    @Query("select * from shelfs where id = :name")
    fun getShelfByName(name: String): LiveData<Shelf>

    @Query("select * from shelfs")
    fun getShelfs(): LiveData<List<Shelf>>

    @Query("select * from shelfs where userId = :userId")
    fun getUserShelfs(userId: Int): LiveData<List<Shelf>>
}