package ie.setu.domain.repository

import ie.setu.domain.Intake
import ie.setu.domain.User
import ie.setu.domain.db.Intakes
import ie.setu.domain.db.Users
import ie.setu.utils.mapToIntake
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class IntakeDAO {

    //Get all the intakes in the database regardless of user id
    fun getAll(): ArrayList<Intake> {
        val intakesList: ArrayList<Intake> = arrayListOf()
        transaction {
            Intakes.selectAll().map {
                intakesList.add(mapToIntake(it)) }
        }
        return intakesList
    }

    //Find a specific intake by intake id
    fun findByIntakeId(id: Int): Intake?{
        return transaction {
            Intakes
                .select() { Intakes.id eq id}
                .map{mapToIntake(it)}
                .firstOrNull()
        }
    }

    //Find all intakes for a specific user id
    fun findByUserId(userId: Int): List<Intake>{
        return transaction {
            Intakes
                .select {Intakes.userId eq userId}
                .map {mapToIntake(it)}
        }
    }

    //Save an intake to the database
    fun save(intake: Intake) : Int?{
        return transaction {
            Intakes.insert {
                it[food] = intake.food
                it[numberOfUnits] = intake.numberOfUnits
                it[calorie] = intake.calorie
                it[userId] = intake.userId
            } get Intakes.id
        }
    }

    fun updateByIntakeId(intakeId: Int, intakeDTO: Intake): Int {
        return transaction {
            Intakes.update ({
                Intakes.id eq intakeId}) {
                it[food] = intakeDTO.food
                it[numberOfUnits] = intakeDTO.numberOfUnits
                it[calorie] = intakeDTO.calorie
                it[userId] = intakeDTO.userId
            }
        }
    }

    fun deleteByIntakeId (intakeId: Int): Int{
        return transaction{
            Intakes.deleteWhere { Intakes.id eq intakeId }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            Intakes.deleteWhere { Intakes.userId eq userId }
        }
    }
}