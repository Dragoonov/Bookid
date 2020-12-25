package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.*
import com.moonlightbutterfly.bookid.Communicator
import com.moonlightbutterfly.bookid.SchedulerProvider
import com.moonlightbutterfly.bookid.UserManager
import com.moonlightbutterfly.bookid.repository.database.entities.Cover
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import com.moonlightbutterfly.bookid.utils.Logos
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CreateEditShelfViewModel @Inject constructor(
    private val repository: InternalRepository,
    private val userManager: UserManager,
    private val communicator: Communicator,
    private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    private var shelfId: MutableLiveData<Int> = MutableLiveData(-1)

    val actionTitle: LiveData<String> get() = _actionTitle
    private var _actionTitle: MutableLiveData<String> = MutableLiveData("")

    val shelfLiveData = shelfId.switchMap { id ->
        LiveDataReactiveStreams.fromPublisher(
            repository.getShelfById(id, userManager.user.value!!.id)
                .doOnNext {
                    it?.let { iconId = it.cover.icon }
                }
        )
    }

    lateinit var iconId: Logos

    lateinit var errorOccurredMessage: String

    private val disposable = CompositeDisposable()

    fun setShelfId(id: Int) = run { shelfId.value = id }

    fun setActionTitle(type: String) = run {
        _actionTitle.value = type
    }

    fun finishCreateModify(name: String, background: Int, iconId: Logos) {
        if (shelfId.value == -1) {
            saveShelf(name, background, iconId)
        } else {
            updateShelf(name, background, iconId)
        }
    }

    private fun saveShelf(name: String, background: Int, iconId: Logos) {
        val shelf = Shelf(
            name = name,
            books = ArrayList(),
            cover = Cover(background, iconId),
            userId = userManager.user.value?.id!!
        )
        disposable.add(
            repository.insertShelf(shelf)
                .subscribeOn(schedulerProvider.io())
                .doOnError {
                    communicator.postMessage(errorOccurredMessage)
                }
                .subscribe()
        )
    }

    private fun updateShelf(name: String, background: Int, iconId: Logos) {
        val shelf = Shelf(
            id = shelfLiveData.value!!.id,
            name = name,
            books = shelfLiveData.value!!.books,
            userId = shelfLiveData.value!!.userId,
            cover = Cover(background, iconId)
        )
        disposable.add(
            repository.updateShelf(shelf)
                .subscribeOn(schedulerProvider.io())
                .doOnError {
                    communicator.postMessage(errorOccurredMessage)
                }.subscribe()
        )
    }

}