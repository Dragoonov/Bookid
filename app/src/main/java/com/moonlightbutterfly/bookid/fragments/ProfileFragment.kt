package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.moonlightbutterfly.bookid.BookidApplication
import com.moonlightbutterfly.bookid.UserManager
import com.moonlightbutterfly.bookid.databinding.ProfileFragmentBinding
import com.moonlightbutterfly.bookid.viewmodels.ShelfViewModel
import javax.inject.Inject

class ProfileFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userManager: UserManager

    private lateinit var viewModel: ShelfViewModel

    private var binding: ProfileFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        (activity?.application as BookidApplication).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[ShelfViewModel::class.java]
        binding = ProfileFragmentBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.userManager = userManager
            it.lifecycleOwner = viewLifecycleOwner
        }
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar?.myToolbar)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}