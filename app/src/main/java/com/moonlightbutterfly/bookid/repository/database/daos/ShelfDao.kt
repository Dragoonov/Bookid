package com.moonlightbutterfly.bookid.repository.database.daos

import androidx.room.*
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface ShelfDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertShelf(shelf: Shelf): Completable

    @Update
    fun updateShelf(shelf: Shelf): Completable

    @Delete
    fun deleteShelf(shelf: Shelf): Completable

    @Query("select * from shelfs where id = :id and userId = :userId")
    fun getShelfById(id: Int, userId: String): Flowable<Shelf?>

    @Query("select * from shelfs where name = :name and userId = :userId")
    fun getShelfByName(name: String, userId: String): Single<Shelf?>

    @Query("select * from shelfs where baseShelfId = :id and userId = :userId")
    fun getShelfByBaseId(id: Int, userId: String): Flowable<Shelf?>

    @Query("select * from shelfs")
    fun getShelfs(): Single<List<Shelf>?>

    @Query("select * from shelfs where userId = :userId")
    fun getUserShelfs(userId: String?): Flowable<List<Shelf>?>

    @Query("select * from shelfs where baseShelfId = :id and userId = :userId")
    fun doBaseShelfsExist(id: Int, userId: String): Single<Shelf>
}