package com.example.plugins

import com.example.getWeather
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.weatherDataRoute() {
    get("/weather-data") {
        val weatherResponse = getWeather()
        // Respond with the JSON data
        call.respond(weatherResponse)
    }
}




