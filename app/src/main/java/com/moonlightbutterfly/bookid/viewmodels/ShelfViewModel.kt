package com.moonlightbutterfly.bookid.viewmodels

import android.graphics.drawable.Drawable
import androidx.lifecycle.*
import com.moonlightbutterfly.bookid.Communicator
import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Cover
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import com.moonlightbutterfly.bookid.utils.BasicShelfsId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShelfViewModel @Inject constructor(
    private val userManager: Manager,
    private val repository: InternalRepository,
    private val dispatcher: CoroutineDispatcher,
    private val communicator: Communicator
) : ViewModel() {

    val shelfsLiveData: LiveData<List<Shelf>> = liveData {
        repository.getUserShelfs(userManager.user.value?.id!!)
            .collect { data -> data?.let { emit(it) } }
    }

    val baseShelfLiveData: LiveData<Shelf?> = Transformations.switchMap(userManager.user) {
        liveData {
            repository.getShelfById(userManager.user.value?.baseShelfId ?: -1)
                ?.collect { emit(it) }
        }
    }

    fun prepareBasicShelfs(names: Array<String>, images: Array<Pair<Int, Int>>) = viewModelScope.launch(dispatcher) {

        repository.getUserShelfs(userManager.user.value?.id!!).collect { list ->
            if (names.size == BasicShelfsId.values().size && list?.find { it.id == BasicShelfsId.FAVORITES.id } == null) {
                for ((index, value) in BasicShelfsId.values().withIndex()) {
                    insertShelf(names[index], images[index], value.id)
                }
            }

        }
    }

    fun deleteShelf(shelf: Shelf?) = shelf?.let {
        viewModelScope.launch(dispatcher) {
            repository.deleteShelf(it)
        }
    }

    fun deleteBookFromShelf(book: Book?, shelf: Shelf?) {
        if (book != null && shelf != null) {
            viewModelScope.launch(dispatcher) {
                shelf.books = shelf.books.filter { it.id != book.id }
                repository.updateShelf(shelf)
            }
        }
    }


    fun updateShelfName(shelf: Shelf?, name: String?) {
        if (shelf != null && name != null) {
            viewModelScope.launch(dispatcher) {
                shelf.name = name
                repository.updateShelf(shelf)
            }
        }
    }


    fun insertBookToShelf(shelf: Shelf?, book: Book?, message:String? = null) {
        if (shelf != null && book != null && !shelf.books.contains(book)) {
            viewModelScope.launch(dispatcher) {
                shelf.books = shelf.books.toMutableList().apply { add(book) }
                repository.updateShelf(shelf)
            }
            message?.let { communicator.postMessage(it) }
        }
    }

    fun insertShelf(name: String?, cover: Pair<Int, Int>, id: Int? = null) = name?.let {
        viewModelScope.launch(dispatcher) {
            val shelf = if (id == null) {
                Shelf(
                    name = it,
                    books = ArrayList(),
                    userId = userManager.user.value?.id!!,
                    cover = Cover(cover.first, cover.second)
                )
            } else {
                Shelf(
                    id = id,
                    name = it,
                    books = ArrayList(),
                    userId = userManager.user.value?.id!!,
                    cover = Cover(cover.first, cover.second)
                )
            }
            repository.insertShelf(shelf)
        }
    }

    fun updateBaseShelf(shelfId: Int) {
        userManager.updateBaseShelf(shelfId)
    }
}