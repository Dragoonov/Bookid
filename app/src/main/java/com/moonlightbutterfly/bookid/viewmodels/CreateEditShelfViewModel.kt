package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.*
import com.moonlightbutterfly.bookid.UserManager
import com.moonlightbutterfly.bookid.repository.database.entities.Cover
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateEditShelfViewModel @Inject constructor(
    private val repository: InternalRepository,
    private val dispatcher: CoroutineDispatcher,
    private val userManager: UserManager
) : ViewModel() {

    private var shelfId: MutableLiveData<Int> = MutableLiveData(-1)

    val actionTitle: LiveData<String> get() = _actionTitle
    private var _actionTitle: MutableLiveData<String> = MutableLiveData("")

    val shelfLiveData = shelfId.switchMap {
        liveData {
            repository.getShelfById(it)?.collect {
                emit(it)
                it?.let { iconId = it.cover.iconId }
            }
        }
    }

    var iconId: Int = -1

    fun setShelfId(id: Int) = run { shelfId.value = id }

    fun setActionTitle(type: String) = run {
        _actionTitle.value = type
    }

    fun finishCreateModify(name: String, background: Int, iconId: Int) {
        if (shelfId.value == -1) {
            saveShelf(name, background, iconId)
        } else {
            updateShelf(name, background, iconId)
        }
    }

    private fun saveShelf(name: String, background: Int, iconId: Int) = viewModelScope.launch(dispatcher) {
        val shelf = Shelf(
            name = name,
            books = ArrayList(),
            cover = Cover(background, iconId),
            userId = userManager.user.value?.id!!
        )
        repository.insertShelf(shelf)
    }

    private fun updateShelf(name: String, background: Int, iconId: Int) = viewModelScope.launch(dispatcher) {
        val shelf = Shelf(
            id = shelfLiveData.value!!.id,
            name = name,
            books = shelfLiveData.value!!.books,
            userId = shelfLiveData.value!!.userId,
            cover = Cover(background, iconId)
        )
        repository.updateShelf(shelf)
    }

}