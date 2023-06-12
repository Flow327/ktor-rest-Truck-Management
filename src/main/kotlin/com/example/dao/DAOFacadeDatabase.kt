package com.example.dao


import com.example.model.Driver
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.Closeable
import java.time.LocalDate
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

    // Delete a driver from Yardout
    fun deleteYardOutDriver(id:Int)

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

    fun moveRowToYardOut(id: Int)


    // Function to move Drivers to Yard out Table
    fun getAllYardOutDrivers(): List<Driver>

    fun getYardOutDriverById(id: Int):Driver

    fun getYardOutDriversByDate(date:LocalDate): List<Driver>

    fun getDatesWithYardOutDrivers(): List<LocalDate>


}
// Class for the DAO Facade Database
class DAOFacadeDatabase(val db: Database) : DAOFacade {
    // Initializes the database
    override fun init() = transaction(db) {
       //SchemaUtils.drop(Drivers, YardOut)
        //SchemaUtils.create(Drivers, YardOut)
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

        // Check if there is already a driver with the same parking or door number
        val existingDriversWithSameParkingOrDoor = Drivers.select {
            (Drivers.parking eq parking) or (Drivers.door eq door)
        }.orderBy(Drivers.timeStamp to SortOrder.ASC).toList()

        // If there is an existing driver with the same parking or door number, delete the oldest one
        if (existingDriversWithSameParkingOrDoor.isNotEmpty()) {
            val oldestDriver = existingDriversWithSameParkingOrDoor.first()
            val oldestDriverId = oldestDriver[Drivers.id]
            Drivers.deleteWhere { Drivers.id eq oldestDriverId }
        }

        // Inserts the new driver into the Drivers table
        Drivers.insert {
            it[Drivers.name] = name; it[Drivers.parking] = parking; it[Drivers.door] = door; it[Drivers.truckNumber] =
            truckNumber; it[Drivers.contents] = contents; it[Drivers.container] = container
            it[Drivers.comments] = comments; it[Drivers.timeStamp] =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString();
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
    override fun deleteYardOutDriver(id: Int) = transaction(db) {
        YardOut.deleteWhere {
            YardOut.id eq id
        }
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
    override fun moveRowToYardOut(id: Int) {
        transaction(db) {
            val driver = Drivers.select { Drivers.id eq id }.singleOrNull()
            driver?.let { row ->
                YardOut.insert {
                    it[name] = row[Drivers.name]
                    it[parking] = row[Drivers.parking]
                    it[door] = row[Drivers.door]
                    it[truckNumber] = row[Drivers.truckNumber]
                    it[contents] = row[Drivers.contents]
                    it[container] = row[Drivers.container]
                    it[comments] = row[Drivers.comments]
                    it[timeStamp] = row[Drivers.timeStamp]
                    it[updateStamp] = row[Drivers.updateStamp]
                    it[usedParking] = row[Drivers.usedParking]
                    it[usedDoors] = row[Drivers.usedDoors]
                    it[yardOutDate] = LocalDate.now().toString()
                }
                Drivers.deleteWhere { Drivers.id eq id }
            }
        }
    }
    // In your DAOFacadeDatabase implementation
    override fun getAllYardOutDrivers(): List<Driver> = transaction(db) {
        YardOut.selectAll().map {
            Driver(
                id = it[YardOut.id],
                name = it[YardOut.name],
                parking = it[YardOut.parking],
                door = it[YardOut.door],
                truckNumber = it[YardOut.truckNumber],
                contents = it[YardOut.contents],
                container = it[YardOut.container],
                comments = it[YardOut.comments],
                timeStamp = it[YardOut.timeStamp],
                updateStamp = it[YardOut.updateStamp] ?: "",
                emailSent = it[YardOut.emailSent],
                usedParking = it[YardOut.usedParking],
                usedDoors = it[YardOut.usedParking],
            )
        }
    }
    override fun getYardOutDriverById(id: Int): Driver = transaction(db) {
        YardOut.select { YardOut.id eq id }.map {
            Driver(
                id = it[YardOut.id],
                name = it[YardOut.name],
                parking = it[YardOut.parking],
                door = it[YardOut.door],
                truckNumber = it[YardOut.truckNumber],
                contents = it[YardOut.contents],
                container = it[YardOut.container],
                comments = it[YardOut.comments],
                timeStamp = it[YardOut.timeStamp],
                updateStamp = it[YardOut.updateStamp] ?: "",
                emailSent = it[YardOut.emailSent],
                usedParking = it[YardOut.usedParking],
                usedDoors = it[YardOut.usedDoors],

            )

        }.single()
    }
    override fun getYardOutDriversByDate(date:LocalDate): List<Driver> = transaction(db) {
        YardOut.select { YardOut.yardOutDate eq date.toString() }
            .map {
                Driver(
                    id = it[YardOut.id],
                    name = it[YardOut.name],
                    parking = it[YardOut.parking],
                    door = it[YardOut.door],
                    truckNumber = it[YardOut.truckNumber],
                    contents = it[YardOut.contents],
                    container = it[YardOut.container],
                    comments = it[YardOut.comments],
                    timeStamp = it[YardOut.timeStamp],
                    updateStamp = it[YardOut.updateStamp] ?: "",
                    emailSent = it[YardOut.emailSent],
                    usedParking = it[YardOut.usedParking],
                    usedDoors = it[YardOut.usedDoors],
                )
            }
    }
    override fun getDatesWithYardOutDrivers(): List<LocalDate> = transaction(db) {
        YardOut.slice(YardOut.yardOutDate)
            .select { YardOut.yardOutDate.isNotNull() }
            .distinct()
            .map { LocalDate.parse(it[YardOut.yardOutDate]) }
    }

    // Closes the database
    override fun close() {

    }
}



