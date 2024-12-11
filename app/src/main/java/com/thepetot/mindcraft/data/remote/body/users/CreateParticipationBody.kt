package com.thepetot.mindcraft.data.remote.body.users

import com.google.gson.annotations.SerializedName

data class CreateParticipationBody(

	@field:SerializedName("score")
	val score: Int,

	@field:SerializedName("challengeId")
	val challengeId: Int
)