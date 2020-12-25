package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.room.EmptyResultSetException
import com.moonlightbutterfly.bookid.Communicator
import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.SchedulerProvider
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Cover
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import com.moonlightbutterfly.bookid.utils.DefaultShelf
import com.moonlightbutterfly.bookid.utils.Logos
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class ShelfsViewModel @Inject constructor(
    private val userManager: Manager,
    private val repository: InternalRepository,
    private val communicator: Communicator,
    private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    val shelfsLiveData: LiveData<List<Shelf>?> = LiveDataReactiveStreams.fromPublisher(
        repository.getUserShelfs(userManager.user.value?.id)
    )

    private val disposable = CompositeDisposable()

    fun prepareBasicShelfs(names: Array<String>, images: Array<Pair<Int, Logos>>) = disposable.add(
        repository.doBaseShelfsExist(DefaultShelf.FAVORITES.id, userManager.user.value?.id!!)
            .subscribeOn(schedulerProvider.io())
            .subscribe(
                {},
                {
                    if (it.javaClass == EmptyResultSetException::class.java) {
                        for ((index, value) in DefaultShelf.values().withIndex()) {
                            insertShelf(names[index], images[index], value.id)
                        }
                    }
                })
    )

    fun deleteShelf(shelf: Shelf?) = shelf?.let {
        disposable.add(repository.deleteShelf(it)
            .subscribeOn(schedulerProvider.io())
            .subscribe()
        )
    }

    fun insertBookToShelf(shelf: Shelf?, book: Book?, message: String? = null) {
        if (shelf != null && book != null && !shelf.books.contains(book)) {
            shelf.books = shelf.books.toMutableList().apply { add(book) }
            disposable.add(repository.updateShelf(shelf)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe {
                message?.let { communicator.postMessage(it) }
            })
        }
    }

    private fun insertShelf(name: String?, cover: Pair<Int, Logos>, baseShelfId: Int) = name?.let {
            val shelf = Shelf(
                name = it,
                books = ArrayList(),
                userId = userManager.user.value?.id!!,
                cover = Cover(cover.first, cover.second),
                baseShelfId = baseShelfId
            )
            disposable.add(repository
                .insertShelf(shelf)
                .subscribeOn(schedulerProvider.io())
                .subscribe())
    }

    override fun onCleared() = disposable.dispose()

}