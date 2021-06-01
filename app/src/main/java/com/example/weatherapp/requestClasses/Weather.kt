package com.example.weatherapp.requestClasses

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException
import org.json.JSONObject

interface WeatherIdCallBack {
    fun onResponse(res: String)

    fun onError(message: String)
}

interface WeatherByIdCallBack {
    fun onResponse(res: JSONObject)

    fun onError(message: String)
}

class Weather(val context: Context) {


    companion object{
        const val BASE_URL = "https://www.metaweather.com/api/location/search/?query="
        const val LOCATION_URL = "https://www.metaweather.com/api/location/"
    }

    fun getCityId(cityName: String, callBack: WeatherIdCallBack){
            var cityId = ""
            val url = BASE_URL + cityName

            val jsonRequest = JsonArrayRequest(Request.Method.GET, url, null, { response ->
                try {
                    val city = response.getJSONObject(0)
                    cityId = city.getString("woeid")
                } catch (e: JSONException){
                    Log.w("MainActivity", e)
                }
                callBack.onResponse(cityId)
            }, {err ->
                Log.w("MainActivity", err)
                callBack.onError("Something went wrong! :(")
            })

            MySingleton.getInstance(context).addToRequestQueue(jsonRequest)


    }

    fun getWeatherById(id:String, callBack: WeatherByIdCallBack) {
        val url = LOCATION_URL + id
        val jsonRequest = JsonObjectRequest(Request.Method.GET, url, null, {response->

            // get weather object for present day
            val weatherObj = response.getJSONArray("consolidated_weather").getJSONObject(0)
            callBack.onResponse(weatherObj)

        }, {err->
            callBack.onError("Something went awry!!!")
            println(err)
        })

        MySingleton.getInstance(context).addToRequestQueue(jsonRequest)

    }

    fun getWeatherByName(cityName: String, callBack: WeatherByIdCallBack){
        this.getCityId(cityName, object:WeatherIdCallBack{
            override fun onResponse(res: String) {
                getWeatherById(res,  callBack)
            }

            override fun onError(message: String) {
                Log.w("MainActivity", message)
            }
        })

    }

}