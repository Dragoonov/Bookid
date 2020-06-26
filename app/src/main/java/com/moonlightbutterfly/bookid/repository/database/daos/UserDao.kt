package com.moonlightbutterfly.bookid.repository.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.moonlightbutterfly.bookid.repository.database.entities.User
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLoggedUser(user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateLoggedUser(user: User)

    @Delete
    fun deleteLoggedUser(user: User)

    @Query("select * from users")
    fun getLoggedUser(): LiveData<User>
}