package ie.setu.helpers

import ie.setu.domain.Activity
import ie.setu.domain.Intake
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Intakes
import ie.setu.domain.db.Users
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.IntakeDAO
import ie.setu.domain.repository.UserDAO
import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser@test.com"
val updatedName = "Updated Name"
val updatedEmail = "Updated Email"
val validDescription = "Test Description"
val validDistance = 120.0
val validDuration = 12.0
val validCalories = 120
val validStarted = DateTime.now()
val validUserId = 2
val updatedDescription = "Updated Description"
val updatedDuration = 13.0
val updatedDistance = 130.0
val updatedCalories = 122
val updatedStarted = DateTime.now()
val updatedUserId = 2
val validNumberOfUnits = 1
val validFood = "test"
val validCalorie = 300
val updatedFood = "Updated Food"
val updatedNumberOfUnits = 13
val updatedCalorie = 122



val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4)
)

val activities = arrayListOf<Activity>(
    Activity(id = 1, description = "Running", duration = 22.0, calories = 230, started = DateTime.now(), distance = 230.0, userId = 1),
    Activity(id = 2, description = "Hopping", duration = 10.5, calories = 80, started = DateTime.now(), distance = 430.0, userId = 1),
    Activity(id = 3, description = "Walking", duration = 12.0, calories = 120, started = DateTime.now(), distance = 80.0, userId = 2)
)

val intakes = arrayListOf<Intake>(
    Intake(intakeId = 1, mealType = "A", date = DateTime.now(), userId = 1),
    Intake(intakeId = 2, mealType = "B", date = DateTime.now(), userId = 1),
    Intake(intakeId = 3, mealType = "C", date = DateTime.now(), userId = 2)
)

fun populateUserTable(): UserDAO {
    SchemaUtils.create(Users)
    val userDAO = UserDAO()
    userDAO.save(users.get(0))
    userDAO.save(users.get(1))
    userDAO.save(users.get(2))
    return userDAO
}
fun populateActivityTable(): ActivityDAO {
    SchemaUtils.create(Activities)
    val activityDAO = ActivityDAO()
    activityDAO.save(activities.get(0))
    activityDAO.save(activities.get(1))
    activityDAO.save(activities.get(2))
    return activityDAO
}

fun populateIntakeTable(): IntakeDAO {
    SchemaUtils.create(Intakes)
    val intakeDAO = IntakeDAO()
    intakeDAO.save(intakes.get(0))
    intakeDAO.save(intakes.get(1))
    intakeDAO.save(intakes.get(2))
    return intakeDAO
}