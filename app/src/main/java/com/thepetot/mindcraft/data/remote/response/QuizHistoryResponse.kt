package com.thepetot.mindcraft.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class QuizHistoryResponse(

    @field:SerializedName("listQuiz")
    val listQuiz: List<ListQuizItem>,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String
)

@Parcelize
data class ListQuizItem(

    @field:SerializedName("duration")
    val duration: Int,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("question")
    val question: Int,

    @field:SerializedName("author")
    val author: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("title")
    val title: String
) : Parcelable