package com.thepetot.mindcraft.data.remote.response.participations

import com.google.gson.annotations.SerializedName
import com.thepetot.mindcraft.data.remote.response.user.Links

data class ParticipationData(
    @SerializedName("id") val id: Int,
    @SerializedName("participantId") val participantId: String,
    @SerializedName("challengeId") val challengeId: String,
    @SerializedName("score") val score: Int,
    @SerializedName("createdAt") val createdAt: String
)

data class ParticipationResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ParticipationData,
    @SerializedName("links") val links: Links
)

data class AllParticipationsResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<ParticipationData>,
    @SerializedName("links") val links: Links
)
