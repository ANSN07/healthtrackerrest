package ie.setu.domain

import org.joda.time.DateTime

data class Badge (var id: Int,
                 var name:String,
                 var level: Int,
                 var date: DateTime,
                 var userId: Int
                 )