package com.example.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Driver(val id: Int, val name: String, val parking: String, val truckNumber: String, val contents: String, val container: String, val comments: String, val timeStamp: String = LocalDateTime.now().format(
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString())