package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.bookid.Communicator
import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Cover
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import com.moonlightbutterfly.bookid.utils.DefaultShelf
import com.moonlightbutterfly.bookid.utils.Logos
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShelfsViewModel @Inject constructor(
    private val userManager: Manager,
    private val repository: InternalRepository,
    private val dispatcher: CoroutineDispatcher,
    private val communicator: Communicator
) : ViewModel() {

    val shelfsLiveData: LiveData<List<Shelf>> = liveData {
        repository.getUserShelfs(userManager.user.value?.id!!)
            .collect { data -> data?.let {
                emit(it)
            } }
    }

    fun prepareBasicShelfs(names: Array<String>, images: Array<Pair<Int, Logos>>) = viewModelScope.launch(dispatcher) {
        repository.getShelfByBaseId(DefaultShelf.FAVORITES.id, userManager.user.value?.id!!)?.collect { shelf ->
            if (shelf == null) {
                for ((index, value) in DefaultShelf.values().withIndex()) {
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

    fun insertBookToShelf(shelf: Shelf?, book: Book?, message: String? = null) {
        if (shelf != null && book != null && !shelf.books.contains(book)) {
            viewModelScope.launch(dispatcher) {
                shelf.books = shelf.books.toMutableList().apply { add(book) }
                repository.updateShelf(shelf)
            }
            message?.let { communicator.postMessage(it) }
        }
    }

    private fun insertShelf(name: String?, cover: Pair<Int, Logos>, baseShelfId: Int) = name?.let {
        viewModelScope.launch(dispatcher) {
            val shelf = Shelf(
                name = it,
                books = ArrayList(),
                userId = userManager.user.value?.id!!,
                cover = Cover(cover.first, cover.second),
                baseShelfId = baseShelfId
            )
            repository.insertShelf(shelf)
        }
    }
}