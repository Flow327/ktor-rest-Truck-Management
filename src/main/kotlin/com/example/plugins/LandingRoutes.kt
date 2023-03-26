package com.example.plugins

import com.example.dao
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.landingRoutes() {
    get("/landing") {
        val doorOrLane = call.parameters["doorOrLane"]?.toIntOrNull() ?: 0
        val model = mapOf("title" to "Landing Page", "doorOrLane" to doorOrLane)
        call.respond(FreeMarkerContent("landing.ftl", model))
    }


    post("/submit") {
       val params = call.receiveParameters()
        val companyName = params["companyName"]
        val trailerNumber = params["trailerNumber"]
        val doorOrLane = params["doorOrLane"]?.toIntOrNull()
        if (companyName != null && trailerNumber != null && doorOrLane != null) {
            // Assuming dao is an instance of your DAOFacadeDatabase class
            dao.createDriver(
                name = companyName,
                parking = doorOrLane,
                door = doorOrLane,
                truckNumber = trailerNumber,
                contents = "",
                container = "",
                comments = "",
                usedParking = false, // You can set a default value or calculate it based on your needs
                usedDoors = false // You can set a default value or calculate it based on your needs
            )

        } else {
            // Handle the case when some of the required fields are missing, e.g., show an error message
            call.respondRedirect("/landing?error=missingFields", permanent = false)
            return@post
        }
        call.respondRedirect("/thankyou", permanent = false)
    }

    get("/landing") {
        val error = call.parameters["error"]
        val errorMessage = if (error != null && error == "missingFields") {
            "Please fill in all required fields."
        } else {
            ""
        }
        call.respond(FreeMarkerContent("landing.ftl", mapOf("title" to "Landing Page", "errorMessage" to errorMessage)))
    }

    get("/thankyou") {
        call.respond(FreeMarkerContent("thankyou.ftl", mapOf("title" to "Thank You")))
    }

}



