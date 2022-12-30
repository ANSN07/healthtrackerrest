package ie.setu.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import ie.setu.domain.Goal
import ie.setu.domain.Weight
import ie.setu.helpers.weights
import ie.setu.helpers.populateGoalTable
import ie.setu.helpers.populateUserTable
import ie.setu.helpers.populateWeightTable
import kotlin.test.assertEquals

private val weight1 = weights.get(0)
private val weight2 = weights.get(1)
private val weight3 = weights.get(2)

class WeightDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateWeight {

        @Test
        fun `multiple weight added to table can be retrieved successfully`() {
            transaction {
                val userDAO = populateUserTable()
                val weightDAO = populateWeightTable()
                assertEquals(mutableListOf(weight1, weight2), weightDAO.findByUserId(weight1.userId))
                assertEquals(mutableListOf(weight3), weightDAO.findByUserId(weight3.userId))
            }
        }
    }

    @Nested
    inner class ReadGoals {

        @Test
        fun `get weight by user id that has no weights, results in no record returned`() {
            transaction {
                val userDAO = populateUserTable()
                val weightDAO = populateWeightTable()
                assertEquals(0, weightDAO.findByUserId(3789).size)
            }
        }

        @Test
        fun `get weight by user id that exists, results in a correct weights returned`() {
            transaction {
                val userDAO = populateUserTable()
                val weightDAO = populateWeightTable()
                assertEquals(weight1, weightDAO.findByUserId(1).get(0))
                assertEquals(weight2, weightDAO.findByUserId(1).get(1))
                assertEquals(weight3, weightDAO.findByUserId(3).get(0))
            }
        }

    }

    @Nested
    inner class UpdateGoals {

        @Test
        fun `updating existing weight in table results in successful update`() {
            transaction {

                val userDAO = populateUserTable()
                val weightDAO = populateWeightTable()

                val weight3updated = Weight(id = 3, value = 70, date = DateTime.now(), userId = 3)
                weightDAO.updateById(weight3updated.id, weight3updated)
                assertEquals(mutableListOf(weight3updated), weightDAO.findByUserId(3))
            }
        }

    }

    @Nested
    inner class DeleteGoals {

        @Test
        fun `deleting goals when 1 or more exist for user id results in deletion`() {
            transaction {

                val userDAO = populateUserTable()
                val weightDAO = populateWeightTable()

                assertEquals(1, weightDAO.findByUserId(3).size)
                weightDAO.deleteById(3)
                assertEquals(0, weightDAO.findByUserId(3).size)
            }
        }
    }
}