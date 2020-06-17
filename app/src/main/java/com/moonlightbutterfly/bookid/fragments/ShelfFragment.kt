package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.moonlightbutterfly.bookid.BookidApplication
import com.moonlightbutterfly.bookid.adapters.ViewPager2Adapter
import com.moonlightbutterfly.bookid.databinding.ShelfFragmentBinding
import com.moonlightbutterfly.bookid.viewmodels.ShelfViewModel
import javax.inject.Inject

class ShelfFragment : Fragment() {
    private lateinit var binding: ShelfFragmentBinding

    companion object {
        fun newInstance(): ShelfFragment =
            ShelfFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ShelfViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity?.application as BookidApplication).appComponent.inject(this)
        binding = ShelfFragmentBinding.inflate(
            inflater,
            container,
            false)

        viewModel = ViewModelProvider(this,viewModelFactory)[ShelfViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewPager.adapter = ViewPager2Adapter(this)
        viewModel.shelfsLiveData.observe(viewLifecycleOwner, Observer {
            (binding.viewPager.adapter as ViewPager2Adapter).replaceShelfs(it)
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = it[position].name
            }.attach()
        })

        return binding.root
    }
}