package com.thepetot.mindcraft.data.remote.response.participations

import com.google.gson.annotations.SerializedName

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
)