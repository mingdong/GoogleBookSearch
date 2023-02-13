package com.example.googlebook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googlebook.R
import com.example.googlebook.databinding.FragmentBookSearchBinding
import com.example.googlebook.viewmodel.BookSearchViewModel
import com.example.googlebook.viewmodel.State.Success
import com.example.googlebook.viewmodel.consumeOnEach
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class BookSearchFragment : Fragment(), CoroutineScope by MainScope() {

  private lateinit var binding: FragmentBookSearchBinding

  private val bookList = ListAdapter()

  private val viewModel: BookSearchViewModel by activityViewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ) = FragmentBookSearchBinding.inflate(inflater, container, false)
      .also { binding = it }
      .root

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.apply {
      resultList.apply {
        layoutManager = LinearLayoutManager(view.context)
        adapter = bookList
      }

      searchbookView.apply {
        isIconifiedByDefault = false
        queryHint = "androiddev"

        setOnQueryTextListener(
          object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
              query?.let {
                clearFocus()
                viewModel.searchBook(query)
              }

              return false
            }

            override fun onQueryTextChange(p0: String?) = false
          }
        )
      }

      bookList.itemClicks().consumeOnEach(Main) {
        viewModel.checkBook(it)
        Navigation.findNavController(view).navigate(R.id.action_open_book_detail)
      }
    }

    launch {
      viewModel.searchState.consumeOnEach(Main) {
        when (it) {
          is Success -> bookList.setData(it.books)
          else       -> Unit
        }

        viewModel.selectedBook.observe(viewLifecycleOwner) { selectedBook ->
          binding.resultList.scrollToPosition(selectedBook.first)
        }
      }
    }
  }
}
