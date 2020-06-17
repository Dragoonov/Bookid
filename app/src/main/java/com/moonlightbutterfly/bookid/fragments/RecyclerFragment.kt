package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.moonlightbutterfly.bookid.Utils
import com.moonlightbutterfly.bookid.adapters.BookAdapter
import com.moonlightbutterfly.bookid.adapters.LAYOUT
import com.moonlightbutterfly.bookid.databinding.RecyclerViewLayoutBinding
import com.moonlightbutterfly.bookid.repository.database.Converters
import com.moonlightbutterfly.bookid.repository.database.entities.Book

class RecyclerFragment: Fragment() {

    companion object {
        fun newInstance(books: List<Book>): RecyclerFragment =
            RecyclerFragment().apply {
                arguments = Bundle().apply { putString("booksList", Utils.convertToJSONString(books)) }
            }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = RecyclerViewLayoutBinding.inflate(
            inflater,
            container,
            false)
        LinearLayout.LayoutParams.WRAP_CONTENT

        binding.booksList = Converters.fromStringToBookList(arguments?.getString("booksList")!!)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.listRecycler.let {
            it.adapter = BookAdapter(LAYOUT.VERTICAL)
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        }

        return binding.root
    }
}