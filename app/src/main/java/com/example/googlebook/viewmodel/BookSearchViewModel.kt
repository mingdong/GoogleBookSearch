package com.example.googlebook.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlebook.BookRepository
import com.example.googlebook.api.createApiService
import com.example.googlebook.repository.RemoteBookRepository
import com.example.googlebook.data.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class BookSearchViewModel : ViewModel() {

  private val _searchState = MutableStateFlow(State.Success(emptyList()))
  val searchState: StateFlow<State> = _searchState

  var uiQueryString = MutableLiveData<String>()
  var selectedBook = MutableLiveData<Pair<Int, Book>>()
  val repo = RemoteBookRepository(createApiService())

  init {
    searchBook("androiddev")
  }

  fun checkBook(book: Pair<Int, Book>) {
    selectedBook.value = book
  }

  fun searchBook(query: String) {
    uiQueryString.value = query
    viewModelScope.launch {
      repo.fetchBooks(query).consumeOnEach { books ->
        _searchState.value = State.Success(books)
      }
    }
  }
}

sealed class State {
  data class Success(val books: List<Book>): State()
  data class Error(val exception: Throwable): State()
}
