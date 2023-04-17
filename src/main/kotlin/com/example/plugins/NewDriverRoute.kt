package com.example.plugins

import com.example.dao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.newDriverRoute() {
    // Function to generate Door numbers
    fun generateDoorNumbers(): List<Int> {
        val minDoorNumber = 50
        val maxDoorNumber = 98
        val cardBoardDoorOne = 74
        val cardBoardDoorTwo = 75
        val unusedParkingNumbers = dao.getUnusedParkingNumbers()

        // generate the list of all door numbers and filter them based on whether they are in the list of unused parking numbers
        return (minDoorNumber..maxDoorNumber).filter { it !in unusedParkingNumbers && it != cardBoardDoorOne && it != cardBoardDoorTwo }
    }

    // Function to Generate Parking numbers
    fun generateParkingNumbers(): List<Int> {
        val minParkingNumber = 216
        val maxParkingNumber = 314
        val fireLane = 237
        val rallyPoints = listOf(274..279)

        // get DAO to get the list of unused parking numbers
        val unusedParkingNumber = dao.getUnusedParkingNumbers()

        // generate the list of all parking numbers and filter them based on whether they are in the list of unused parking numbers
        return (minParkingNumber..maxParkingNumber).filter { it !in unusedParkingNumber && it != fireLane && it !in rallyPoints.first() }

    }

    get("/driver") {
        // Get the action from the query parameters
        when (val action = (call.request.queryParameters["action"] ?: "new")) {
            // If the action is new, respond with the FreeMarker template
            "new" -> call.respond(
                FreeMarkerContent(
                    "driver.ftl",
                    mapOf(
                        "action" to action,
                        "parkingNumbers" to generateParkingNumbers(),
                        "doorNumbers" to generateDoorNumbers()
                    )
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
                                "action" to action,
                                "parkingNumbers" to generateParkingNumbers(),
                                "doorNumbers" to generateDoorNumbers()
                            )
                        )
                    )
                }
            }
        }
    }
    get("/api/drivers") {
        val drivers = dao.getAllDrivers()
        call.respond(HttpStatusCode.OK, drivers)
    }
// Post request for the driver page
    post("/driver") {
        // Get the post parameters
        val postParameters: Parameters = call.receiveParameters()
        // Get the action from the post parameters
        when (postParameters["action"] ?: "new") {
            // If the action is new, create a new driver
            "new" -> dao.createDriver(
                postParameters["name"] ?: "",
                postParameters["parking"]?.toInt() ?: 0,
                postParameters["door"]?.toInt() ?: 0,
                postParameters["truckNumber"] ?: "",
                postParameters["contents"] ?: "",
                postParameters["container"] ?: "",
                postParameters["comments"] ?: "",
                postParameters["timeStamp"] ?: "",
                usedParking = true,
                usedDoors = true
            )

            "edit" -> {
                val id = postParameters["id"]
                if (id != null) {
                    val existingDriver = dao.getDriver(id.toInt())
                    val newParking = postParameters["parking"]?.toInt() ?: 0
                    val newDoor = postParameters["door"]?.toInt() ?: 0

                    val usedParking = existingDriver?.parking != newParking
                    val usedDoors = existingDriver?.door != newDoor

                    dao.updateDriver(
                        id.toInt(),
                        postParameters["name"] ?: "",
                        newParking,
                        newDoor,
                        postParameters["truckNumber"] ?: "",
                        postParameters["contents"] ?: "",
                        postParameters["container"] ?: "",
                        postParameters["comments"] ?: "",
                        postParameters["updateStamp"] ?: "",
                        usedParking = usedParking,
                        usedDoors = usedDoors
                    )
                }
            }
            // If the action is edit, get the id from the post parameters

        }
        // Respond with the FreeMarker template and the list of drivers
        call.respond(FreeMarkerContent("index.ftl", mapOf("drivers" to dao.getAllDrivers())))
    }
}