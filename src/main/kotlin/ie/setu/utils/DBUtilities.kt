package ie.setu.utils

import ie.setu.domain.User
import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.ResultRow
import ie.setu.domain.Activity
import ie.setu.domain.Intake
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Intakes

fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email]
)

fun mapToActivity(it: ResultRow) = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)

fun mapToIntake(it: ResultRow) = Intake(
    id = it[Intakes.id],
    food = it[Intakes.food],
    numberOfUnits = it[Intakes.numberOfUnits],
    calorie = it[Intakes.calorie],
    userId = it[Intakes.userId]
)