package ie.setu.domain.repository

import ie.setu.domain.Badge
import ie.setu.domain.FoodItem
import ie.setu.domain.db.Badges
import ie.setu.domain.db.FoodItems
import ie.setu.utils.mapToBadge
import ie.setu.utils.mapToFoodItem
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class FoodItemDAO {

    fun findByUserId(userId: Int): List<FoodItem>{
        return transaction {
            FoodItems
                .select { FoodItems.userId eq userId}
                .map { mapToFoodItem(it) }
        }
    }

    fun save(foodItem: FoodItem) : Int?{
        return transaction {
            FoodItems.insert {
                it[foodName] = foodItem.foodName
                it[calorie] = foodItem.calorie
                it[unitMeasure] = foodItem.unitMeasure
                it[numberOfItems] = foodItem.numberOfItems
                it[userId] = foodItem.userId
            } get FoodItems.foodId
        }
    }

    fun updateByFoodId(foodId: Int, foodDTO: FoodItem): Int {
        return transaction {
            FoodItems.update ({
                FoodItems.foodId eq foodId}) {
                it[foodName] = foodDTO.foodName
                it[calorie] = foodDTO.calorie
                it[unitMeasure] = foodDTO.unitMeasure
                it[numberOfItems] = foodDTO.numberOfItems
                it[userId] = foodDTO.userId
            }
        }
    }

    fun deleteByFoodId (foodId: Int): Int{
        return transaction{
            FoodItems.deleteWhere { FoodItems.foodId eq foodId }
        }
    }
}