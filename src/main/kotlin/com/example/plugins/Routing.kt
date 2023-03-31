package com.example.plugins

import com.example.dao
import com.example.getWeather
import com.example.model.Driver
import com.example.model.calculateStats
import com.example.model.filterDrivers
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

fun Application.configureRoutes() {

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

        // get DAO to get the list of unused parking numbers
        val unusedParkingNumber = dao.getUnusedParkingNumbers()

        // generate the list of all parking numbers and filter them based on whether they are in the list of unused parking numbers
        return (minParkingNumber..maxParkingNumber).filter { it !in unusedParkingNumber && it != fireLane }
    }

    fun sendNotificationEmail(driver: Driver, subject: String, status: String) {
        val to =
            arrayOf("julio.acostasilverio@walgreens.com,justin.roberts@walgreens.com,luis.baez@walgreens.com,jarred.bennettmoorer@walgreens.com,John.Botero@walgreens.com,frank.capaccio@walgreens.com,ian.earle@walgreens.com,matt.flaherty@walgreens.com,david.matthew.johnson@walgreens.com,anderson.oyola@walgreens.com,tuan.pham@walgreens.com,timothy.reardon@walgreens.com,duane.smith@walgreens.com,mark.arnold@walgreens.com,pablo.mendez@walgreens.com,robert.staniewicz@walgreens.com,desmond.ledford@walgreens.com") // Add more email addresses as needed
        val from = "julio.acostasilverio@walgreens.com"
        val host = "corpsmtp.walgreens.com"

        val properties = System.getProperties()
        properties.setProperty("mail.smtp.host", host)

        val session = Session.getDefaultInstance(properties)

        val message = MimeMessage(session)
        message.setFrom(InternetAddress(from))
        to.forEach { recipient ->
            message.addRecipient(Message.RecipientType.TO, InternetAddress(recipient))
        }
        message.subject = subject
        message.setText("$status Trailer number: ${driver.truckNumber} Has been in the Lot. Initial date of creation: ${driver.timeStamp}.")

        Transport.send(message)
    }

// Create the routing
    routing {
        landingRoutes()
        YardOutRoute()
        returnDriverRoute()
        yardOutDisplayRoute()
        // Route for the index page
        route("/") {
            // Get request for the index page
            get {
                // Respond with the FreeMarker template and the list of drivers
                call.respond(FreeMarkerContent("index.ftl", mapOf("drivers" to dao.getAllDrivers())))

                // Iterate through all drivers and check if their timestamp is older than 5 or 8 days
                dao.getAllDrivers().forEach { driver ->
                    if (!driver.emailSent) { // Check if the email has already been sent
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        val timestamp = LocalDateTime.parse(driver.timeStamp, formatter)
                        val fiveDaysAgo = LocalDateTime.now().minusDays(5)
                        val eightDaysAgo = LocalDateTime.now().minusDays(8)

                        if (timestamp.isBefore(eightDaysAgo)) {
                            // Send critical email
                            sendNotificationEmail(
                                driver,
                                "Critical: Yard-test",
                                "CRITICAL! Trailer has been in lot for 8 days "
                            )
                            dao.updateEmailSent(driver.id) // Update the emailSent flag in the database
                        } else if (timestamp.isBefore(fiveDaysAgo)) {
                            // Send cautionary email
                            sendNotificationEmail(driver, "Yard-test", "Trailer has reached 5 days in the lot.")
                            dao.updateEmailSent(driver.id) // Update the emailSent flag in the database
                        }
                    }
                }
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
            // Post request for the driver page
            post {
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

        // Route for the delete page
        route("/delete") {
            println("delete block")
            // Get request for the delete page
            get {
                // Get the id and table from the query parameters
                val id = call.request.queryParameters["id"]
                val table = call.request.queryParameters["table"]
                // If the id is not null, delete the driver
                if (id != null) {
                    when (table) {
                        "drivers" -> {
                            val driver = dao.getDriver(id.toInt())
                            dao.deleteDriver(id.toInt())
                            if (driver != null) {
                                dao.setParkingAsUnused(driver.parking)
                                dao.setDoorAsUnused(driver.door)
                            }
                            call.respond(
                                FreeMarkerContent(
                                    "index.ftl",
                                    mapOf("drivers" to dao.getAllDrivers())
                                )
                            )
                        }

                        "yardout" -> {
                            dao.deleteDriver(id.toInt())
                            call.respondRedirect("/yardout?deleted=true")
                        }
                    }
                }
            }
        }
        route("/weather") {
            println("weather block")
            get {
                val weatherResponse = getWeather()
                println(weatherResponse)

                // Pass the data to FreeMarker template
                call.respond(FreeMarkerContent("weather.ftl", mapOf("weatherResponse" to weatherResponse)))
            }
        }

        route("/search") {
            get {
                val query = call.parameters["query"] ?: ""
                println(query)
                val drivers = dao.searchDrivers(query)
                call.respond(FreeMarkerContent("search.ftl", mapOf("drivers" to drivers)))
            }
            post {
                val params = call.receiveParameters()
                val query = params["query"] ?: ""
                println(query)
                val drivers = dao.searchDrivers(query)
                call.respond(FreeMarkerContent("search.ftl", mapOf("drivers" to drivers)))
            }
        }
        route("/stats") {
            get {
                val drivers = dao.getAllDrivers()
                val contentKeyWords = listOf("full", "empty", "full chep", "full blonde")
                val containerKeyWords = listOf("yes", "no")

                val filteredDrivers = filterDrivers(drivers, contentKeyWords, containerKeyWords)
                val updateStats = calculateStats(filteredDrivers)
                call.respond(FreeMarkerContent("stats.ftl", mapOf("stats" to listOf(updateStats))))
            }
        }
    }
}









