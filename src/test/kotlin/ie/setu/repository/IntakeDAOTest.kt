package ie.setu.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import ie.setu.domain.db.Intakes
import ie.setu.domain.Intake
import ie.setu.domain.repository.IntakeDAO
import ie.setu.helpers.intakes
import ie.setu.helpers.populateIntakeTable
import ie.setu.helpers.populateUserTable
import org.joda.time.DateTime
import kotlin.test.assertEquals

//retrieving some test data from Fixtures
private val intake1 = intakes.get(0)
private val intake2 = intakes.get(1)
private val intake3 = intakes.get(2)

class IntakeDAOTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateIntakes {

        @Test
        fun `multiple intakes added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with three users and three intakes
                val userDAO = populateUserTable()
                val intakeDAO = populateIntakeTable()
                //Act & Assert
                assertEquals(3, intakeDAO.getAll().size)
                assertEquals(intake1, intakeDAO.findByIntakeId(intake1.intakeId))
                assertEquals(intake2, intakeDAO.findByIntakeId(intake2.intakeId))
                assertEquals(intake3, intakeDAO.findByIntakeId(intake3.intakeId))
            }
        }
    }

    @Nested
    inner class ReadIntakes {

        @Test
        fun `getting all intakes from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three intakes
                val userDAO = populateUserTable()
                val intakeDAO = populateIntakeTable()
                //Act & Assert
                assertEquals(3, intakeDAO.getAll().size)
            }
        }

        @Test
        fun `get intake by user id that has no intakes, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three intakes
                val userDAO = populateUserTable()
                val intakeDAO = populateIntakeTable()
                //Act & Assert
                assertEquals(0, intakeDAO.findByUserId(3).size)
            }
        }

        @Test
        fun `get intake by user id that exists, results in a correct intake(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three intakes
                val userDAO = populateUserTable()
                val intakeDAO = populateIntakeTable()
                //Act & Assert
                assertEquals(intake1, intakeDAO.findByUserId(1).get(0))
                assertEquals(intake2, intakeDAO.findByUserId(1).get(1))
                assertEquals(intake3, intakeDAO.findByUserId(2).get(0))
            }
        }

        @Test
        fun `get all intakes over empty table returns none`() {
            transaction {

                //Arrange - create and setup intakeDAO object
                SchemaUtils.create(Intakes)
                val intakeDAO = IntakeDAO()

                //Act & Assert
                assertEquals(0, intakeDAO.getAll().size)
            }
        }

        @Test
        fun `get intake by intake id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three intakes
                val userDAO = populateUserTable()
                val intakeDAO = populateIntakeTable()
                //Act & Assert
                assertEquals(null, intakeDAO.findByIntakeId(4))
            }
        }

        @Test
        fun `get intake by intake id that exists, results in a correct intake returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three intakes
                val userDAO = populateUserTable()
                val intakeDAO = populateIntakeTable()
                //Act & Assert
                assertEquals(intake1, intakeDAO.findByIntakeId(1))
                assertEquals(intake3, intakeDAO.findByIntakeId(3))
            }
        }
    }

    @Nested
    inner class UpdateIntakes {

        @Test
        fun `updating existing intake in table results in successful update`() {
            transaction {

                //Arrange - create and populate tables with three users and three intakes
                val userDAO = populateUserTable()
                val intakeDAO = populateIntakeTable()

                //Act & Assert
                val intake3updated = Intake(intakeId = 3, mealType = "ABC",
                    date = DateTime.now(), userId = 2)
                intakeDAO.updateByIntakeId(intake3updated.intakeId, intake3updated)
                assertEquals(intake3updated, intakeDAO.findByIntakeId(3))
            }
        }

        @Test
        fun `updating non-existant intake in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three intakes
                val userDAO = populateUserTable()
                val intakeDAO = populateIntakeTable()

                //Act & Assert
                val intake4updated = Intake(intakeId = 3, mealType = "ABC",
                    date = DateTime.now(), userId = 2)
                intakeDAO.updateByIntakeId(4, intake4updated)
                assertEquals(null, intakeDAO.findByIntakeId(4))
                assertEquals(3, intakeDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteIntakes {

        @Test
        fun `deleting a non-existant intake (by id) in table results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three intakes
                val userDAO = populateUserTable()
                val intakeDAO = populateIntakeTable()

                //Act & Assert
                assertEquals(3, intakeDAO.getAll().size)
                intakeDAO.deleteByIntakeId(4)
                assertEquals(3, intakeDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing intake (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three intakes
                val userDAO = populateUserTable()
                val intakeDAO = populateIntakeTable()

                //Act & Assert
                assertEquals(3, intakeDAO.getAll().size)
                intakeDAO.deleteByIntakeId(intake3.intakeId)
                assertEquals(2, intakeDAO.getAll().size)
            }
        }


        @Test
        fun `deleting intakes when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three intakes
                val userDAO = populateUserTable()
                val intakeDAO = populateIntakeTable()

                //Act & Assert
                assertEquals(3, intakeDAO.getAll().size)
                intakeDAO.deleteByUserId(3)
                assertEquals(3, intakeDAO.getAll().size)
            }
        }

        @Test
        fun `deleting intakes when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three intakes
                val userDAO = populateUserTable()
                val intakeDAO = populateIntakeTable()

                //Act & Assert
                assertEquals(3, intakeDAO.getAll().size)
                intakeDAO.deleteByUserId(1)
                assertEquals(1, intakeDAO.getAll().size)
            }
        }
    }
}