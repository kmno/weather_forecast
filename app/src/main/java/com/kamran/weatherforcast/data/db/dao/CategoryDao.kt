/*
 * Creator: Kamran Noorinejad on 6/15/20 2:02 PM
 * Last modified: 6/15/20 2:02 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */
package com.kamran.weatherforcast.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kamran.weatherforcast.core.Constants.dbName
import com.kamran.weatherforcast.data.model.Category
import kotlinx.coroutines.flow.Flow

/**
 * Created by Kamran Noorinejad on 6/15/2020 AD 14:02.
 * Edited by Kamran Noorinejad on 6/15/2020 AD 14:02.
 */

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: Category)

    @Query("SELECT * from ${dbName}_categories WHERE is_active=1 ORDER BY id DESC")
    fun getCategories(): Flow<List<Category>>

    @Query("SELECT * from ${dbName}_categories WHERE id=:catId")
    fun getCategoryById(catId: Int): Category

    @Query("DELETE FROM ${dbName}_categories")
    fun deleteAll()

}