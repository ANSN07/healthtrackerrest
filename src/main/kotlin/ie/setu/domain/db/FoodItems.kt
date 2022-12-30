package ie.setu.domain.db

import ie.setu.domain.db.Intakes.references
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object FoodItems : Table("foodItems") {
    val foodId = integer("food_id").autoIncrement().primaryKey()
    val foodName = varchar("food_name", 100)
    val calorie = integer("calorie")
    val unitMeasure = varchar("unit_measure", 100)
    val numberOfItems = integer("number_of_items")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}