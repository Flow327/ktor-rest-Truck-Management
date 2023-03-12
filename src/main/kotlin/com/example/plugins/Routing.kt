package com.example.plugins

import com.example.dao
import com.example.getWeather
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRoutes() {
// Create the routing
    routing {
        // Route for the index page
        route("/") {
            // Get request for the index page
            get {
                // Respond with the FreeMarker template and the list of drivers
                call.respond(FreeMarkerContent("index.ftl", mapOf("drivers" to dao.getAllDrivers())))
            }
        }
        // Route for the driver page
        route("/driver") {
            // Get request for the driver page
            get {
                // Get the action from the query parameters
                when (val action = (call.request.queryParameters["action"] ?: "new")) {
                    // If the action is new, respond with the FreeMarker template
                    "new" -> call.respond(
                        FreeMarkerContent(
                            "driver.ftl",
                            mapOf("action" to action)
                        )
                    )

                    // If the action is edit, get the id from the query parameters
                    "edit" -> {
                        val id = call.request.queryParameters["id"]
                        // If the id is not null, respond with the FreeMarker template and the driver
                        if (id != null) {
                            call.respond(
                                FreeMarkerContent(
                                    "driver.ftl",
                                    mapOf(
                                        "driver" to dao.getDriver(id.toInt()),
                                        "action" to action
                                    )
                                )
                            )
                        }
                    }
                }
            }
            // Post request for the driver page
            post {
                // Get the post parameters
                val postParameters: Parameters = call.receiveParameters()
                // Get the action from the post parameters
                when (postParameters["action"] ?: "new") {
                    // If the action is new, create a new driver
                    "new" -> dao.createDriver(
                        postParameters["name"] ?: "",
                        postParameters["parking"] ?: "",
                        postParameters["truckNumber"] ?: "",
                        postParameters["contents"] ?: "",
                        postParameters["container"] ?: "",
                        postParameters["comments"] ?: "",
                        postParameters["timeStamp"] ?: ""

                    )

                    "edit" -> {
                        val id = postParameters["id"]
                        if (id != null)
                            dao.updateDriver(
                                id.toInt(),
                                postParameters["name"] ?: "",
                                postParameters["parking"] ?: "",
                                postParameters["truckNumber"] ?: "",
                                postParameters["contents"] ?: "",
                                postParameters["container"] ?: "",
                                postParameters["comments"] ?: "",
                                postParameters["timeStamp"] ?: ""
                            )
                    }
                    // If the action is edit, get the id from the post parameters

                }
                // Respond with the FreeMarker template and the list of drivers
                call.respond(FreeMarkerContent("index.ftl", mapOf("drivers" to dao.getAllDrivers())))
            }

        }

        // Route for the delete page
        route("/delete") {
            // Get request for the delete page
            get {
                // Get the id from the query parameters
                val id = call.request.queryParameters["id"]
                // If the id is not null, delete the driver
                if (id != null) {
                    dao.deleteDriver(id.toInt())
                    // Respond with the FreeMarker template and the list of drivers
                    call.respond(
                        FreeMarkerContent(
                            "index.ftl",
                            mapOf("drivers" to dao.getAllDrivers())
                        )
                    )

                }
            }
        }
        route("/weather") {
            get {
                val weatherResponse = getWeather()
                println(weatherResponse)

                // Pass the data to FreeMarker template
                call.respond(FreeMarkerContent("weather.ftl", mapOf("weatherResponse" to weatherResponse)))
            }
        }
    }

}
