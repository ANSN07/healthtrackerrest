package ie.setu.utils

import ie.setu.domain.*
import ie.setu.domain.db.*
import org.jetbrains.exposed.sql.ResultRow

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
    distance = it[Activities.distance],
    userId = it[Activities.userId]
)

fun mapToIntake(it: ResultRow) = Intake(
    id = it[Intakes.id],
    food = it[Intakes.food],
    numberOfUnits = it[Intakes.numberOfUnits],
    calorie = it[Intakes.calorie],
    userId = it[Intakes.userId]
)

fun mapToBadge(it: ResultRow) = Badge(
    id = it[Badges.id],
    name = it[Badges.name],
    level = it[Badges.level],
    date = it[Badges.date],
    userId = it[Badges.userId]
)

fun mapToGoal(it: ResultRow) = Goal(
    id = it[Goals.id],
    targetWeight = it[Goals.targetWeight],
    targetCalories = it[Goals.targetCalories],
    targetLevel = it[Goals.targetLevel],
    date = it[Goals.date],
    userId = it[Goals.userId]
)

fun mapToWeight(it: ResultRow) = Weight(
    id = it[userWeight.id],
    value = it[userWeight.value],
    date = it[userWeight.date],
    userId = it[userWeight.userId]
)
