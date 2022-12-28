package ie.setu.domain

import org.joda.time.DateTime

data class Weight (var id: Int,
                 var value: Int,
                 var date: DateTime,
                 var userId: Int)