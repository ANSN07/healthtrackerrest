package ie.setu.domain.repository

import ie.setu.domain.FoodItemIntake
import ie.setu.domain.db.FoodItemIntakes
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class FoodItemIntakeDAO {

    fun save(foodItemIntake: FoodItemIntake) : Int?{
        return transaction {
            FoodItemIntakes.insert {
                it[foodId] = foodItemIntake.foodId
                it[intakeId] = foodItemIntake.intakeId
                it[numberOfItems] = foodItemIntake.numberOfItems
            } get FoodItemIntakes.foodId
        }
    }

    fun updateByFoodIntakeId(foodIntakeId: Int, foodItemIntakeDTO: FoodItemIntake): Int {
        return transaction {
            FoodItemIntakes.update ({
                FoodItemIntakes.foodIntakeId eq foodIntakeId}) {
                it[foodId] = foodItemIntakeDTO.foodId
                it[intakeId] = foodItemIntakeDTO.intakeId
                it[numberOfItems] = foodItemIntakeDTO.numberOfItems
            }
        }
    }

    fun deleteByFoodIntakeId (foodIntakeId: Int): Int{
        return transaction{
            FoodItemIntakes.deleteWhere { FoodItemIntakes.foodIntakeId eq foodIntakeId }
        }
    }
}