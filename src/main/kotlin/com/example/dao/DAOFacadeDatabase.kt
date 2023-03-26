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
        parking: Int,
        door: Int,
        truckNumber: String,
        contents: String,
        container: String,
        comments: String,
        timeStamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString(),
        updateStamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString(),
        usedParking: Boolean,
        usedDoors: Boolean
    )

    // Updates a driver in the database
    fun updateDriver(
        id: Int,
        name: String,
        parking: Int,
        door: Int,
        truckNumber: String,
        contents: String,
        container: String,
        comments: String,
        updateStamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString() ?:"",
        usedParking: Boolean,
        usedDoors: Boolean
    )
    // Add a function to convert a ResultRow to a Driver object
    fun toDriver(row: ResultRow): Driver =
        Driver(
            id = row[Drivers.id],
            name = row[Drivers.name],
            parking = row[Drivers.parking],
            door = row[Drivers.door],
            truckNumber = row[Drivers.truckNumber],
            contents = row[Drivers.contents],
            container = row[Drivers.container],
            comments = row[Drivers.comments],
            timeStamp = row[Drivers.timeStamp],
            updateStamp = row[Drivers.updateStamp] ?: "",
            usedParking = row[Drivers.usedParking],
            usedDoors = row[Drivers.usedDoors],
            emailSent = row[Drivers.emailSent]
        )


    // Deletes a driver from the database
    fun deleteDriver(id: Int)

    // Gets a driver from the database
    fun getDriver(id: Int): Driver?

    // Gets all drivers from the database
    fun getAllDrivers(): List<Driver>

    // get unused parking numbers
    fun getUnusedParkingNumbers(): List<Int>

    // Set parking as unused function
    fun setParkingAsUnused(parking: Int)

    // get used door numbers function
    fun getUnusedDoorNumbers(): List<Int>

    // set door as unused function
    fun setDoorAsUnused(door: Int)

    // check if parking is used
    fun isParkingUsed(parking: Int): Boolean

    // check if door is used
    fun isDoorUsed(door: Int): Boolean
    // Function checks String in Search Bar
    fun searchDrivers(query: String): List<Driver>

    // Function is for updated Email
    fun updateEmailSent(id: Int)
}
// Class for the DAO Facade Database
class DAOFacadeDatabase(val db: Database) : DAOFacade {
    // Initializes the database
    override fun init() = transaction(db) {
        //SchemaUtils.drop(Drivers)
        //SchemaUtils.create(Drivers)
    }

    // Creates a driver in the database
    override fun createDriver(
        name: String,
        parking: Int,
        door: Int,
        truckNumber: String,
        contents: String,
        container: String,
        comments: String,
        timeStamp: String,
        updateStamp: String,
        usedParking: Boolean,
        usedDoors: Boolean
    ) = transaction(db) {

        // Inserts the driver into the Drivers table
        Drivers.insert {
            it[Drivers.name] = name; it[Drivers.parking] = parking; it[Drivers.door] = door; it[Drivers.truckNumber] =
            truckNumber; it[Drivers.contents] = contents; it[Drivers.container] = container
            it[Drivers.comments] = comments; it[Drivers.timeStamp] =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString();
            //it[Drivers.updateStamp] = updateStamp;
            it[Drivers.usedParking] = usedParking;
            it[Drivers.usedDoors] = usedDoors
        }
        // Returns Unit
        Unit
    }

    // Updates a driver in the database
    override fun updateDriver(
        id: Int,
        name: String,
        parking: Int,
        door: Int,
        truckNumber: String,
        contents: String,
        container: String,
        comments: String,
        updateStamp: String,
        usedParking: Boolean,
        usedDoors: Boolean
    ) = transaction(db) {
        // Updates the driver in the Drivers table
        Drivers.update({ Drivers.id eq id }) {
            it[Drivers.name] = name
            it[Drivers.parking] = parking
            it[Drivers.door] = door
            it[Drivers.truckNumber] = truckNumber
            it[Drivers.contents] = contents
            it[Drivers.container] = container
            it[Drivers.comments] = comments
            it[Drivers.updateStamp] =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString()
            it[Drivers.usedParking] = usedParking
            it[Drivers.usedDoors] = usedDoors


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
                it[Drivers.door],
                it[Drivers.truckNumber],
                it[Drivers.contents],
                it[Drivers.container],
                it[Drivers.comments],
                it[Drivers.timeStamp],
                it[Drivers.updateStamp] ?: "",
                it[Drivers.usedParking],
                it[Drivers.usedDoors],
                it[Drivers.emailSent]
            )
        }.singleOrNull()
        // Returns the driver or null
    }

    // Gets all drivers from the database
    override fun getAllDrivers() = transaction(db) {
        // Selects all drivers from the Drivers table
        val drivers = Drivers.selectAll().map {
            // Maps the drivers to Driver objects
            Driver(
                it[Drivers.id],
                it[Drivers.name],
                it[Drivers.parking],
                it[Drivers.door],
                it[Drivers.truckNumber],
                it[Drivers.contents],
                it[Drivers.container],
                it[Drivers.comments],
                it[Drivers.timeStamp],
                it[Drivers.updateStamp] ?: "",
                it[Drivers.usedParking],
                it[Drivers.usedDoors],
                it[Drivers.emailSent]
            )
        }
        // Returns the list of drivers
        val sortedDrivers = drivers.sortedBy { it.parking }

        return@transaction sortedDrivers
    }

    override fun getUnusedParkingNumbers(): List<Int> {
        return transaction {
            Drivers.selectAll()
                .map { it[Drivers.parking] }
        }
    }

    override fun setParkingAsUnused(parking: Int) {
        transaction {
            Drivers.update({ Drivers.parking eq parking }) {
                it[usedParking] = false
            }
        }

    }


    override fun getUnusedDoorNumbers(): List<Int> {
        return transaction {
            Drivers.selectAll()
                .map { it[Drivers.door] }
        }
    }

    override fun setDoorAsUnused(door: Int) {
        transaction {
            Drivers.update({ Drivers.door eq door }) {
                it[usedDoors] = false
            }
        }
    }

    override fun isParkingUsed(parking: Int): Boolean {
        return transaction {
            Drivers.select { (Drivers.parking eq parking) and (Drivers.usedParking eq true) }.count() > 0
        }
    }

    override fun isDoorUsed(door: Int): Boolean {
        return transaction {
            Drivers.select { (Drivers.door eq door) and (Drivers.usedDoors eq true) }.count() > 0
        }

    }

    // Function to search Drivers
    override fun searchDrivers(query: String): List<Driver> {
        val drivers = transaction(db) {
            val queryInt = query.toIntOrNull() ?: -1
            Drivers.select {
                (Drivers.id eq queryInt) or
                        (Drivers.door eq queryInt) or
                        (Drivers.parking eq queryInt) or
                        (Drivers.truckNumber like query)
            }.map { toDriver(it) }
        }
        println(drivers)
        return drivers
    }

    override fun updateEmailSent(id: Int) {
        transaction(db) {
            Drivers.update({ Drivers.id eq id }) {
                it[emailSent] = true
            }
        }
    }

    // Closes the database
    override fun close() {
    }

}
