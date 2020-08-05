package com.moonlightbutterfly.bookid.repository.database.daos

import androidx.room.*
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import kotlinx.coroutines.flow.Flow

@Dao
interface ShelfDao {

    @Insert
    suspend fun insertShelf(shelf: Shelf)

    @Update
    suspend fun updateShelf(shelf: Shelf)

    @Delete
    suspend fun deleteShelf(shelf: Shelf)

    @Query("select * from shelfs where id = :id")
    suspend fun getShelfById(id: Int): Shelf

    @Query("select * from shelfs where name = :name")
    suspend fun getShelfByName(name: String): Shelf

    @Query("select * from shelfs")
    suspend fun getShelfs(): List<Shelf>?

    @Query("select * from shelfs where userId = :userId")
    fun getUserShelfs(userId: String): Flow<List<Shelf>?>
}