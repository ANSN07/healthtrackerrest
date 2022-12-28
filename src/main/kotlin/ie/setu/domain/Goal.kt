package ie.setu.domain

import org.joda.time.DateTime

data class Goal (var id: Int,
                 var targetWeight: Int,
                 var targetCalories: Int,
                 var targetLevel: Int,
                 var date: DateTime,
                 var userId: Int)