package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.*
import com.moonlightbutterfly.bookid.Communicator
import com.moonlightbutterfly.bookid.UserManager
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val externalRepository: ExternalRepository,
    private val dispatcher: CoroutineDispatcher,
    internalRepository: InternalRepository,
    userManager: UserManager,
    communicator: Communicator
) :
    BaseViewModel(dispatcher, internalRepository, userManager, communicator) {

    private var searchedBooks: MutableLiveData<List<Book>> = liveData<List<Book>> {
        emit(ArrayList())
    } as MutableLiveData<List<Book>>
    val allBooks = MediatorLiveData<MutableList<Book>>().apply {
        value = ArrayList()
        addSource(searchedBooks) {
            if (it != null) {
                value = ArrayList<Book>().apply {
                    addAll(value as ArrayList<Book> + it)
                }
            }
        }
    }
    val showHint: LiveData<Boolean> get() = _showHint
    private var _showHint = MutableLiveData(true)

    var currentQuery: String? = ""
        private set

    private var page = 1

    private var currentJob: Job? = null

    val allDataLoaded: LiveData<Boolean> = Transformations.map(searchedBooks) {
        searchedBooks.value != null
    }

    private fun clearLatestSearchedBooksBatch() = with(searchedBooks) {
        value = null
    }

    fun requestSearch(query: String?) {
        clearLatestSearchedBooksBatch()
        currentJob?.cancel()
        allBooks.value = ArrayList()
        page = 1
        currentQuery = query
        _showHint.value = false
        if (!query.isNullOrEmpty()) {
            currentJob = viewModelScope.launch(dispatcher) {
                searchedBooks.value = externalRepository.loadSearchedBooks(query, page)
            }
        }
    }

    fun loadMore() {
        clearLatestSearchedBooksBatch()
        page = page.inc()
        viewModelScope.launch(dispatcher) {
            searchedBooks.value = externalRepository.loadSearchedBooks(currentQuery, page)
        }
    }
}