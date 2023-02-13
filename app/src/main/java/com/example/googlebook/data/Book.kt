package com.example.googlebook.data

data class Book(
  val id: String,
  val title: String,
  val description: String,
  val authors: List<String>,
  val thumbnailUrl: String? = null
)
