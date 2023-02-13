package com.example.googlebook.api

import com.example.googlebook.data.BooksResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GoogleBookApi {
  companion object {
    const val BASE_URL = "https://www.googleapis.com/books/v1/"
  }

  @GET("volumes")
  suspend fun searchBooks(
    @Query("q") searchString: String,
    @Query("startIndex") startIndex: Int = 0,
    @Query("maxResults") maxResults: Int = 40,
  ): BooksResponse

  @GET("volumes/{id}")
  suspend fun getBook(
    @Path("id") id: String
  ): BooksResponse
}
