package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.moonlightbutterfly.bookid.BookidApplication
import com.moonlightbutterfly.bookid.adapters.ViewPager2Adapter
import com.moonlightbutterfly.bookid.databinding.EditShelfsFragmentBinding
import com.moonlightbutterfly.bookid.viewmodels.ShelfViewModel
import javax.inject.Inject

class EditShelfsFragment: Fragment() {

    private lateinit var binding: EditShelfsFragmentBinding

    companion object {
        fun newInstance(): EditShelfsFragment =
            EditShelfsFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ShelfViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity?.application as BookidApplication).appComponent.inject(this)
        binding = EditShelfsFragmentBinding.inflate(
            inflater,
            container,
            false)

        viewModel = ViewModelProvider(this,viewModelFactory)[ShelfViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.shelfsLiveData.observe(viewLifecycleOwner, Observer {
            // Uzupelnic
        })
        return binding.root
    }
}