package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.requestClasses.Weather
import com.example.weatherapp.requestClasses.WeatherByIdCallBack
import com.example.weatherapp.requestClasses.WeatherIdCallBack
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private var myList = mutableListOf<String>()
    private lateinit var binding: ActivityMainBinding
    lateinit var adapter: ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, myList)
        binding.weatherReportList.adapter = adapter

        // Set listeners for each button
        binding.getCityIdBtn.setOnClickListener {
            val city = binding.enterCityText.text.toString()
            Weather(this).getCityId(city, object:WeatherIdCallBack{
                override fun onResponse(res: String) {
                    Toast.makeText(this@MainActivity, "ID of $city is $res", Toast.LENGTH_LONG).show()
                }

                override fun onError(message: String) {
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
                }
            })
        }


        binding.weatherByIdBtn.setOnClickListener {
            val id = binding.enterCityText.text.toString()
            Weather(this).getWeatherById(id, object:WeatherByIdCallBack {
                override fun onResponse(res: JSONObject) {
                    processResponse(res)
                }

                override fun onError(message: String) {
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
                }
            })
        }

        binding.weatherByNameBtn.setOnClickListener {
            val city = binding.enterCityText.text.toString()
            Weather(this).getWeatherByName(city, object:WeatherByIdCallBack {
                override fun onResponse(res: JSONObject) {
                    processResponse(res)
                }

                override fun onError(message: String) {
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun processResponse(res: JSONObject) {
        val keyz = res.keys()
        myList.clear()

        while (keyz.hasNext()){
            val key = keyz.next()
            val value = res.getString(key)
            val formattedString = "$key \t - \t $value"
            myList.add(formattedString)
        }

        adapter.notifyDataSetChanged()

    }
}
