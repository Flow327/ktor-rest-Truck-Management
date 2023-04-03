package com.example.plugins

import com.example.dao
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.searchRoute(){
    get ("/search"){
        val query = call.parameters["query"] ?: ""
        println(query)
        val drivers = dao.searchDrivers(query)
        call.respond(FreeMarkerContent("search.ftl", mapOf("drivers" to drivers)))
    }
    post("/search") {
        val params = call.receiveParameters()
        val query = params["query"] ?: ""
        println(query)
        val drivers = dao.searchDrivers(query)
        call.respond(FreeMarkerContent("search.ftl", mapOf("drivers" to drivers)))
    }
}