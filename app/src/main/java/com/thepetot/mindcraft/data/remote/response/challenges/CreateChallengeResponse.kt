package com.thepetot.mindcraft.data.remote.response.challenges

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateChallengeResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("links")
	val links: Links,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: List<ErrorsItem?>?
): Parcelable

@Parcelize
data class ErrorsItem(

	@field:SerializedName("field")
	val field: String?,

	@field:SerializedName("messages")
	val messages: List<String>
): Parcelable

@Parcelize
data class Data(

	@field:SerializedName("summary")
	val summary: String,

	@field:SerializedName("totalQuestions")
	val totalQuestions: Int,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("timeSeconds")
	val timeSeconds: Int,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("authorId")
	val authorId: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("tags")
	val tags: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String,

	@field:SerializedName("authorFirstName")
	val authorFirstName: String,

	@field:SerializedName("authorLastName")
	val authorLastName: String
): Parcelable

@Parcelize
data class Links(

	@field:SerializedName("createUserParticipation")
	val createUserParticipation: String,

	@field:SerializedName("deleteUserChallenge")
	val deleteUserChallenge: String,

	@field:SerializedName("userChallenges")
	val userChallenges: String,

	@field:SerializedName("createUserChallenge")
	val createUserChallenge: String,

	@field:SerializedName("updateUserChallenge")
	val updateUserChallenge: String,

	@field:SerializedName("deleteUser")
	val deleteUser: String,

	@field:SerializedName("self")
	val self: String,

	@field:SerializedName("updateUser")
	val updateUser: String,

	@field:SerializedName("userDetails")
	val userDetails: String,

	@field:SerializedName("userParticipations")
	val userParticipations: String
): Parcelable
