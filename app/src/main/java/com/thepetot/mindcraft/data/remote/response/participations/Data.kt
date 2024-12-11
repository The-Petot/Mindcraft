package com.thepetot.mindcraft.data.remote.response.participations

import com.google.gson.annotations.SerializedName

data class Data(

	@field:SerializedName("participantId")
	val participantId: Int,

	@field:SerializedName("score")
	val score: Int,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("challengeId")
	val challengeId: Int,

	@field:SerializedName("id")
	val id: Int
)