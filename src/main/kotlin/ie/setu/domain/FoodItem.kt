package ie.setu.domain

data class FoodItem (var foodId: Int,
                 var foodName: String,
                 var calorie: Int,
                 var unitMeasure: String,
                 var numberOfItems: Int,
                 var userId: Int)