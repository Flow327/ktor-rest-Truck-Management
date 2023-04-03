package com.example.plugins

import com.example.dao
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteDriverRoute() {
    get("/deleteDriver") {
        val queryParameters = call.request.queryParameters
        when (queryParameters["action"]) {
            "delete" -> {
                val id = queryParameters["id"]?.toIntOrNull()
                if (id != null) {
                    val driver = dao.getDriver(id)
                    dao.deleteDriver(id)
                    if (driver != null) {
                        dao.setParkingAsUnused(driver.parking)
                        dao.setDoorAsUnused(driver.door)
                    }
                }
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
}
