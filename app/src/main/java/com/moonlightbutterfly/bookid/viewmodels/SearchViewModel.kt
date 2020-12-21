package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.moonlightbutterfly.bookid.Communicator
import com.moonlightbutterfly.bookid.SchedulerProvider
import com.moonlightbutterfly.bookid.UserManager
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val externalRepository: ExternalRepository,
    private val schedulerProvider: SchedulerProvider,
    internalRepository: InternalRepository,
    userManager: UserManager,
    communicator: Communicator
) : BaseViewModel(internalRepository, communicator, schedulerProvider, userManager) {

    private var searchedBooks: MutableLiveData<List<Book>> = MutableLiveData(listOf())

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

    private var currentStream: Disposable? = null

    val allDataLoaded: LiveData<Boolean> = searchedBooks.map {
        searchedBooks.value != null
    }

    private fun clearLatestSearchedBooksBatch() {
        searchedBooks.value = null
    }

    fun requestSearch(query: String?) {
        clearLatestSearchedBooksBatch()
        currentStream?.dispose()
        allBooks.value = ArrayList()
        page = 1
        currentQuery = query
        _showHint.value = false
        if (!query.isNullOrEmpty()) {
            currentStream = externalRepository.loadSearchedBooks(query, page)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSuccess { searchedBooks.value = it }
                .subscribe()
        }
    }

    fun loadMore() {
        clearLatestSearchedBooksBatch()
        page = page.inc()
        disposable.add(externalRepository.loadSearchedBooks(currentQuery, page)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnSuccess { searchedBooks.value = it }
            .subscribe())
    }
}