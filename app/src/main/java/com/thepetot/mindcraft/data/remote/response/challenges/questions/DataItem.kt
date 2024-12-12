package com.thepetot.mindcraft.data.remote.response.challenges.questions

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class DataItem(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("challengeId")
	val challengeId: Int,

	@field:SerializedName("question")
	val question: String,

	@field:SerializedName("answers")
	val answers: List<AnswersItem>,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("checked")
	var checked: Int? = null,

	@field:SerializedName("explanation")
	val explanation: String
) : Parcelable