package com.thepetot.mindcraft.data.remote.response.user.all_users

import com.google.gson.annotations.SerializedName

data class DataItem(

	@field:SerializedName("currentRank")
	val currentRank: Int,

	@field:SerializedName("firstName")
	val firstName: String,

	@field:SerializedName("lastName")
	val lastName: String,

	@field:SerializedName("profileImgUrl")
	val profileImgUrl: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("totalScore")
	val totalScore: Int,

	@field:SerializedName("email")
	val email: String
)