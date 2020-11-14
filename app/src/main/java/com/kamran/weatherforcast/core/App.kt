package com.kamran.weatherforcast.core

import android.app.Application
import com.kamran.weatherforcast.di.apiModule
import com.kamran.weatherforcast.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Created by Kamran Noorinejad on 11/7/2020 AD 13:40.
 * Edited by Kamran Noorinejad on 11/7/2020 AD 13:40.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@App)
            modules(
                viewModelModule,
                apiModule
            )
        }
    }
}