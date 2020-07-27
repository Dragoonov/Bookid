package com.moonlightbutterfly.bookid.repository.internalrepo

import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RoomRepositoryFake : InternalRepository {

    private val shelfs: MutableList<Shelf>? = ArrayList()

    private var user: User? = null

    override suspend fun insertShelf(shelf: Shelf): Unit = run { shelfs?.add(shelf)!! }

    override suspend fun updateShelf(shelf: Shelf): Unit = run {
        shelfs?.removeIf { shelf.id == it.id }.also {
            if (it!!) {
                shelfs?.add(shelf)
            }
        }
    }

    override suspend fun deleteShelf(shelf: Shelf): Unit = run {
        shelfs?.removeIf { shelf.id == it.id }!!
    }

    override suspend fun getShelfById(id: Int): Shelf? = shelfs!![id]

    override suspend fun getShelfByName(name: String): Shelf? = shelfs?.find { it.name == name }

    override suspend fun getShelfes(): List<Shelf>? = shelfs

    override fun getUserShelfs(userId: String): Flow<List<Shelf>?> = flowOf(shelfs?.toList()!!)

    override fun getUser(): Flow<User?> = flowOf(
        User(
            "116644458345052983935",
            "Jakub Lipowski",
            "jakub.lipowski01@gmail.com",
            "https://lh3.googleusercontent.com/a-/AOh14GiPou93h951L-XfDmexoG3YKIFM1e7zsNzl5a4B"
        )
    )
    override suspend fun insertUser(user: User): Unit = run { this.user = user }

    override suspend fun deleteUser(user: User) = run { this.user = null }
}