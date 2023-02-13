package com.example.googlebook.data

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class BooksResponse(
  @Json(name = "totalItems") val totalItems: Int,
  @Json(name = "items") val items: List<BookResult>
) : Parcelable
