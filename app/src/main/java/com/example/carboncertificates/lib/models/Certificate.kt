package com.example.carboncertificates.lib.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Certificate(
    @Json(name = "id")
    val id: String,
    @Json(name = "originator")
    val originator: String,
    @Json(name = "originator-country")
    val originatorCountry: String,
    @Json(name = "owner")
    val owner: String,
    @Json(name = "owner-country")
    val ownerCountry: String,
    @Json(name = "status")
    val status: String
)