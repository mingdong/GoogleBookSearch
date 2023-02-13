package com.example.googlebook

import com.example.googlebook.data.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
  suspend fun fetchBooks(searchQuery: String): Flow<List<Book>>
}
