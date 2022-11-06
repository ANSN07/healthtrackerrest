package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.Intake
import ie.setu.domain.User
import ie.setu.helpers.ServerContainer
import ie.setu.helpers.*
import ie.setu.utils.jsonToObject
import org.junit.jupiter.api.TestInstance
import kong.unirest.Unirest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import org.joda.time.DateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IntakeControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()


    @Nested
    inner class ReadIntakes {
        @Test
        fun `get all intakes from the database returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/intakes/").asString()
            if (response.status == 200) {
                val retrievedIntakes: ArrayList<Intake> = jsonToObject(response.body.toString())
                assertNotEquals(0, retrievedIntakes.size)
            } else {
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get intakes by intake id when intake does not exist returns 404 response`() {

            //Arrange - test data for intake id
            val intakeId = Integer.MIN_VALUE

            // Act - attempt to retrieve the non-existent intake from the database
            val retrieveResponse = retrieveIntakeByIntakeId(intakeId)

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `get intakes by user id when user does not exist returns 404 response`() {

            //Arrange - test data for intake id
            val userId = Integer.MIN_VALUE

            // Act - attempt to retrieve the intake of a non-existent user from the database
            val retrieveResponse = retrieveIntakeByUserId(userId)

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `getting intakes by intake id when id exists, returns a 200 response`() {

            val response = retrieveUserByEmail(validEmail)
            val validUser : User = jsonToObject(response.body.toString())

            //Arrange - add the intake
            val addResponse = addIntake(validFood, validCalorie, validNumberOfUnits, validUser.id)
            val addedIntake : Intake = jsonToObject(addResponse.body.toString())

            //Assert - retrieve the added intake from the database and verify return code
            val retrieveResponse = retrieveIntakeByIntakeId(addedIntake.id)
            assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added intake
            deleteIntakeByIntakeId(addedIntake.id)

        }

        @Test
        fun `getting intakes by user id when id exists, returns a 200 response`() {

            val response = retrieveUserByEmail(validEmail)
            val validUser : User = jsonToObject(response.body.toString())

            //Arrange - add the intake
            val addResponse = addIntake(validFood, validCalorie, validNumberOfUnits, validUser.id)
            val addedIntake : Intake = jsonToObject(addResponse.body.toString())

            //Assert - retrieve the added intake from the database and verify return code
            val retrieveResponse = retrieveIntakeByUserId(addedIntake.userId)
            assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added intake
            deleteIntakeByIntakeId(addedIntake.id)

        }

    }

    @Nested
    inner class CreateIntakes {
        @Test
        fun `add an intake with correct details returns a 201 response`() {
            val response = retrieveUserByEmail(validEmail)
            val validUser : User = jsonToObject(response.body.toString())

            //Arrange & Act & Assert
            //    add the intake and verify return code (using fixture data)
            val addResponse = addIntake(validFood, validCalorie, validNumberOfUnits, validUser.id)
            assertEquals(201, addResponse.status)

            //Assert - retrieve the added intake from the database and verify return code
            val retrieveResponse = retrieveIntakeByUserId(validUser.id)
            assertEquals(200, retrieveResponse.status)

            //Assert - verify the contents of the retrieved intake
            val retrievedIntake : Intake = jsonToObject(addResponse.body.toString())
            assertEquals(validFood, retrievedIntake.food)
            assertEquals(validCalorie, retrievedIntake.calorie)
            assertEquals(validNumberOfUnits, retrievedIntake.numberOfUnits)
            assertEquals(validUserId, retrievedIntake.userId)

            //After - restore the db to previous state by deleting the added intake
            val deleteResponse = deleteIntakeByIntakeId(retrievedIntake.id)
            assertEquals(204, deleteResponse.status)

        }
    }

    @Nested
    inner class UpdateIntakes {
        @Test
        fun `updating a intake when it exists, returns a 204 response`() {
            val response = retrieveUserByEmail(validEmail)
            val validUser : User = jsonToObject(response.body.toString())
            //Arrange - add the intake that we plan to do an update on
            val addedResponse = addIntake(validFood, validCalorie, validNumberOfUnits, validUser.id)
            val addedIntake : Intake = jsonToObject(addedResponse.body.toString())

            //Act & Assert - update the contents of the retrieved intake and assert 204 is returned
            assertEquals(204, updateIntake(addedIntake.id, updatedFood, updatedCalorie, updatedCalorie, updatedUserId).status)

            //Act & Assert - retrieve updated intake and assert details are correct
            val updatedIntakeResponse = retrieveIntakeByIntakeId(addedIntake.id)
            val updatedIntake : Intake = jsonToObject(updatedIntakeResponse.body.toString())
            assertEquals(validFood, updatedIntake.food)
            assertEquals(validCalorie, updatedIntake.calorie)
            assertEquals(validNumberOfUnits, updatedIntake.numberOfUnits)
            assertEquals(validUserId, updatedIntake.userId)

            //After - restore the db to previous state by deleting the added intake
            deleteIntakeByIntakeId(addedIntake.id)

        }

        @Test
        fun `updating a intake when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to update contents of intake that doesn't exist
            assertEquals(404, updateIntake(-1, updatedFood, updatedCalorie, updatedNumberOfUnits, updatedUserId).status)
        }
    }

    @Nested
    inner class DeleteIntakes {
        @Test
        fun `deleting a intake when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete an intake that doesn't exist
            assertEquals(404, deleteIntakeByIntakeId(-1).status)
        }

        @Test
        fun `deleting intakes of a user when user id doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete an intake that doesn't exist
            assertEquals(404, deleteIntakeByUserId(-1).status)
        }

        @Test
        fun `deleting an intake when it exists, returns a 204 response`() {

            //Arrange - add the intake that we plan to do a delete on
            val addedResponse = addIntake(validFood, validCalorie, validNumberOfUnits, validUserId)
            val addedIntake : Intake = jsonToObject(addedResponse.body.toString())

            //Act & Assert - delete the added intake and assert a 204 is returned
            assertEquals(204, deleteIntakeByIntakeId(addedIntake.id).status)

            //Act & Assert - attempt to retrieve the deleted intake --> 404 response
            assertEquals(404, retrieveIntakeByIntakeId(addedIntake.id).status)

        }

        @Test
        fun `deleting intake of a user when user id exists, returns a 204 response`() {

            val response = retrieveUserByEmail(validEmail)
            val validUser : User = jsonToObject(response.body.toString())
            //Arrange - add the intake that we plan to do a delete on
            val addedResponse = addIntake(validFood, validCalorie, validNumberOfUnits, validUser.id)
            val addedIntake : Intake = jsonToObject(addedResponse.body.toString())

            //Act & Assert - delete the added intake and assert a 204 is returned
            assertEquals(204, deleteIntakeByUserId(addedIntake.userId).status)

            //Act & Assert - attempt to retrieve the deleted intake --> 404 response
            assertEquals(404, retrieveIntakeByUserId(addedIntake.userId).status)

        }
    }

    //helper function to add a test intake to the database
    private fun addIntake (food: String, calorie: Int, numberOfUnits: Int, userId: Int): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/intakes")
            .body("{\"food\":\"$food\", \"calorie\":\"$calorie\", \"numberOfUnits\":\"$numberOfUnits\", \"userId\":\"$userId\"}")
            .asJson()
    }

    //helper function to delete a test intake from the database
    private fun deleteIntakeByIntakeId (intakeId: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/intakes/${intakeId}").asString()
    }

    //helper function to delete a test intake from the database
    private fun deleteIntakeByUserId (userId: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/users/${userId}/intakes").asString()
    }

    //helper function to retrieve a test intake from the database by intake id
    private fun retrieveIntakeByIntakeId(intakeId: Int) : HttpResponse<String> {
        return Unirest.get(origin + "/api/intakes/${intakeId}").asString()
    }

    //helper function to retrieve a test intake from the database by user id
    private fun retrieveIntakeByUserId(userId: Int) : HttpResponse<String> {
        return Unirest.get(origin + "/api/users/${userId}/intakes").asString()
    }

    //helper function to update a test intake to the database
    private fun updateIntake (id: Int, food: String, calorie: Int, numberOfUnits: Int, userId: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/intakes/${id}")
            .body("{\"food\":\"$food\", \"calorie\":\"$calorie\", \"numberOfUnits\":\"$numberOfUnits\", \"userId\":\"$userId\"}")
            .asJson()
    }

    private fun retrieveUserByEmail(email : String) : HttpResponse<String> {
        return Unirest.get(origin + "/api/users/email/${email}").asString()
    }

}