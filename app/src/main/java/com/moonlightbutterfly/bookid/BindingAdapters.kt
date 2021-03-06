package com.moonlightbutterfly.bookid

import android.view.View
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.moonlightbutterfly.bookid.adapters.BookAdapter
import com.moonlightbutterfly.bookid.adapters.ShelfAdapter
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.utils.DefaultShelf
import com.moonlightbutterfly.bookid.viewmodels.ProfileViewModel

@BindingAdapter("loadImage")
fun loadImage(view: ImageView, url: String?) {
    Glide.with(view.context)
        .load(url)
        .placeholder(R.drawable.ic_launcher_foreground)
        .transition(DrawableTransitionOptions.withCrossFade())
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
fun updateRecyclerBooks(view: RecyclerView, data: List<Book>?) {
    view.adapter.apply {
        if (data != null && data != (this as BookAdapter).books) {
            (view.adapter as BookAdapter).let {
                it.clearList()
                it.updateList(data)
            }
        }
    }
}

@BindingAdapter("dataList")
fun updateRecyclerShelfs(view: RecyclerView, data: List<Shelf>?) {
    view.adapter.apply {
        if ((data != null && data != (this as ShelfAdapter).shelfs) ||
                data?.map { it.name } != (this as ShelfAdapter).shelfs.map { it.name }) {
            (view.adapter as ShelfAdapter).updateList(data)
        }
    }
}

@BindingAdapter("fillData")
fun fillData(view: RadioGroup, list: List<Shelf>?) {
    list?.forEach {
        if (!DefaultShelf.matches(it.id)) {
            view.addView(RadioButton(view.context).apply {
                text = it.name
            })
        }
    }
}

@BindingAdapter("verticalBias")
fun verticalBias(view: ProgressBar, value: Float) {
    val params = view.layoutParams as ConstraintLayout.LayoutParams
    params.verticalBias = value
    view.layoutParams = params
}

@BindingAdapter("populateChips", "actionHandler")
fun populateChips(view: ChipGroup, shelfsList: List<Shelf>?, viewModel: ProfileViewModel) {
    view.removeAllViews()
    shelfsList?.forEach { shelf ->
        if (!DefaultShelf.matches(shelf.baseShelfId)) {
            val chip = Chip(view.context, null, 0)
            chip.chipBackgroundColor = AppCompatResources.getColorStateList(view.context, R.color.chips_color)
            chip.setOnClickListener { viewModel.updateBaseShelf(shelf.id) }
            chip.isSelected = shelf.id == viewModel.baseShelfLiveData.value?.id
            chip.text = shelf.name
            view.addView(chip)
        }
    }
}

@BindingAdapter("updateChipsCheck")
fun updateChipsCheck(view: ChipGroup, shelf: Shelf?) {
    view.children.forEach { (it as Chip).isSelected = it.text == shelf?.name }
}