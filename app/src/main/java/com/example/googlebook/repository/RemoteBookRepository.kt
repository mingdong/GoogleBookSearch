package com.example.googlebook.repository

import com.example.googlebook.BookRepository
import com.example.googlebook.api.GoogleBookApi
import com.example.googlebook.data.Book
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteBookRepository(
  private val bookApi: GoogleBookApi,
  val dispatcher: CoroutineDispatcher = IO
) : BookRepository {
  override suspend fun fetchBooks(searchQuery: String): Flow<List<Book>> {
    return flow {
      val value = bookApi.searchBooks(searchQuery).items.map {
        Book(
          it.id,
          it.volumeInfo.title,
          it.volumeInfo.description ?: "",
          it.volumeInfo.authors ?: emptyList(),
          it.volumeInfo.imageLinks?.thumbnail ?: ""
        )
      }
      emit(value)
    }.flowOn(dispatcher)
  }
}
