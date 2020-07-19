package com.moonlightbutterfly.bookid.repository.database.daos

import androidx.room.*
import com.moonlightbutterfly.bookid.repository.database.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoggedUser(user: User)

    @Delete
    suspend fun deleteLoggedUser(user: User)

    @Query("select * from users")
    fun getLoggedUser(): Flow<User>
}