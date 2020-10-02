package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.*
import com.moonlightbutterfly.bookid.Communicator
import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class BooksListViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val internalRepository: InternalRepository,
    userManager: Manager,
    communicator: Communicator
) : BaseViewModel(dispatcher, internalRepository, userManager, communicator) {

    private val customShelfIdLiveData = MutableLiveData(1000)
    val customShelfLiveData: LiveData<Shelf?> = Transformations.switchMap(customShelfIdLiveData) {
        liveData {
            internalRepository.getShelfById(it)?.collect {
                emit(it)
            }
        }
    }
    fun setCustomShelfId(id: Int) = run { customShelfIdLiveData.value = id }

    fun deleteBookFromCustom(book: Book) = viewModelScope.launch(dispatcher) {
        deleteBookFromShelf(book, customShelfLiveData.value)
    }

}