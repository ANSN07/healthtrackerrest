package ie.setu.domain.repository

import ie.setu.domain.Intake
import ie.setu.domain.User
import ie.setu.domain.db.Intakes
import ie.setu.domain.db.Users
import ie.setu.utils.mapToIntake
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class IntakeDAO {

    fun getAll(): ArrayList<Intake> {
        val intakesList: ArrayList<Intake> = arrayListOf()
        transaction {
            Intakes.selectAll().map {
                intakesList.add(mapToIntake(it)) }
        }
        return intakesList
    }

    fun findByIntakeId(id: Int): Intake?{
        return transaction {
            Intakes
                .select() { Intakes.intakeId eq id}
                .map{mapToIntake(it)}
                .firstOrNull()
        }
    }

    fun findByUserId(userId: Int): List<Intake>{
        return transaction {
            Intakes
                .select {Intakes.userId eq userId}
                .map {mapToIntake(it)}
        }
    }

    fun save(intake: Intake) : Int?{
        return transaction {
            Intakes.insert {
                it[mealType] = intake.mealType
                it[date] = intake.date
                it[userId] = intake.userId
            } get Intakes.intakeId
        }
    }

    fun updateByIntakeId(intakeId: Int, intakeDTO: Intake): Int {
        return transaction {
            Intakes.update ({
                Intakes.intakeId eq intakeId}) {
                it[mealType] = intakeDTO.mealType
                it[date] = intakeDTO.date
                it[userId] = intakeDTO.userId
            }
        }
    }

    fun deleteByIntakeId (intakeId: Int): Int{
        return transaction{
            Intakes.deleteWhere { Intakes.intakeId eq intakeId }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            Intakes.deleteWhere { Intakes.userId eq userId }
        }
    }
}