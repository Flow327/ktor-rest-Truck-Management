package com.example.plugins

import com.example.dao
import com.example.model.calculateStats
import com.example.model.filterDrivers
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.statusRoute() {
    get("/stats") {
        val drivers = dao.getAllDrivers()
        val contentKeyWords = listOf("full", "empty", "full chep", "full blonde")
        val containerKeyWords = listOf("yes", "no")

        val filteredDrivers = filterDrivers(drivers, contentKeyWords, containerKeyWords)
        val updateStats = calculateStats(filteredDrivers)
        call.respond(FreeMarkerContent("stats.ftl", mapOf("stats" to listOf(updateStats))))
    }
}
