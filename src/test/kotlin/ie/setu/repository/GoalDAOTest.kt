package ie.setu.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import ie.setu.domain.Goal
import ie.setu.helpers.goals
import ie.setu.helpers.populateGoalTable
import ie.setu.helpers.populateUserTable
import kotlin.test.assertEquals

private val goal1 = goals.get(0)
private val goal2 = goals.get(1)
private val goal3 = goals.get(2)

class GoalDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateGoals {

        @Test
        fun `multiple goals added to table can be retrieved successfully`() {
            transaction {
                val userDAO = populateUserTable()
                val goalDAO = populateGoalTable()
                assertEquals(mutableListOf(goal1), goalDAO.findByUserId(goal1.userId))
                assertEquals(mutableListOf(goal2), goalDAO.findByUserId(goal2.userId))
                assertEquals(mutableListOf(goal3), goalDAO.findByUserId(goal3.userId))
            }
        }
    }

    @Nested
    inner class ReadGoals {

        @Test
        fun `get goal by user id that has no goals, results in no record returned`() {
            transaction {
                val userDAO = populateUserTable()
                val goalDAO = populateGoalTable()
                assertEquals(0, goalDAO.findByUserId(3789).size)
            }
        }

        @Test
        fun `get goal by user id that exists, results in a correct goals returned`() {
            transaction {
                val userDAO = populateUserTable()
                val goalDAO = populateGoalTable()
                assertEquals(goal1, goalDAO.findByUserId(1).get(0))
                assertEquals(goal2, goalDAO.findByUserId(2).get(0))
                assertEquals(goal3, goalDAO.findByUserId(3).get(0))
            }
        }

    }

    @Nested
    inner class UpdateGoals {

        @Test
        fun `updating existing goal in table results in successful update`() {
            transaction {

                val userDAO = populateUserTable()
                val goalDAO = populateGoalTable()

                val goal3updated = Goal(id = 3, targetWeight = 40, targetCalories = 40,
                    targetLevel = 2, date = DateTime.now(), userId = 3)
                goalDAO.updateById(goal3updated.id, goal3updated)
                assertEquals(mutableListOf(goal3updated), goalDAO.findByUserId(3))
            }
        }

    }

    @Nested
    inner class DeleteGoals {

        @Test
        fun `deleting goals when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three goals
                val userDAO = populateUserTable()
                val goalDAO = populateGoalTable()

                //Act & Assert
                assertEquals(1, goalDAO.findByUserId(3).size)
                goalDAO.deleteByUserId(3)
                assertEquals(0, goalDAO.findByUserId(3).size)
            }
        }
    }
}