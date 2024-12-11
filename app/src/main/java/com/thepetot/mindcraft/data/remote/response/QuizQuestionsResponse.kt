package com.thepetot.mindcraft.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class QuizQuestionsResponse(

    @field:SerializedName("listQuestions")
    val listQuestions: List<ListQuestionsItem>,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String
)

@Parcelize
data class ListQuestionsItem(

    @field:SerializedName("question")
    val question: String,

    @field:SerializedName("options")
    val options: Options,

    @field:SerializedName("correct_answer")
    val correctAnswer: String,

    @field:SerializedName("explanation")
    val explanation: String
) : Parcelable

@Parcelize
data class Options(

    @field:SerializedName("A")
    val a: String,

    @field:SerializedName("B")
    val b: String,

    @field:SerializedName("C")
    val c: String,

    @field:SerializedName("D")
    val d: String
) : Parcelable