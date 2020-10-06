package com.moonlightbutterfly.bookid.repository.database.daos

import androidx.room.*
import com.moonlightbutterfly.bookid.repository.database.entities.User
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(user: User): Completable

    @Delete
    fun deleteUser(user: User): Completable

    @Query("select * from users")
    fun getUser(): Flowable<User?>

    @Query("select * from users where id = :id")
    fun getUserById(id:String): Flowable<User?>
}