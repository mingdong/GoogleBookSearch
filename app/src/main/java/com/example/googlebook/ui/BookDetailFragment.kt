package com.example.googlebook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.googlebook.databinding.FragmentBookDetailBinding
import com.example.googlebook.viewmodel.BookSearchViewModel

class BookDetailFragment : Fragment() {
  private var binding: FragmentBookDetailBinding? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ) = FragmentBookDetailBinding.inflate(inflater, container, false)
    .also { binding = it }
    .root

  private val viewModel: BookSearchViewModel by activityViewModels()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel.selectedBook.observe(viewLifecycleOwner) { current ->
      binding?.apply {
        tvTitle.text = current.second.title
        tvDescription.text = current.second.description
        tvAuthor.text = current.second.authors.joinToString(";")
        Glide.with(view).load(current.second.thumbnailUrl).into(image)
      }
    }
  }
}
