package com.example.googlebook.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.googlebook.databinding.BookListItemBinding
import com.example.googlebook.viewmodel.clicks
import com.example.googlebook.viewmodel.conflated
import com.example.googlebook.viewmodel.sendTo
import com.example.googlebook.data.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

  private val data = mutableListOf<Book>()

  private var itemClicksChannel: Channel<Pair<Int, Book>>? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    ViewHolder(
      viewBinding = BookListItemBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
      )
    )

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(data[position])
    holder.itemView.clicks().map { position to data[position] }.sendTo(Dispatchers.IO, ::itemClicksChannel)
  }

  override fun getItemCount(): Int = data.size

  fun itemClicks(): Flow<Pair<Int, Book>> = conflated { itemClicksChannel = this }

  fun setData(newData: List<Book>) {
    data.clear()
    data.addAll(newData)
    notifyDataSetChanged()
  }

  class ViewHolder(val viewBinding: ViewBinding) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(book: Book) {
      (viewBinding as BookListItemBinding).run {
        Glide.with(viewBinding.root).load(book.thumbnailUrl).into(image)
        viewBinding.title.text = book.title
      }
    }
  }
}
