package com.thepetot.mindcraft.data.remote.response.challenges.questions

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class AnswersItem(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("questionId")
	val questionId: Int,

	@field:SerializedName("answer")
	val answer: String,

	@field:SerializedName("correct")
	val correct: Boolean,

	@field:SerializedName("id")
	val id: Int
) : Parcelable