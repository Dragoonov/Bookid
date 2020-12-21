package com.moonlightbutterfly.bookid.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.navigation.findNavController
import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.fragments.ProfileFragmentDirections
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import io.reactivex.Flowable
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val userManager: Manager,
    private val repository: InternalRepository
) : ViewModel() {

    val baseShelfLiveData: LiveData<Shelf?> = userManager.user.switchMap {
        LiveDataReactiveStreams.fromPublisher(
            if (userManager.user.value != null) {
                repository.getShelfById(userManager.user.value!!.baseShelfId, userManager.user.value!!.id)
            } else {
                Flowable.fromArray()
            }
        )
    }

    val shelfsLiveData: LiveData<List<Shelf>?> = LiveDataReactiveStreams.fromPublisher(
        repository.getUserShelfs(userManager.user.value?.id!!)
    )

    fun updateBaseShelf(shelfId: Int) {
        userManager.updateBaseShelf(shelfId)
    }

    fun signOut(view: View) {
        userManager.signOutUser(view.context)
        view.findNavController().navigate(ProfileFragmentDirections.actionProfileToNavGraph())
    }

    fun provideUserManager(): Manager = userManager
}