package com.moonlightbutterfly.bookid.repository.database.daos

import androidx.room.*
import com.moonlightbutterfly.bookid.repository.database.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("select * from users")
    fun getUser(): Flow<User?>

    @Query("select * from users where id = :id")
    fun getUserById(id:String): Flow<User?>?
}