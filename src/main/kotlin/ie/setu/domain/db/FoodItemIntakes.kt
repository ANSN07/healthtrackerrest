package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

object FoodItemIntakes : Table("foodItemIntake") {
    val foodIntakeId = integer("foodintake_id").autoIncrement().primaryKey()
    val foodId = integer("food_id")
    val intakeId = integer("intake_id")
    val numberOfItems = integer("number_of_items")
}