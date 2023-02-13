package com.example.googlebook

import app.cash.turbine.test
import com.example.googlebook.api.GoogleBookApi
import com.example.googlebook.data.BookResult
import com.example.googlebook.data.BooksResponse
import com.example.googlebook.repository.RemoteBookRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RemoteBookRepositoryTest {
  val apiServiceMock: GoogleBookApi = mockk(relaxed = true)
  val bookResult: BookResult = mockk(relaxed = true)

  @Test
  fun testEmptyResult() = runBlocking {
    coEvery { apiServiceMock.searchBooks(any()) } returns
      BooksResponse(totalItems = 0, items = emptyList())
    val bookRepo = RemoteBookRepository(apiServiceMock, TestCoroutineDispatcher())
    bookRepo.fetchBooks("mockk").test {
      assertEquals(0, awaitItem().size)
      awaitComplete()
    }
  }

  @Test
  fun testNonEmptyResult() = runBlocking {
    coEvery { apiServiceMock.searchBooks(any()) } returns
      BooksResponse(totalItems = 1, items = listOf(bookResult))

    val bookRepo = RemoteBookRepository(apiServiceMock, TestCoroutineDispatcher())
    bookRepo.fetchBooks("mockk").test {
      assertEquals(1, awaitItem().size)
      awaitComplete()
    }
  }
}
