package com.kamran.weatherforcast.ui.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.kamran.weatherforcast.R
import com.kamran.weatherforcast.data.api.State
import com.kamran.weatherforcast.data.model.*
import com.kamran.weatherforcast.ui.viewmodels.HomeActivityViewModel
import com.kamran.weatherforcast.utils.Alerts
import com.kamran.weatherforcast.utils.DateHelper
import com.link184.kidadapter.setUp
import com.link184.kidadapter.simple.SingleKidAdapter
import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.days
import com.xoxoer.lifemarklibrary.Lifemark
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.daily_recyclerview_list_item.view.*
import kotlinx.android.synthetic.main.hourly_recyclerview_list_item.view.*
import kotlinx.android.synthetic.main.home_content.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
@SuppressLint("SetTextI18n")
class HomeActivity : AppCompatActivity() {

    private val homeActivityViewModel: HomeActivityViewModel by viewModel()

    private var dayTime = "morning"
    private val localTimestamp = DateTimeTz.nowLocal()
    private val dateFormat =
        DateFormat("EE dd MMM yyyy") // Construct a new DateFormat from a String

    private lateinit var hourlyAdapter: SingleKidAdapter<Current>
    private lateinit var dailyAdapter: SingleKidAdapter<Daily>

    private var hours = mutableListOf<Current>()
    private var days = mutableListOf<Daily>()

    private val citiesData = mapOf(
        0 to "51.421509,35.694389",
        1 to "52.538799,29.6036",
        2 to "46.291901,38.080002"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_WeatherForcast)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        today.text = localTimestamp.format(dateFormat)

        setUpScreen()

        setBackgroundColor()

        setupCitiesSpinner()

        val networkConnection = Lifemark(applicationContext)
        networkConnection.ObservableNetworkCondition()
            .observe(this@HomeActivity, { isConnected ->
                if (isConnected) {
                    getCurrentWeatherForecast(
                        citiesData[0]?.split(",")?.first(),
                        citiesData[0]?.split(",")?.get(1)
                    )
                } else {
                    Alerts.showAlertDialogWithDefaultButton(
                        "Oops!",
                        getString(R.string.no_connection), "OK", this
                    )
                }
            })
    }

    private fun setupCitiesSpinner() {
        cities.hint = "Tehran"
        cities.apply {
            setItems(
                arrayListOf(
                    "Tehran",
                    "Shiraz",
                    "Tabriz"
                )
            )
            setOnSpinnerItemSelectedListener<String> { index, _ ->
                getCurrentWeatherForecast(
                    citiesData[index]?.split(",")?.first(),
                    citiesData[index]?.split(",")?.get(1)
                )
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setBackgroundColor() {
        var drw = R.drawable.morning_bg
        when (localTimestamp.hours) {
            in 0..6 -> {
                drw = R.drawable.dawn_bg
                dayTime = "dawn"
            }//min dawn
            in 7..11 -> {
                drw = R.drawable.morning_bg
                dayTime = "morning"
            }//morn
            in 12..16 -> {
                drw = R.drawable.noon_bg
                dayTime = "morning"
            }//max noon
            in 17..19 -> {
                drw = R.drawable.evening_bg
                dayTime = "evening"
            }//eve
            in 20..23 -> {
                drw = R.drawable.night_bg
                dayTime = "night"
            }//night
        }
        window.decorView.background = ContextCompat.getDrawable(this, drw)
    }

    @SuppressLint("SetTextI18n")
    private fun getCurrentWeatherForecast(lot: String?, lat: String?) {
        homeActivityViewModel.getWeatherForecast(lot!!, lat!!)
            .observe(this, { networkResource ->
                when (networkResource.state) {
                    State.LOADING -> {
                        loading_bar.visibility = View.VISIBLE
                        home_content.visibility = View.GONE
                    }
                    State.SUCCESS -> {
                        loading_bar.visibility = View.GONE
                        home_content.visibility = View.VISIBLE

                        val responseData = networkResource.data
                        setCurrentWeatherData(responseData!!)

                        hours = responseData.hourly.drop(1).take(12).toMutableList()
                        feedHours()

                        days = responseData.daily.take(7).toMutableList()
                        feedDays()
                    }
                    State.ERROR -> {
                        Alerts.showAlertDialogWithDefaultButton(
                            "Error!",
                            networkResource.message.toString(), "OK", this
                        )
                    }
                }
            })
    }

    private fun setCurrentWeatherData(data: Forecast) {
        city_text.text = data.timezone
        current_temp.text = getString(R.string.temp, setTemps(data.current.temp))
        setWeatherIconBasedOnDayTime(data.current.weather)
    }

    private fun setWeatherIconBasedOnDayTime(weather: List<Weather>) {
        when(weather.first().description){
            "clear sky" -> {
                weather_icon.setImageDrawable(setWeatherIcons(weather, "_$dayTime"))
            }
            "few clouds" -> {
                weather_icon.setImageDrawable(setWeatherIcons(weather, "_$dayTime"))
            }
            else -> weather_icon.setImageDrawable(setWeatherIcons(weather))
        }

    }

    @SuppressLint("StringFormatMatches")
    private fun feedHours() {
        hourlyAdapter = hourly_recyclerview.setUp<Current> {
            withLayoutManager(
                LinearLayoutManager(
                    applicationContext,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            )
            withLayoutResId(R.layout.hourly_recyclerview_list_item)
            withItems(hours)
            bindIndexed { hour, _ ->
                hourly_temp.text = getString(R.string.temp, setTemps(hour.temp))
                hourly_time.text = DateHelper.convertLongToTime(hour.dt.toLong())
                    .toLowerCase(Locale.getDefault())
                try {
                    hourly_icon.setImageDrawable(setWeatherIcons(hour.weather))
                } catch (e: Exception) {
                    //e.printStackTrace()
                }
            }
        }
    }

    @SuppressLint("StringFormatMatches")
    private fun feedDays() {
        val today = DateTimeTz.nowLocal()
        dailyAdapter = daily_recyclerview.setUp<Daily> {
            withLayoutResId(R.layout.daily_recyclerview_list_item)
            withItems(days)
            bindIndexed { day, position ->
                if (position == 0) day_title.text = getString(R.string.tomorrow)
                else day_title.text =
                    (today + (position + 1).days).dayOfWeek.toString()
                day_temp.text = getString(
                    R.string.temp_max_min,
                    setTemps(day.temp.max),
                    setTemps(day.temp.min)
                )
                try {
                    day_weather_icon.setImageDrawable(setWeatherIcons(day.weather))
                } catch (e: Exception) {
                    //e.printStackTrace()
                }
            }
        }
    }

    private fun setUpScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    private fun setWeatherIcons(weather: List<Weather>, dayTime:String = ""): Drawable? {
        return ContextCompat.getDrawable(
            applicationContext,
            resources.getIdentifier(
                weather.first().description.replace(
                    " ",
                    "_"
                )+dayTime,
                "drawable",
                applicationContext?.packageName
            )
        )
    }

    private fun setTemps(temp: String): Int {
        return temp.toFloat().roundToInt()
    }
}