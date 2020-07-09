package com.moonlightbutterfly.bookid

import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moonlightbutterfly.bookid.adapters.BookAdapter
import com.moonlightbutterfly.bookid.adapters.EditShelfsShelfAdapter
import com.moonlightbutterfly.bookid.adapters.LAYOUT
import com.moonlightbutterfly.bookid.databinding.ComposableBookListBinding
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    Glide.with(view.context)
        .load(url)
        .placeholder(R.drawable.ic_launcher_foreground)
        .error(R.drawable.ic_launcher_foreground)
        .into(view)
}

@BindingAdapter("android:visibility")
fun goneUnless(view: View, data: Boolean) {
    view.visibility = if (data) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("dataList")
fun updateRecycler(view: RecyclerView, data: List<Book>?) {
    view.adapter.apply {
        if (data != null) {
            (view.adapter as BookAdapter).let {
                it.clearList()
                it.updateList(data)
            }
        }
    }
}

@BindingAdapter("shelfDataList")
fun updateRecyclerShelf(view: RecyclerView, data: List<Shelf>?) {
    view.adapter.apply {
        if (data != null) {
            (view.adapter as EditShelfsShelfAdapter).updateList(data)
        }
    }
}

@BindingAdapter("createBookShelfs")
fun createBookShelfs(view: LinearLayout, shelfsList: List<Shelf>?) {
    view.removeAllViews()
    if (shelfsList != null) {
        for (shelf in shelfsList) {
            ComposableBookListBinding.inflate(
                LayoutInflater.from(view.context),
                view,
                true).also {
                it.listRecycler.apply {
                    layoutManager =
                        LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = BookAdapter()
                }
                it.books = shelf.books
                it.title = shelf.name
            }
        }
    }
}

@BindingAdapter("override_width")
fun overrideWidth(view: ConstraintLayout, type: LAYOUT) {
    if (type == LAYOUT.VERTICAL) {
        view.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
    } else {
        view.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
    }
}

@BindingAdapter("fillData")
fun fillData(view: RadioGroup, list: List<Shelf>?) {
    list?.forEach {
        view.addView(RadioButton(view.context).apply {
            text = it.name
        })
    }
}