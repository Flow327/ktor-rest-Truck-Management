package com.example.dao

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

//Declare an object called Drivers that extends the Table class
object YardOut : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 50)
    val parking: Column<Int?> = integer("parking").nullable()
    val door: Column<Int?> = integer("door").nullable()
    val truckNumber: Column<String> = varchar("truckNumber", 100)
    val contents: Column<String> = varchar("contents", 100)
    val container: Column<String> = varchar("containers", 100)
    val comments: Column<String> = varchar("comments", 100)
    val timeStamp: Column<String> = varchar("timeStamp", 250)
    val updateStamp: Column<String?> = varchar("updateStamp", 250).nullable()
    val usedParking =bool("usedParking").default(false)
    val usedDoors =bool("usedDoors").default(false)
    val emailSent = bool("emailSent").default(false)
    val yardOutDate = varchar("date", 250)

    override val primaryKey = PrimaryKey(Drivers.id)
}