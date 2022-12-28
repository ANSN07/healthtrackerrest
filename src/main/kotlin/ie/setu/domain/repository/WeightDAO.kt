package ie.setu.domain.repository

import ie.setu.domain.Weight
import ie.setu.domain.db.userWeight
import ie.setu.utils.mapToWeight
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class WeightDAO {

    fun findByUserId(userId: Int): List<Weight>{
        return transaction {
            userWeight
                .select {userWeight.userId eq userId}
                .map {
                    mapToWeight(it)
                }
        }
    }

    fun save(weight: Weight) : Int?{
        return transaction {
            userWeight.insert {
                it[value] = weight.value
                it[date] = weight.date
                it[userId] = weight.userId
            } get userWeight.id
        }
    }

    fun updateById(weightId: Int, weightDTO: Weight): Int {
        return transaction {
            userWeight.update ({
                userWeight.id eq weightId}) {
                it[value] = weightDTO.value
                it[date] = weightDTO.date
                it[userId] = weightDTO.userId
            }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            userWeight.deleteWhere { userWeight.userId eq userId }
        }
    }

}