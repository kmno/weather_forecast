/*
 * Creator: Kamran Noorinejad on 6/15/20 2:14 PM
 * Last modified: 6/15/20 2:14 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kamran.weatherforcast.data.repository

import android.app.Application
import android.content.Context
import com.kmno.leftorite.R
import com.kmno.leftorite.core.App
import com.kmno.leftorite.data.api.ApiClientProvider
import com.kmno.leftorite.data.api.Resource
import com.kmno.leftorite.data.db.LeftoriteDatabase
import com.kmno.leftorite.data.db.dao.CategoryDao
import com.kmno.leftorite.data.db.dao.ItemDao
import com.kmno.leftorite.data.model.Category
import com.kmno.leftorite.utils.NetworkInfo
import com.kmno.leftorite.utils.UserInfo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by Kamran Noorinejad on 6/15/2020 AD 14:14.
 * Edited by Kamran Noorinejad on 6/15/2020 AD 14:14.
 */

class DbRepository(
    _context: Context,
    application: Application,
    apiProvider: ApiClientProvider,
    netInfo: NetworkInfo
) : CoroutineScope {

    private val context = _context
    private val api = apiProvider.createApiClient()
    private val networkState = netInfo

    private var status = true
    private var message = "OFFLINE"

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var categoryDao: CategoryDao?
    private var itemDao: ItemDao?

    init {
        val db = LeftoriteDatabase.getDatabase(application)
        categoryDao = db?.categoryDao()
        itemDao = db?.itemDao()
    }

    /* CATEGORIES */
    /**
     * check if network available
     * then get the latest list of categories and store them in db
     **/

    private fun getCategoriesListFlowOffline(): Flow<Resource<List<Category>>>? {
        return categoryDao?.getCategories()?.map {
            mapDbToDomain(it)
        }?.flowOn(Dispatchers.IO)?.catch {
            emit(
                Resource.error(
                    false,
                    context.getString(R.string.network_error_text),
                    null
                )
            )
        }
    }

    private fun getCategoriesListFlowOnline(): Flow<Resource<List<Category>>>? {
        return flow {
            try {
                val response = api.getCategories(UserInfo.id, UserInfo.token)
                if (response.isSuccessful) {
                    status = response.body()?.status ?: true
                    message = response.body()?.response?.message ?: ""
                    response.body()?.response?.data?.let {
                        refreshCategoriesOfflineCache(it)
                        emit(mapDbToDomain(it))
                    }
                } else {
                    emit(
                        Resource.error(
                            response.body()?.status,
                            response.body()?.response?.message,
                            null
                        )
                    )
                }
            } catch (e: Exception) {
                emit(
                    Resource.error(
                        false,
                        context.getString(R.string.network_error_text),
                        null
                    )
                )
            }
        }
            .flowOn(Dispatchers.IO)
            .catch {
                emit(
                    Resource.error(
                        false,
                        context.getString(R.string.network_error_text),
                        null
                    )
                )
            }
    }

    private fun mapDbToDomain(list: List<Category>): Resource<List<Category>> {
        if (list.isEmpty()) status = false
        return Resource.success(status, message, data = list)
    }

    fun getCategoriesList(): Flow<Resource<List<Category>>>? {
        if (networkState.isOnline()) return getCategoriesListFlowOnline()
        return getCategoriesListFlowOffline()
    }

    /*suspend fun getCategoriesList(): Resource<List<Category>>? {
        if (networkState.isOnline()) {
            try {
                val response = api.getCategories(UserInfo.id, UserInfo.token)
                if (response.isSuccessful) {
                    status = response.body()?.status ?: true
                    message = response.body()?.response?.message ?: ""
                    response.body()?.response?.data?.let {
                        refreshCategoriesOfflineCache(it)
                    }
                } else {
                    return Resource.error(
                        response.body()?.status,
                        response.body()?.response?.message,
                        null
                    )
                }
            } catch (e: Exception) {
                return Resource.error(
                    false,
                    context.getString(R.string.network_error_text),
                    null
                )
            }
        }
        return withContext(Dispatchers.IO) {
            Resource.success(
                status,
                message,
                categoryDao?.getCategories()
            )
        }
    }*/

    /**
     * Refresh the categories stored in the offline cache.
     **/
    private suspend fun refreshCategoriesOfflineCache(categories: List<Category>) {
        try {
            val insertJob = GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    categories.forEach { record ->
                        App.logger.error("$record inserted ")
                        categoryDao?.insertCategory(record)
                    }
                }
            }
            insertJob.join()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}