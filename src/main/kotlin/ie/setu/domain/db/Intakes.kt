package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one activity.
//       Database wise, this is the table object.

object Intakes : Table("intakes") {
    val id = integer("id").autoIncrement().primaryKey()
    val food = varchar("food", 100)
    val calorie = integer("calorie")
    val numberOfUnits = integer("numberOfUnits")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)

}
