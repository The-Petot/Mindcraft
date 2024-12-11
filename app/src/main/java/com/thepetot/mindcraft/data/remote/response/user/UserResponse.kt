package com.thepetot.mindcraft.data.remote.response.user

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("links")
	val links: Links,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: List<ErrorsItem>
)

data class ErrorsItem(

	@field:SerializedName("field")
	val field: String,

	@field:SerializedName("messages")
	val messages: List<String>
)

data class Data(

	@field:SerializedName("currentRank")
	val currentRank: Int,

	@field:SerializedName("firstName")
	val firstName: String,

	@field:SerializedName("lastName")
	val lastName: String,

	@field:SerializedName("twoFactorEnabled")
	val twoFactorEnabled: Boolean,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("profileImgUrl")
	val profileImgUrl: String,

	@field:SerializedName("twoFactorSecret")
	val twoFactorSecret: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("totalScore")
	val totalScore: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("notificationEnabled")
	val notificationEnabled: Boolean,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)

data class Links(

	@field:SerializedName("createUserParticipation")
	val createUserParticipation: String,

	@field:SerializedName("deleteUserChallenge")
	val deleteUserChallenge: String,

	@field:SerializedName("userChallenges")
	val userChallenges: String,

	@field:SerializedName("createUserChallenge")
	val createUserChallenge: String,

	@field:SerializedName("deleteUser")
	val deleteUser: String,

	@field:SerializedName("self")
	val self: String,

	@field:SerializedName("updateUser")
	val updateUser: String,

	@field:SerializedName("users")
	val users: String,

	@field:SerializedName("userParticipations")
	val userParticipations: String
)
