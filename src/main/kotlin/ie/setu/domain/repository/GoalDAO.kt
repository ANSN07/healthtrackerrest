package ie.setu.domain.repository

import ie.setu.domain.Goal
import ie.setu.domain.db.Goals
import ie.setu.utils.mapToGoal
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class GoalDAO {

    fun findByUserId(userId: Int): List<Goal>{
        return transaction {
            Goals
                .select {Goals.userId eq userId}
                .map { mapToGoal(it) }
        }
    }

    fun save(goal: Goal) : Int?{
        return transaction {
            Goals.insert {
                it[targetWeight] = goal.targetWeight
                it[targetCalories] = goal.targetCalories
                it[targetLevel] = goal.targetLevel
                it[date] = goal.date
                it[userId] = goal.userId
            } get Goals.id
        }
    }

    fun updateById(goalId: Int, goalDTO: Goal): Int {
        return transaction {
            Goals.update ({
                Goals.id eq goalId}) {
                it[targetWeight] = goalDTO.targetWeight
                it[targetCalories] = goalDTO.targetCalories
                it[targetLevel] = goalDTO.targetLevel
                it[date] = goalDTO.date
                it[userId] = goalDTO.userId
            }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            Goals.deleteWhere { Goals.userId eq userId }
        }
    }
}