package ie.setu.domain.repository

import ie.setu.domain.Badge
import ie.setu.domain.db.Badges
import ie.setu.utils.mapToBadge
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class BadgeDAO {

    fun findByUserId(userId: Int): List<Badge>{
        return transaction {
            Badges
                .select { Badges.userId eq userId}
                .map { mapToBadge(it) }
        }
    }

    fun save(badge: Badge) {
         transaction {
            Badges.insert {
                it[name] = badge.name
                it[level] = badge.level
                it[date] = badge.date
                it[userId] = badge.userId
            } get Badges.id
        }
    }

}