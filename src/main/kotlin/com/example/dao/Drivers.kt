package com.example.dao

import org.jetbrains.exposed.sql.Table

//Declare an object called Drivers that extends the Table class
object Drivers : Table() {

    //Declare an integer variable called id that auto increments
    val id = integer("id").autoIncrement()

    //Declare a varchar variable called name with a length of 50
    val name = varchar("name", 50)

    //Declare a varchar variable called parking with a length of 50
    val parking = integer("parking")

    //Declare a varchar variable called parking with a length of 50
    val door = integer("door")

    //Declare a varchar variable called truckNumber with a length of 100
    val truckNumber = varchar("truckNumber", 100)

    // Declare a varchar variable called contents with length of 100
    val contents = varchar("contents", 100)

    // Declare a varchar variable called container with length of 100
    val container = varchar("containers", 100)

    // Declare a varchar variable called comments with length of 100
    val comments = varchar("comments", 100)

    // Declare a varchar variable called timeStamp with length of 250
    val timeStamp = varchar("timeStamp", 250)

    // Declare a varchar variable called updateStamp with length of 250
    val updateStamp = varchar("updateStamp", 250).nullable()

    // Declare an Integer variable called usedParking
    val usedParking = bool("usedParking")

    // Declare an Integer variable called usedDoors
    val usedDoors = bool("usedDoors")

    //Declare the primary key as the id variable
    override val primaryKey = PrimaryKey(id)
}