package com.moonlightbutterfly.bookid

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.moonlightbutterfly.bookid.adapters.BookAdapter
import com.moonlightbutterfly.bookid.adapters.LAYOUT
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.viewmodels.BookViewModel

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
            (view.adapter as BookAdapter).updateList(data)
        }
    }
}

@BindingAdapter("override_width")
fun overrideWidth(view: LinearLayout, type: LAYOUT) {
    if (type == LAYOUT.VERTICAL) {
        view.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
    } else {
        view.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
    }
}