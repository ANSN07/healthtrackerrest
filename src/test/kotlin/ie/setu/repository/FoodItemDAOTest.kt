package ie.setu.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import ie.setu.domain.FoodItem
import ie.setu.helpers.foodItems
import ie.setu.helpers.populateFoodItemTable
import ie.setu.helpers.populateUserTable
import kotlin.test.assertEquals

private val foodItem1 = foodItems.get(0)
private val foodItem2 = foodItems.get(1)
private val foodItem3 = foodItems.get(2)

class FoodItemDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateFoodItems {

        @Test
        fun `multiple foodItems added to table can be retrieved successfully`() {
            transaction {
                val userDAO = populateUserTable()
                val foodItemDAO = populateFoodItemTable()
                assertEquals(mutableListOf(foodItem1), foodItemDAO.findByUserId(foodItem1.userId))
                assertEquals(mutableListOf(foodItem2), foodItemDAO.findByUserId(foodItem2.userId))
                assertEquals(mutableListOf(foodItem3), foodItemDAO.findByUserId(foodItem3.userId))
            }
        }
    }

    @Nested
    inner class ReadFoodItems {

        @Test
        fun `get foodItem by user id that has no foodItems, results in no record returned`() {
            transaction {
                val userDAO = populateUserTable()
                val foodItemDAO = populateFoodItemTable()
                assertEquals(0, foodItemDAO.findByUserId(3789).size)
            }
        }

        @Test
        fun `get foodItem by user id that exists, results in a correct foodItems returned`() {
            transaction {
                val userDAO = populateUserTable()
                val foodItemDAO = populateFoodItemTable()
                assertEquals(foodItem1, foodItemDAO.findByUserId(1).get(0))
                assertEquals(foodItem2, foodItemDAO.findByUserId(2).get(0))
                assertEquals(foodItem3, foodItemDAO.findByUserId(3).get(0))
            }
        }

    }

    @Nested
    inner class UpdateFoodItems {

        @Test
        fun `updating existing foodItem in table results in successful update`() {
            transaction {

                val userDAO = populateUserTable()
                val foodItemDAO = populateFoodItemTable()

                val foodItem3updated = FoodItem(foodId = 3, foodName = "A", unitMeasure = "a", calorie = 100, numberOfItems = 100, userId = 3)
                foodItemDAO.updateByFoodId(foodItem3updated.foodId, foodItem3updated)
                assertEquals(mutableListOf(foodItem3updated), foodItemDAO.findByUserId(3))
            }
        }

    }

    @Nested
    inner class DeleteFoodItems {

        @Test
        fun `deleting foodItems when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three foodItems
                val userDAO = populateUserTable()
                val foodItemDAO = populateFoodItemTable()

                //Act & Assert
                assertEquals(1, foodItemDAO.findByUserId(3).size)
                foodItemDAO.deleteByFoodId(3)
                assertEquals(0, foodItemDAO.findByUserId(3).size)
            }
        }
    }
}