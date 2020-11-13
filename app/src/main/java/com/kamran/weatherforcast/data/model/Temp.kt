package com.kamran.weatherforcast.data.model

import com.google.gson.annotations.SerializedName

data class Temp(
    @SerializedName("min")
    var min: String,
    @SerializedName("day")
    var day: String,
    @SerializedName("night")
    var night: String,
    @SerializedName("eve")
    var eve: String,
    @SerializedName("morn")
    var morn: String,
    @SerializedName("max")
    var max: String
)