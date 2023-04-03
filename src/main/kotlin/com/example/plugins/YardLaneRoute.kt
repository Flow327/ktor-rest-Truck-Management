package com.example.plugins

import com.example.dao
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.yardLaneRoute() {
    get("/parkingLot") {
    call.respond(
        FreeMarkerContent(
            "parkingLot.ftl",
            mapOf("drivers" to dao.getAllDrivers())
        )
    )
}
}