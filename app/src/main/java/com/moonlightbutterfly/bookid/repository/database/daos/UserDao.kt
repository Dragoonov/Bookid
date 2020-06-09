package com.moonlightbutterfly.bookid.repository.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.moonlightbutterfly.bookid.repository.database.entities.User
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface UserDao {
    @Insert
    fun insertLoggedUser(): Completable

    @Update
    fun updateLoggedUser(): Completable

    @Query("select * from users")
    fun getLoggedUser(): Single<User>
}