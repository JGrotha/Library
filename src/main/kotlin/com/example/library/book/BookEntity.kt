package com.example.library.book

import java.time.LocalDate

data class BookEntity(
    val id: Long?,
    val title: String,
    val author: String,
    val isbn: String,
    val publishedAt: LocalDate
) {

    companion object {

        fun fromRequest(request: BookDto): BookEntity =
            BookEntity(
                id = null,
                title = request.title,
                author = request.author,
                isbn = request.isbn,
                publishedAt = request.publishedDate
            )
    }
}
