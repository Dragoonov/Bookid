package com.moonlightbutterfly.bookid.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.navigation.findNavController
import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.fragments.ProfileFragmentDirections
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val userManager: Manager,
    private val repository: InternalRepository
) : ViewModel() {

    val baseShelfLiveData: LiveData<Shelf?> = Transformations.switchMap(userManager.user) {
        liveData {
            if (userManager.user.value != null) {
                repository.getShelfById(userManager.user.value!!.baseShelfId, userManager.user.value!!.id)
                    ?.collect { emit(it) }
            }
        }
    }

    val shelfsLiveData: LiveData<List<Shelf>> = liveData {
        repository.getUserShelfs(userManager.user.value?.id!!)
            .collect { data -> data?.let { emit(it) } }
    }

    fun updateBaseShelf(shelfId: Int) {
        userManager.updateBaseShelf(shelfId)
    }

    fun signOut(view: View) {
        userManager.singOutUser(view.context)
        view.findNavController().navigate(ProfileFragmentDirections.actionProfileToNavGraph())
    }

    fun provideUserManager(): Manager = userManager
}