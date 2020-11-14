/*
 * Creator: Kamran Noorinejad on 5/17/20 10:48 AM
 * Last modified: 5/17/20 10:48 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kamran.weatherforcast.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kamran.weatherforcast.R
import com.kamran.weatherforcast.core.Constants
import com.kamran.weatherforcast.data.api.ApiClientProvider
import com.kamran.weatherforcast.data.api.Resource
import kotlinx.coroutines.Dispatchers
import kotlin.math.roundToInt

/**
 * Created by Kamran Noorinejad on 5/17/2020 AD 10:48.
 * Edited by Kamran Noorinejad on 5/17/2020 AD 10:48.
 */
class HomeActivityViewModel(
    private val context: Context,
    apiProvider: ApiClientProvider
) :
    ViewModel() {

    private val api = apiProvider.createApiClient()

    //get all items list
    fun getWeatherForecast(lot: String, lat: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = api.getWeatherForecast(lat, lot, Constants.units, Constants.appid)
            if (response.isSuccessful) {
                emit(
                    Resource.success(response.body())
                )
            } else {
                emit(
                    Resource.error(
                        response.body()?.cod,
                        response.body()?.message,
                        null
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(
                Resource.error(
                    222,
                    context.getString(R.string.network_error_text),
                    null
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}