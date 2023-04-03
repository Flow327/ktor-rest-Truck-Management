package com.example

import com.beust.klaxon.Klaxon
import com.example.dao.DAOFacadeDatabase
import com.example.model.WeatherResponse
import com.example.plugins.configureRoutes
import freemarker.cache.ClassTemplateLoader
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.util.*
import io.ktor.websocket.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.exposed.sql.Database
import org.slf4j.event.*
import java.util.*
import javax.mail.*

// Create a DAO facade object to connect to the database
val dao = DAOFacadeDatabase(
    Database.connect(
        "jdbc:sqlserver://PWD-WCTSQLL01;databaseName=YardManagement;encrypt=false;integratedSecurity=true;",
        driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
    )
)

// Main function to start the server
fun main() {
// Start the server on port 8080
    embeddedServer(Netty, 8080, module = Application::myApplicationModule).start(wait = true)

}
// Function to get Weather
fun getWeather(): WeatherResponse {
    val client = OkHttpClient()

    val lat = "41.8562" // latitude for Windsor, CT
    val lon = "-72.6437" // longitude for  Windsor, CT
    val units = "imperial"
    val apiKey = "f3c7fb83268e7789449af63628d70dcb" // replace with your OpenWeatherMap API key

    val url =
        "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$apiKey&units=$units&units=$units"

    val request = Request.Builder()
        .url(url)
        .get()
        .build()

    val response = client.newCall(request).execute()
    val responseString = response.body?.string() ?: ""
    return Klaxon().parse<WeatherResponse>(responseString)!!
}
// Function to create the application module
@OptIn(InternalAPI::class)
fun Application.myApplicationModule() {
// Initialize the database
    dao.init()

    configureRoutes()


// Install callLogging feature
    install(CallLogging) {
        level = Level.INFO

    }
// Install FreeMarker to use for templating
    install(FreeMarker) {
        // Set the template loader to the class loader
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

}



















