package com.example.dao

import com.example.model.Driver
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.Closeable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Interface for the DAO Facade
interface DAOFacade : Closeable {
    // Initializes the database
    fun init()

    // Creates a driver in the database
    fun createDriver(
        name: String,
        parking: String,
        truckNumber: String,
        contents: String,
        container: String,
        comments: String,
        timeStamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString()
    )

    // Updates a driver in the database
    fun updateDriver(
        id: Int,
        name: String,
        parking: String,
        truckNumber: String,
        contents: String,
        container: String,
        comments: String,
        timeStamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString()
    )

    // Deletes a driver from the database
    fun deleteDriver(id: Int)

    // Gets a driver from the database
    fun getDriver(id: Int): Driver?

    // Gets all drivers from the database
    fun getAllDrivers(): List<Driver>
}

// Class for the DAO Facade Database
class DAOFacadeDatabase(val db: Database) : DAOFacade {
    // Initializes the database
    override fun init() = transaction(db) {


    }

    // Creates a driver in the database
    override fun createDriver(
        name: String,
        parking: String,
        truckNumber: String,
        contents: String,
        container: String,
        comments: String,
        timeStamp: String
    ) = transaction(db) {

        // Inserts the driver into the Drivers table
        Drivers.insert {
            it[Drivers.name] = name; it[Drivers.parking] = parking; it[Drivers.truckNumber] =
            truckNumber; it[Drivers.contents] = contents; it[Drivers.container] = container
            it[Drivers.comments] = comments; it[Drivers.timeStamp] =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString()
        }
        // Returns Unit
        Unit
    }


    // Updates a driver in the database
    override fun updateDriver(
        id: Int,
        name: String,
        parking: String,
        truckNumber: String,
        contents: String,
        container: String,
        comments: String,
        timeStamp: String
    ) = transaction(db) {
        // Updates the driver in the Drivers table
        Drivers.update({ Drivers.id eq id }) {
            it[Drivers.name] = name
            it[Drivers.parking] = parking
            it[Drivers.truckNumber] = truckNumber
            it[Drivers.contents] = contents
            it[Drivers.container] = container
            it[Drivers.comments] = comments
            it[Drivers.timeStamp] =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString()


        }
        // Returns Unit
        Unit
    }

    // Deletes a driver from the database
    override fun deleteDriver(id: Int) = transaction(db) {
        // Deletes the driver from the Drivers table
        Drivers.deleteWhere {
            Drivers.id eq id
        }
        // Returns Unit
        Unit
    }


    // Gets a driver from the database
    override fun getDriver(id: Int) = transaction(db) {
        // Selects the driver from the Drivers table
        Drivers.select { Drivers.id eq id }.map {
            // Maps the driver to a Driver object
            Driver(
                it[Drivers.id],
                it[Drivers.name],
                it[Drivers.parking],
                it[Drivers.truckNumber],
                it[Drivers.contents],
                it[Drivers.container],
                it[Drivers.comments],
                it[Drivers.timeStamp]
            )
        }.singleOrNull()
        // Returns the driver or null
    }

    // Gets all drivers from the database
    override fun getAllDrivers() = transaction(db) {
        // Selects all drivers from the Drivers table
        Drivers.selectAll().map {
            // Maps the drivers to Driver objects
            Driver(
                it[Drivers.id],
                it[Drivers.name],
                it[Drivers.parking],
                it[Drivers.truckNumber],
                it[Drivers.contents],
                it[Drivers.container],
                it[Drivers.comments],
                it[Drivers.timeStamp]
            )
        }
        // Returns the list of drivers
    }

    // Closes the database
    override fun close() {

    }

}
