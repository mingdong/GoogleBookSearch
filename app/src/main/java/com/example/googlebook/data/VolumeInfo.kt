package com.example.googlebook.data

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ImageLinksResource(
  @Json(name = "thumbnail") val thumbnail: String?
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class VolumeInfo(
  @Json(name = "title") val title: String,
  @Json(name = "subtitle") val subtitle: String?,
  @Json(name = "authors") val authors: List<String>?,
  @Json(name = "imageLinks") val imageLinks: ImageLinksResource?,
  @Json(name = "description") val description: String?
) : Parcelable
