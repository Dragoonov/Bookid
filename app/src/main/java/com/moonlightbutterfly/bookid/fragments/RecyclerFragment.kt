package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.moonlightbutterfly.bookid.adapters.LAYOUT
import com.moonlightbutterfly.bookid.databinding.RecyclerViewLayoutBinding
import com.moonlightbutterfly.bookid.Converters
import com.moonlightbutterfly.bookid.CustomItemDecoration
import com.moonlightbutterfly.bookid.adapters.BookAdapterVertical
import com.moonlightbutterfly.bookid.repository.database.entities.Book

class RecyclerFragment: BaseFragment<RecyclerViewLayoutBinding, ViewModel>() {

    companion object {
        fun newInstance(books: List<Book>): RecyclerFragment =
            RecyclerFragment().apply {
                arguments = Bundle().apply { putString("booksList", Converters.convertToJSONString(books)) }
            }
    }

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = RecyclerViewLayoutBinding.inflate(inflater, container, false).also {
            it.booksList = Converters.fromStringToBookList(arguments?.getString("booksList")!!)
            it.lifecycleOwner = viewLifecycleOwner
            it.listRecycler.let { recycler ->
                recycler.addItemDecoration(CustomItemDecoration(LAYOUT.VERTICAL))
                recycler.adapter = BookAdapterVertical()
                recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }
}