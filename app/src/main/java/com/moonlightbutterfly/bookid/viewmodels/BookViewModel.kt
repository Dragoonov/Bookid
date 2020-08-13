package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.*
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class BookViewModel @Inject constructor(
    private val repository: ExternalRepository,
    private val dispatcher: CoroutineDispatcher,
    private val internalRepository: InternalRepository
) : ViewModel() {

    private val _authorBooksLiveData = MutableLiveData<List<Book>>()
    val authorBooksLiveData: LiveData<List<Book>?> get() = _authorBooksLiveData

    private val _similarBooksLiveData = MutableLiveData<List<Book>>()
    val similarBooksLiveData: LiveData<List<Book>?> get() = _similarBooksLiveData

    private val _isBookInFavorites = liveData {
        val found = internalRepository.getShelfByName("Favorites")?.books?.find { bookLiveData.value?.id == it.id } != null
        emit(found)
    }
    val isBookInFavorites: LiveData<Boolean> get() = _isBookInFavorites

    private var _bookLiveData = MutableLiveData<Book>()
    val bookLiveData: LiveData<Book> get() = _bookLiveData

    private val _allDataLoaded = MediatorLiveData<Boolean>().apply {
        addSource(authorBooksLiveData) { value = isDataLoaded() }
        addSource(similarBooksLiveData) { value = isDataLoaded() }
    }
    val allDataLoaded: LiveData<Boolean> get() = _allDataLoaded

    private fun isDataLoaded() = (
            authorBooksLiveData.value != null && similarBooksLiveData.value != null && isBookInFavorites.value != null)

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
            val authorBooks = repository
                .loadAuthorBooks(_bookLiveData.value?.authors?.get(0))
                .removeDisplayedBookFromList(_bookLiveData.value as Book)
            _authorBooksLiveData.value = authorBooks
            val similarBooks = repository
                .loadSimilarBooks(_bookLiveData.value?.cathegories?.get(0))
            _similarBooksLiveData.value = similarBooks
        }
    }

    private fun clearCurrentData() {
        _authorBooksLiveData.value = null
        _similarBooksLiveData.value = null
    }

    private fun List<Book>.removeDisplayedBookFromList(book: Book): List<Book>? = this
        .toMutableList()
        .filter { it.id != book.id }

    fun <T> List<T>.toStringWithoutBrackets(): String = toString().removePrefix("[").removeSuffix("]")
}