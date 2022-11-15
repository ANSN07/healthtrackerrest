package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.controllers.UserController.addUser
import ie.setu.domain.Activity
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
class ActivityControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()


    @Nested
    inner class ReadActivities {
        @Test
        fun `get all activities from the database returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/activities/").asString()
            if (response.status == 200) {
                val retrievedActivities: ArrayList<Activity> = jsonToObject(response.body.toString())
                assertNotEquals(0, retrievedActivities.size)
            } else {
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get activities by activity id when activity does not exist returns 404 response`() {

            //Arrange - test data for activity id
            val activityId = Integer.MIN_VALUE

            // Act - attempt to retrieve the non-existent activity from the database
            val retrieveResponse = retrieveActivityByActivityId(activityId)

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `get activities by user id when user does not exist returns 404 response`() {

            //Arrange - test data for activity id
            val userId = Integer.MIN_VALUE

            // Act - attempt to retrieve the activity of a non-existent user from the database
            val retrieveResponse = retrieveActivityByUserId(userId)

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `getting activities by activity id when id exists, returns a 200 response`() {

            val response = retrieveUserByEmail(validEmail)
            val validUser : User = jsonToObject(response.body.toString())

            //Arrange - add the activity
            val addResponse = addActivity(validDescription, validDuration, validStarted, validCalories, validUser.id)
            val addedActivity : Activity = jsonToObject(addResponse.body.toString())

            //Assert - retrieve the added activity from the database and verify return code
            val retrieveResponse = retrieveActivityByActivityId(addedActivity.id)
            assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added activity
            deleteActivityByActivityId(addedActivity.id)

        }

        @Test
        fun `getting activities by user id when id exists, returns a 200 response`() {


            val addedResponse = addUser(validName, validEmail)
            val response = retrieveUserByEmail(validEmail)
            val validUser : User = jsonToObject(response.body.toString())

            //Arrange - add the activity
            val addResponse = addActivity(validDescription, validDuration, validStarted, validCalories, validUser.id)
            val addedActivity : Activity = jsonToObject(addResponse.body.toString())

            //Assert - retrieve the added activity from the database and verify return code
            val retrieveResponse = retrieveActivityByUserId(addedActivity.userId)
            assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added activity
            deleteActivityByActivityId(addedActivity.id)

        }

    }

    @Nested
    inner class CreateActivities {
        @Test
        fun `add an activity with correct details returns a 201 response`() {
            addUser(validName, validEmail)
            val response = retrieveUserByEmail(validEmail)
            val validUser : User = jsonToObject(response.body.toString())

            //Arrange & Act & Assert
            //    add the activity and verify return code (using fixture data)
            val addResponse = addActivity(validDescription, validDuration, validStarted, validCalories, validUser.id)
            assertEquals(201, addResponse.status)

            //Assert - retrieve the added activity from the database and verify return code
            val retrieveResponse = retrieveActivityByUserId(validUser.id)
            assertEquals(200, retrieveResponse.status)

            //Assert - verify the contents of the retrieved activity
            val retrievedActivity : Activity = jsonToObject(addResponse.body.toString())
            assertEquals(validDescription, retrievedActivity.description)
            assertEquals(validCalories, retrievedActivity.calories)
            assertEquals(validStarted.toString(), retrievedActivity.started.toString())
            assertEquals(validDuration, retrievedActivity.duration)

            //After - restore the db to previous state by deleting the added activity
            val deleteResponse = deleteActivityByActivityId(retrievedActivity.id)
            assertEquals(204, deleteResponse.status)

        }
    }

    @Nested
    inner class UpdateActivities {
        @Test
        fun `updating a activity when it exists, returns a 204 response`() {
            addUser(validName, validEmail)
            val response = retrieveUserByEmail(validEmail)
            val validUser : User = jsonToObject(response.body.toString())
            //Arrange - add the activity that we plan to do an update on
            val addedResponse = addActivity(validDescription, validDuration, validStarted, validCalories, validUser.id)
            val addedActivity : Activity = jsonToObject(addedResponse.body.toString())

            //Act & Assert - update the contents of the retrieved activity and assert 204 is returned
            assertEquals(204, updateActivity(addedActivity.id, updatedDescription, updatedDuration, updatedStarted, updatedCalories, validUser.id).status)

            //Act & Assert - retrieve updated activity and assert details are correct
            val updatedActivityResponse = retrieveActivityByActivityId(addedActivity.id)
            val updatedActivity : Activity = jsonToObject(updatedActivityResponse.body.toString())
            assertEquals(updatedDescription, updatedActivity.description)
            assertEquals(updatedCalories, updatedActivity.calories)
            assertEquals(updatedStarted.toString(), updatedActivity.started.toString())
            assertEquals(updatedDuration, updatedActivity.duration)

            //After - restore the db to previous state by deleting the added activity
            deleteActivityByActivityId(addedActivity.id)
            deleteUser(validUser.id)
        }

        @Test
        fun `updating a activity when it doesn't exist, returns a 404 response`() {

            addUser(validName, validEmail)
            val response = retrieveUserByEmail(validEmail)
            val validUser : User = jsonToObject(response.body.toString())

            //Act & Assert - attempt to update contents of activity that doesn't exist
            assertEquals(404, updateActivity(-1, updatedDescription, updatedDuration, updatedStarted, updatedCalories, validUser.id).status)
            deleteUser(validUser.id)
        }

    }

    @Nested
    inner class DeleteActivities {
        @Test
        fun `deleting a activity when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete an activity that doesn't exist
            assertEquals(404, deleteActivityByActivityId(-1).status)
        }

        @Test
        fun `deleting activities of a user when user id doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete an activity that doesn't exist
            assertEquals(404, deleteActivityByUserId(-1).status)
        }

        @Test
        fun `deleting an activity when it exists, returns a 204 response`() {

            addUser(validName, validEmail)
            val response = retrieveUserByEmail(validEmail)
            val validUser : User = jsonToObject(response.body.toString())

             //Arrange - add the activity that we plan to do a delete on
            val addedResponse = addActivity(validDescription, validDuration, validStarted, validCalories, validUser.id)
            val addedActivity : Activity = jsonToObject(addedResponse.body.toString())

            //Act & Assert - delete the added activity and assert a 204 is returned
            assertEquals(204, deleteActivityByActivityId(addedActivity.id).status)

            //Act & Assert - attempt to retrieve the deleted activity --> 404 response
            assertEquals(404, retrieveActivityByActivityId(addedActivity.id).status)

            deleteUser(validUser.id)

        }

        @Test
        fun `deleting activity of a user when user id exists, returns a 204 response`() {
            addUser(validName, validEmail)
            val response = retrieveUserByEmail(validEmail)
            val validUser : User = jsonToObject(response.body.toString())
            //Arrange - add the activity that we plan to do a delete on
            val addedResponse = addActivity(validDescription, validDuration, validStarted, validCalories, validUser.id)
            val addedActivity : Activity = jsonToObject(addedResponse.body.toString())

            //Act & Assert - delete the added activity and assert a 204 is returned
            assertEquals(204, deleteActivityByUserId(addedActivity.userId).status)

            //Act & Assert - attempt to retrieve the deleted activity --> 404 response
            assertEquals(404, retrieveActivityByUserId(addedActivity.userId).status)
            deleteUser(validUser.id)
        }
    }

    //helper function to add a test activity to the database
    private fun addActivity (description: String, duration: Double, started: DateTime, calories: Int, userId: Int): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/activities")
            .body("{\"description\":\"$description\", \"duration\":\"$duration\", \"started\":\"$started\", \"calories\":\"$calories\", \"userId\":\"$userId\"}")
            .asJson()
    }

    //helper function to delete a test activity from the database
    private fun deleteActivityByActivityId (activityId: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/activities/${activityId}").asString()
    }

    //helper function to delete a test activity from the database
    private fun deleteActivityByUserId (userId: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/users/${userId}/activities").asString()
    }

    //helper function to retrieve a test activity from the database by activity id
    private fun retrieveActivityByActivityId(activityId: Int) : HttpResponse<String> {
        return Unirest.get(origin + "/api/activities/${activityId}").asString()
    }

    //helper function to retrieve a test activity from the database by user id
    private fun retrieveActivityByUserId(userId: Int) : HttpResponse<String> {
        return Unirest.get(origin + "/api/users/${userId}/activities").asString()
    }

    //helper function to update a test activity to the database
    private fun updateActivity (id: Int, description: String, duration: Double, started: DateTime, calories: Int, userId: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/activities/${id}")
            .body("{\"description\":\"$description\", \"duration\":\"$duration\", \"started\":\"$started\", \"calories\":\"$calories\", \"userId\":\"$userId\"}")
            .asJson()
    }

    private fun retrieveUserByEmail(email : String) : HttpResponse<String> {
        return Unirest.get(origin + "/api/users/email/${email}").asString()
    }


    //helper function to add a test user to the database
    private fun addUser (name: String, email: String): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/users")
            .body("{\"name\":\"$name\", \"email\":\"$email\"}")
            .asJson()
    }

    //helper function to delete a test user from the database
    private fun deleteUser (id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/users/${id}").asString()
    }

}