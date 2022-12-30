package ie.setu.domain.repository

import ie.setu.domain.FoodItem
import ie.setu.domain.db.FoodItems
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class FoodItemDAO {

    fun save(foodItem: FoodItem) : Int?{
        return transaction {
            FoodItems.insert {
                it[foodName] = foodItem.foodName
                it[calorie] = foodItem.calorie
                it[unitMeasure] = foodItem.unitMeasure
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
            }
        }
    }

    fun deleteByFoodId (foodId: Int): Int{
        return transaction{
            FoodItems.deleteWhere { FoodItems.foodId eq foodId }
        }
    }
}