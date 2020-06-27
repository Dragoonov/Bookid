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
import com.moonlightbutterfly.bookid.dialogs.AddShelfDialog
import com.moonlightbutterfly.bookid.viewmodels.ShelfViewModel
import javax.inject.Inject

class ShelfFragment : Fragment() {
    private lateinit var binding: ShelfFragmentBinding
    private var mediator: TabLayoutMediator? = null

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
        binding.apply {
            viewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
            viewPager.adapter = ViewPager2Adapter(this@ShelfFragment)
            addShelfButton.setOnClickListener { AddShelfDialog.newInstance().show(
                activity?.supportFragmentManager!!,
                "AddShelfDialog") }
        }
        viewModel.shelfsLiveData.observe(viewLifecycleOwner, Observer {
            binding.hintContener.visibility = if (it.isNullOrEmpty()) View.VISIBLE else View.GONE
            mediator?.detach()
            (binding.viewPager.adapter as ViewPager2Adapter).replaceShelfs(it)
            mediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = it[position].name
            }.apply { attach() }
        })

        return binding.root
    }
}