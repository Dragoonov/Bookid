package com.moonlightbutterfly.bookid.repository.database.daos

import androidx.room.*
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import kotlinx.coroutines.flow.Flow

@Dao
interface ShelfDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertShelf(shelf: Shelf)

    @Update
    suspend fun updateShelf(shelf: Shelf)

    @Delete
    suspend fun deleteShelf(shelf: Shelf)

    @Query("select * from shelfs where id = :id and userId = :userId")
    fun getShelfById(id: Int, userId: String): Flow<Shelf?>?

    @Query("select * from shelfs where name = :name and userId = :userId")
    suspend fun getShelfByName(name: String, userId: String): Shelf

    @Query("select * from shelfs where baseShelfId = :id and userId = :userId")
    fun getShelfByBaseId(id: Int, userId: String): Flow<Shelf?>?

    @Query("select * from shelfs")
    suspend fun getShelfs(): List<Shelf>?

    @Query("select * from shelfs where userId = :userId")
    fun getUserShelfs(userId: String): Flow<List<Shelf>?>
}