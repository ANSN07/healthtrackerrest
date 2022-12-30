package ie.setu.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import ie.setu.helpers.badges
import ie.setu.helpers.populateBadgeTable
import ie.setu.helpers.populateUserTable
import kotlin.test.assertEquals

private val badge1 = badges.get(0)
private val badge2 = badges.get(1)
private val badge3 = badges.get(2)

class BadgeDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateBadges {

        @Test
        fun `multiple badges added to table can be retrieved successfully`() {
            transaction {
                val userDAO = populateUserTable()
                val badgeDAO = populateBadgeTable()
                assertEquals(mutableListOf(badge1), badgeDAO.findByUserId(badge1.userId))
                assertEquals(mutableListOf(badge2), badgeDAO.findByUserId(badge2.userId))
                assertEquals(mutableListOf(badge3), badgeDAO.findByUserId(badge3.userId))
            }
        }
    }

    @Nested
    inner class ReadBadges {

        @Test
        fun `get badges by user id that has no badges, results in no record returned`() {
            transaction {
                val userDAO = populateUserTable()
                val badgeDAO = populateBadgeTable()
                assertEquals(0, badgeDAO.findByUserId(8999).size)
            }
        }

        @Test
        fun `get badges by user id that exists, results in correct badges returned`() {
            transaction {
                val userDAO = populateUserTable()
                val badgeDAO = populateBadgeTable()
                assertEquals(badge1, badgeDAO.findByUserId(1).get(0))
                assertEquals(badge2, badgeDAO.findByUserId(3).get(0))
                assertEquals(badge3, badgeDAO.findByUserId(2).get(0))
            }
        }
    }

}