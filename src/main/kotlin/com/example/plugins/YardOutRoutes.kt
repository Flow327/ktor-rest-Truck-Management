package com.example.plugins

import com.example.dao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.YardOutRoute() {
    route("/yardout") {
        post {
            val postParameters: Parameters = call.receiveParameters()
            when (postParameters["action"] ?: "yardout") {
                "yardout" -> {
                    val id = postParameters["id"]?.toIntOrNull()

                    if (id != null) {
                        // Move the row with the given ID from the main table to the yard_out_table
                        // and delete the row from the main table.
                        dao.moveRowToYardOut(id)
                    }
                }
            }
            // Redirect to the appropriate page after processing the form submission
            call.respondRedirect("/")
        }
    }
}
        fun Route.returnDriverRoute() {
            get("/return") {
                val yardOutDriverId = call.parameters["id"]?.toIntOrNull()
                if (yardOutDriverId != null) {
                    // Fetch the yard out driver with the given ID
                    val yardOutDriver = dao.getYardOutDriverById(yardOutDriverId)

                    // Create a new driver in the original Drivers table with the fetched yard out driver's data
                    dao.createDriver(
                        name = yardOutDriver.name,
                        parking = yardOutDriver.parking,
                        door = yardOutDriver.door,
                        truckNumber = yardOutDriver.truckNumber,
                        contents = yardOutDriver.contents,
                        container = yardOutDriver.container,
                        comments = yardOutDriver.comments,
                        timeStamp = yardOutDriver.timeStamp,
                        updateStamp = yardOutDriver.updateStamp ?: "",
                        usedDoors = false,
                        usedParking = false
                    )
                }
                call.respondRedirect("/yardout")
            }
        }

fun Route.yardOutDisplayRoute() {
    get("/yardout") {
        val yardOutDrivers = dao.getAllYardOutDrivers()
        call.respond(FreeMarkerContent("yardout.ftl", mapOf("yardOutDrivers" to yardOutDrivers)))
    }
}



