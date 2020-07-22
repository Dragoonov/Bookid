package com.moonlightbutterfly.bookid.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayoutMediator
import com.moonlightbutterfly.bookid.adapters.ViewPager2Adapter
import com.moonlightbutterfly.bookid.databinding.ShelfFragmentBinding
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.viewmodels.ShelfViewModel

class ShelfFragment : BaseFragment<ShelfFragmentBinding, ShelfViewModel>(ShelfViewModel::class.java) {

    private var mediator: TabLayoutMediator? = null

    private val shelfsObserver: Observer<List<Shelf>> = Observer {
        binding?.hintContener?.visibility = if (it.isNullOrEmpty()) View.VISIBLE else View.GONE
        mediator?.detach()
        (binding?.viewPager?.adapter as ViewPager2Adapter).replaceShelfs(it)
        mediator = TabLayoutMediator(binding?.tabLayout!!, binding?.viewPager!!) { tab, position ->
            tab.text = it[position].name
        }.apply {
            attach()
        }
    }

    override fun inject() = appComponent.inject(this)

    override fun initializeViewModel() {
        super.initializeViewModel()
        viewModel.shelfsLiveData.observe(viewLifecycleOwner, shelfsObserver)
    }

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = ShelfFragmentBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.viewPager.adapter = ViewPager2Adapter(this@ShelfFragment)
        }
    }

    override fun initializeCustom() = (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar?.myToolbar)

    override fun onDestroyView() {
        binding?.viewPager?.adapter = null
        mediator?.detach()
        mediator = null
        super.onDestroyView()
    }

}