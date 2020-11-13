/*
 * Creator: Kamran Noorinejad on 6/15/20 2:07 PM
 * Last modified: 6/15/20 2:07 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kamran.weatherforcast.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kamran.weatherforcast.core.Constants.dbName
import com.kamran.weatherforcast.data.db.dao.CategoryDao
import com.kamran.weatherforcast.data.model.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by Kamran Noorinejad on 6/15/2020 AD 14:07.
 * Edited by Kamran Noorinejad on 6/15/2020 AD 14:07.
 */
@Database(entities = [Category::class], version = 1, exportSchema = false)
abstract class LeftoriteDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    companion object {

        // @Volatile
        //private var INSTANCE: LeftoriteDatabase? = null

        private lateinit var INSTANCE: LeftoriteDatabase

        fun getDatabase(context: Context): LeftoriteDatabase? {
            // if (INSTANCE == null) {
            synchronized(LeftoriteDatabase::class.java) {
                // if (INSTANCE == null) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        LeftoriteDatabase::class.java, dbName
                    ).build()
                }
            }
            //  }
            return INSTANCE
        }

        fun clearTables() {
            GlobalScope.launch(Dispatchers.IO) {
                INSTANCE.clearAllTables()
            }
        }
    }

}