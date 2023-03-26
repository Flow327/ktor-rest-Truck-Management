package com.example.model

import kotlin.math.roundToInt

data class Stats (
    val fullTrailers: Int,
    val emptyTrailers: Int,
    val fullContainers: Int,
    val emptyContainers: Int,
    val fullChepTrailers: Int,
    val fullBlondeTrailers: Int,
    val totalTrailers: Int,
    val percentageOccupied: Int
)
fun calculateStats(drivers: List<Driver>): Stats {
    val fullTrailers = drivers.count { driver -> driver.contents.toLowerCase() == "full" }
    val emptyTrailers = drivers.count { driver -> driver.contents.toLowerCase() == "empty" }
    val fullContainers = drivers.count { driver -> driver.container.toLowerCase() == "no" }
    val emptyContainers = drivers.count { driver -> driver.container.toLowerCase() == "yes" }
    val fullChepTrailers = drivers.count { driver -> driver.contents.toLowerCase() == "full chep" }
    val fullBlondeTrailers = drivers.count { driver -> driver.contents.toLowerCase() == "full blonde" }
    val totalTrailers = fullTrailers + emptyTrailers
    val percentageOccupied = (totalTrailers.toDouble() / 99.0 * 100).roundToInt()


    return Stats(
        fullTrailers = fullTrailers,
        emptyTrailers = emptyTrailers,
        fullContainers = fullContainers,
        emptyContainers = emptyContainers,
        fullChepTrailers = fullChepTrailers,
        fullBlondeTrailers = fullBlondeTrailers,
        totalTrailers = totalTrailers,
        percentageOccupied = percentageOccupied
    )
}

fun filterDrivers(drivers: List<Driver>, contentsKeywords: List<String>, containerKeywords: List<String>): List<Driver> {
    return drivers.filter { driver ->
        val contents = driver.contents.toLowerCase()
        val container = driver.container.toLowerCase()


        val contentsMatch = contentsKeywords.any { keyword -> contents.contains(keyword) }
        val containerMatch = containerKeywords.any { keyword -> container.contains(keyword) }


        contentsMatch && containerMatch
    }
}


