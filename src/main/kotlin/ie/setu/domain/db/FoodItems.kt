package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

object FoodItems : Table("foodItems") {
    val foodId = integer("food_id").autoIncrement().primaryKey()
    val foodName = varchar("food_name", 100)
    val calorie = integer("calorie")
    val unitMeasure = varchar("unit_measure", 100)
}