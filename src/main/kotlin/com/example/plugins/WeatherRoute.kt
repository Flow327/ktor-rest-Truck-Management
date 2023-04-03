package com.example.plugins

import com.example.getWeather
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.weatherRoute(){
    get("/weather") {
        val weatherResponse = getWeather()
        println(weatherResponse)

        // Pass the data to FreeMarker template
        call.respond(FreeMarkerContent("weather.ftl", mapOf("weatherResponse" to weatherResponse)))
    }
}