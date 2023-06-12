package com.example.plugins

import com.example.dao
import com.example.model.Driver
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

fun Route.homePageRoute(){
    fun sendNotificationEmail(driver: Driver, subject: String, status: String) {
        val to =
            arrayOf("julio.acostasilverio@walgreens.com,justin.roberts@walgreens.com,luis.baez@walgreens.com,jarred.bennettmoorer@walgreens.com,John.Botero@walgreens.com,frank.capaccio@walgreens.com,ian.earle@walgreens.com,matt.flaherty@walgreens.com,david.matthew.johnson@walgreens.com,anderson.oyola@walgreens.com,tuan.pham@walgreens.com,timothy.reardon@walgreens.com,duane.smith@walgreens.com,mark.arnold@walgreens.com,pablo.mendez@walgreens.com,robert.staniewicz@walgreens.com,desmond.ledford@walgreens.com")
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
    get("/") {
        call.response.header("Cache-Control", "no-store, no-cache, must-revalidate, proxy-revalidate")
        call.response.header("Pragma", "no-cache")
        call.response.header("Expires", "0")
        call.response.header("Surrogate-Control", "no-store")

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
