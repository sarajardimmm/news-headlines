package com.example.bbcheadlines.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val title: String,
    val description: String?,
    val content: String?,
    val imageUrl: String?,
    val url: String?,
    val publishedAt: String?,
    val publishedAtRaw: String?
) : Parcelable