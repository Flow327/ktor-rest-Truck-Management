package com.example.plugins

import com.example.dao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun Route.yardOutRoute() {
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
            call.respondRedirect("/")
        }
    }
}
fun Route.deleteYardOutDriverRoute() {
    get("/deleteyard") {
        val queryParameters = call.request.queryParameters
        when (queryParameters["action"]) {
            "delete" -> {
                val id = queryParameters["id"]?.toIntOrNull()
                if (id != null) {
                    // Delete the yard out driver with the given ID
                    dao.deleteYardOutDriver(id)
                }
            }
        }
        // Respond with the FreeMarker template and the list of drivers
        call.respond(
            FreeMarkerContent(
                "yardout.ftl",
                mapOf("yardOutDrivers" to dao.getAllYardOutDrivers()
                )
            ))
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
                        usedDoors = yardOutDriver.usedDoors,
                        usedParking = yardOutDriver.usedParking
                    )
                    dao.deleteYardOutDriver(yardOutDriverId)
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
fun getDayOfMonthSuffix(day: Int): String {
    return when (day % 10) {
        1 -> if (day != 11) "st" else "th"
        2 -> if (day != 12) "nd" else "th"
        3 -> if (day != 13) "rd" else "th"
        else -> "th"
    }
}
fun Route.yardOutByDateRoute() {
    get("/day") {
        val dateString = call.request.queryParameters["date"]
        if (dateString != null) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = LocalDate.parse(dateString, formatter)
            val driversByDate = dao.getYardOutDriversByDate(date)

            // Convert the date to "April 7th 2023" format for display
            val monthFormatter = DateTimeFormatter.ofPattern("MMMM")
            val yearFormatter = DateTimeFormatter.ofPattern("yyyy")
            val daySuffix = getDayOfMonthSuffix(date.dayOfMonth)
            val formattedDate = "${monthFormatter.format(date)} ${date.dayOfMonth}$daySuffix ${yearFormatter.format(date)}"

            call.respond(FreeMarkerContent("day.ftl", mapOf("yardOutDrivers" to driversByDate, "formattedDate" to formattedDate))) // pass the driversByDate to the template
        } else {
            call.respond(HttpStatusCode.BadRequest, "Missing date parameter.")
        }
    }
}

fun Route.datesWithRecordsRoute() {
    get("/dates-with-records") {
        val datesWithRecords = dao.getDatesWithYardOutDrivers()
        call.respondText(contentType = ContentType.Application.Json) {
            Json.encodeToString(ListSerializer(String.serializer()), datesWithRecords.map { it.toString() })
        }
    }
}



















