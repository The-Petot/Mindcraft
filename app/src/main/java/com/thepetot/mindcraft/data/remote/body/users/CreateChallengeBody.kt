package com.thepetot.mindcraft.data.remote.body.users

import com.google.gson.annotations.SerializedName

data class CreateChallengeBody(

	@field:SerializedName("timeSeconds")
	val timeSeconds: Int,

	@field:SerializedName("material")
	val material: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("tags")
	val tags: List<String>
)