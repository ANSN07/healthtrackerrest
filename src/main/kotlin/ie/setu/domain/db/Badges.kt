package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Badges : Table("badges") {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 100)
    val level = integer("level")
    val date = datetime("date")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}