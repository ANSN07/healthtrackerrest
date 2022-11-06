package ie.setu.domain

data class Intake (
    var id: Int,
    var food: String,
    var calorie: Int,
    var userId: Int,
    var numberOfUnits: Int)