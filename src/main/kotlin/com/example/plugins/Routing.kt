package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRoutes() {
// Create the routing
    routing {
        newDriverRoute()
        homePageRoute()
        landingRoutes()
        yardOutRoute()
        yardLaneRoute()
        doorNumberRoute()
        returnDriverRoute()
        yardOutDisplayRoute()
        statusRoute()
        searchRoute()
        weatherRoute()
        deleteDriverRoute()
        deleteYardOutDriverRoute()
        yardOutByDateRoute()
        datesWithRecordsRoute()
    }
}









