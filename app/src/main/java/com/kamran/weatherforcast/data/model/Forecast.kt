package com.kamran.weatherforcast.data.model

import com.google.gson.annotations.SerializedName

data class Forecast(
    @SerializedName("cod")
    var cod: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("timezone")
    var timezone: String,
    @SerializedName("current")
    var current: Current,
    @SerializedName("hourly")
    var hourly: List<Current>,
    @SerializedName("daily")
    var daily: List<Daily>
)