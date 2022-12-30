package ie.setu.domain

import org.joda.time.DateTime

data class Intake (
    var intakeId: Int,
    var mealType: String,
    var date: DateTime,
    var userId: Int)