package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.Exception
import javax.inject.Inject

abstract class BaseFragment<T : ViewDataBinding,K : ViewModel>(private val clas: Class<K>? = null): Fragment() {

    protected var binding: T? = null
    lateinit var viewModel: K

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    open fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {}

    open fun initializeViewModel() {
        if (clas != null) {
            viewModel = ViewModelProvider(this,viewModelFactory)[clas]
        }
    }
    open fun initializeCustom() {}

    open fun inject() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inject()
        initializeViewModel()
        initializeBinding(inflater, container)
        initializeCustom()
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}