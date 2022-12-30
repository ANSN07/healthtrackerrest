package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Intakes : Table("intakes") {
    val intakeId = integer("intake_id").autoIncrement().primaryKey()
    val mealType = varchar("meal_type", 100)
    val date = datetime("date")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}
