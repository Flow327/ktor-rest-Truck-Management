package com.example.dao

import org.jetbrains.exposed.sql.Table

//Declare an object called Drivers that extends the Table class
object Drivers : Table() {

    //Declare an integer variable called id that auto increments
    val id = integer("id").autoIncrement()

    //Declare a varchar variable called name with a length of 50
    val name = varchar("name", 50)

    //Declare a varchar variable called parking with a length of 50
    val parking = varchar("parking", 50)

    //Declare a varchar variable called truckNumber with a length of 100
    val truckNumber = varchar("truckNumber", 100)

    // Declare a varchar variable called contents with length of 100
    val contents = varchar("contents", 100)

    // Declare a varchar variable called container with length of 100
    val container = varchar("containers", 100)

    // Declare a varchar variable called comments with length of 100
    val comments = varchar("comments", 100)

    val timeStamp = varchar("timeStamp", 250)


    //Declare the primary key as the id variable
    override val primaryKey = PrimaryKey(id)
}