/*
 * Creator: Kamran Noorinejad on 5/17/20 10:48 AM
 * Last modified: 5/17/20 10:48 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kamran.weatherforcast.ui.viewmodels

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kamran.weatherforcast.R
import com.kamran.weatherforcast.core.Constants
import com.kamran.weatherforcast.data.api.ApiClientProvider
import com.kamran.weatherforcast.data.api.Resource
import com.kamran.weatherforcast.data.model.Category
import com.kamran.weatherforcast.data.repository.DbRepository
import com.kamran.weatherforcast.ui.activities.HomeActivity
import com.kamran.weatherforcast.utils.ConfigPref
import com.kamran.weatherforcast.utils.ShowCase
import com.kamran.weatherforcast.utils.UserInfo
import kotlinx.coroutines.Dispatchers

/**
 * Created by Kamran Noorinejad on 5/17/2020 AD 10:48.
 * Edited by Kamran Noorinejad on 5/17/2020 AD 10:48.
 */
class HomeActivityViewModel(
    private val context: Context,
    apiProvider: ApiClientProvider
    //  private val dbRepository: DbRepository
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