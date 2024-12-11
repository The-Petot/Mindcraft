package com.thepetot.mindcraft.data.remote.response.challenges.test

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class Links(

	@field:SerializedName("challengeParticipants")
	val challengeParticipants: String,

	@field:SerializedName("challengeQuestions")
	val challengeQuestions: String,

	@field:SerializedName("challengeDetails")
	val challengeDetails: String,

	@field:SerializedName("self")
	val self: String
) : Parcelable