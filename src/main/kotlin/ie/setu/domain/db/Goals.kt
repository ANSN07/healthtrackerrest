package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Goals : Table("goals") {
    val id = integer("id").autoIncrement().primaryKey()
    val targetWeight = integer("targetweight")
    val targetCalories = integer("targetcalories")
    val targetLevel = integer("targetlevel")
    val date = datetime("date")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}