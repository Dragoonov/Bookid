package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.*
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class BookViewModel @Inject constructor(
    private val repository: ExternalRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _authorBooksLiveData = liveData {
        val books = repository
            .loadAuthorBooks(_bookLiveData.value?.author)
            .removeDisplayedBookFromList(_bookLiveData.value as Book)
        emit(books)
    } as MutableLiveData
    val authorBooksLiveData: LiveData<List<Book>?> get() = _authorBooksLiveData

    private var _bookLiveData = MutableLiveData<Book>()
    val bookLiveData: LiveData<Book> get() = _bookLiveData

    private val _allDataLoaded = Transformations.map(authorBooksLiveData) {it != null}
    val allDataLoaded: LiveData<Boolean> get() = _allDataLoaded

    fun setBook(book: Book) {
        if (_bookLiveData.value?.id == book.id) {
            return
        }
        this._bookLiveData.value = book
        refreshData()
    }

    fun refreshData() {
        clearCurrentData()
        viewModelScope.launch(dispatcher) {
            val books = repository
                .loadAuthorBooks(_bookLiveData.value?.author)
                .removeDisplayedBookFromList(_bookLiveData.value as Book)
            _authorBooksLiveData.value = books
        }
    }

    private fun clearCurrentData() {
        _authorBooksLiveData.value = null
    }

    private fun List<Book>.removeDisplayedBookFromList(book: Book): List<Book>? = this
        .toMutableList()
        .filter { it.id != book.id }

}