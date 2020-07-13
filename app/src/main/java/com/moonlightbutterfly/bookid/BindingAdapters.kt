package com.moonlightbutterfly.bookid

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.moonlightbutterfly.bookid.adapters.BookAdapter
import com.moonlightbutterfly.bookid.adapters.EditShelfsShelfAdapter
import com.moonlightbutterfly.bookid.adapters.LAYOUT
import com.moonlightbutterfly.bookid.databinding.ComposableBookListBinding
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf


@BindingAdapter("loadImage", "drawerManager", requireAll = false)
fun loadImage(view: ImageView, url: String?, drawerManager: DrawerManager?) {
    Glide.with(view.context)
        .load(url)
        .placeholder(R.drawable.ic_launcher_foreground)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                drawerManager?.paintDrawer(resource?.toBitmap())
                return false
            }
        })
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
                    layoutManager = LinearLayoutManager(
                        view.context,
                        LinearLayoutManager.HORIZONTAL,
                        false)
                    addItemDecoration(CustomItemDecoration(LAYOUT.HORIZONTAL))
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
@BindingAdapter("verticalBias")
fun verticalBias(view: ProgressBar, value: Float) {
    val params = view.layoutParams as ConstraintLayout.LayoutParams
    params.verticalBias = value
    view.layoutParams = params
}