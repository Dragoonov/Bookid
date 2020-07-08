package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.moonlightbutterfly.bookid.adapters.BookAdapter
import com.moonlightbutterfly.bookid.adapters.LAYOUT
import com.moonlightbutterfly.bookid.databinding.RecyclerViewLayoutBinding
import com.moonlightbutterfly.bookid.Converters
import com.moonlightbutterfly.bookid.repository.database.entities.Book

class RecyclerFragment: Fragment() {

    private var binding: RecyclerViewLayoutBinding? = null

    companion object {
        fun newInstance(books: List<Book>): RecyclerFragment =
            RecyclerFragment().apply {
                arguments = Bundle().apply { putString("booksList", Converters.convertToJSONString(books)) }
            }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RecyclerViewLayoutBinding.inflate(inflater, container, false).also {
            it.booksList = Converters.fromStringToBookList(arguments?.getString("booksList")!!)
            it.lifecycleOwner = viewLifecycleOwner
            it.listRecycler.let { recycler ->
                recycler.adapter = BookAdapter(LAYOUT.VERTICAL)
                recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}