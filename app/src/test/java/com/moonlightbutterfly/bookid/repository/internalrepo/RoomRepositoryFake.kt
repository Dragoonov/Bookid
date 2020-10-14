package com.moonlightbutterfly.bookid.repository.internalrepo

import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RoomRepositoryFake : InternalRepository {

    private var shelfs: MutableList<Shelf>? = null


    private var user: User? = null

    override fun insertShelf(shelf: Shelf): Completable {
        if(shelfs == null) shelfs = ArrayList()
        return Completable.fromCallable {
            shelfs?.add(shelf)
        }

    }

    override fun updateShelf(shelf: Shelf): Completable {
        if(shelfs == null) shelfs = ArrayList()
        return Completable.fromCallable {
            shelfs?.removeIf { shelf.id == it.id }.also {
                if (it!!) {
                    shelfs?.add(shelf)
                }
            }
        }
    }

    override fun deleteShelf(shelf: Shelf) =
       Completable.fromCallable { shelfs?.removeIf { shelf.id == it.id } }

    override fun getShelfById(id: Int, userId: String): Flowable<Shelf?> = Flowable.just(
        shelfs?.find {
            id == it.id && userId == it.userId
        }
    )

    override fun getShelfByName(name: String, userId: String): Single<Shelf?> = Single.just(shelfs?.find {
        it.name == name && userId == it.userId
    })

    override fun getShelfByBaseId(id: Int, userId: String): Flowable<Shelf?> = Flowable.just(
        shelfs?.find { it.baseShelfId == id && userId == it.userId }
    )

    override fun doBaseShelfsExist(id: Int, userId: String): Single<Shelf> = Single.just(
        shelfs?.find { it.baseShelfId == id && userId == it.userId }
    )

    override fun getShelfes(): Single<List<Shelf>?> = Single.just(shelfs)

    override fun getUserShelfs(userId: String?): Flowable<List<Shelf>?> = Flowable.just(shelfs?.toList())
    override fun getUser(): Flowable<User?> = Flowable.just(
        User(
            "11",
            "Jakub Lipowski",
            "jakub.lipowski01@gmail.com",
            "https://lh3.googleusercontent.com/a-/AOh14GiPou93h951L-XfDmexoG3YKIFM1e7zsNzl5a4B"
        )
    )

    override fun getUserById(id: String): Flowable<List<User>> = Flowable.just(
        if (id == this.user?.id) {
            listOf(this.user!!)
        } else {
            listOf()
        }
    )
    override fun insertUser(user: User): Completable = Completable.fromCallable {
        this.user = user
        user
    }

    override fun updateUser(user: User): Completable = Completable.fromCallable {
        this.user = user
        user
    }

    override fun deleteUser(user: User) = Completable.fromCallable {
        this.user = null
        null
    }

}