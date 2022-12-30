package ie.setu.helpers

import ie.setu.domain.*
import ie.setu.domain.db.*
import ie.setu.domain.repository.*
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

val goals = arrayListOf<Goal>(
    Goal(id = 1, targetWeight = 30, targetLevel = 4, targetCalories = 120, date = DateTime.now(), userId = 1),
    Goal(id = 2, targetWeight = 70, targetLevel = 2, targetCalories = 130, date = DateTime.now(), userId = 2),
    Goal(id = 3, targetWeight = 40, targetLevel = 1, targetCalories = 140, date = DateTime.now(), userId = 3)
)

val badges = arrayListOf<Badge>(
    Badge(id = 1, name = "A", level = 4, date = DateTime.now(), userId = 1),
    Badge(id = 2, name = "B", level = 4, date = DateTime.now(), userId = 3),
    Badge(id = 3, name = "C", level = 4, date = DateTime.now(), userId = 2)
)

val weights = arrayListOf<Weight>(
    Weight(id = 1, value = 30, date = DateTime.now(), userId = 1),
    Weight(id = 2, value = 40, date = DateTime.now(), userId = 1),
    Weight(id = 3, value = 50, date = DateTime.now(), userId = 3)
)

val foodItems = arrayListOf<FoodItem>(
    FoodItem(foodId = 1, foodName = "A", unitMeasure = "a", calorie = 100, numberOfItems = 100, userId = 1),
    FoodItem(foodId = 2, foodName = "N", unitMeasure = "b", calorie = 100, numberOfItems = 100, userId = 2),
    FoodItem(foodId = 3, foodName = "P", unitMeasure = "c", calorie = 100, numberOfItems = 100, userId = 3)
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

fun populateBadgeTable(): BadgeDAO {
    SchemaUtils.create(Badges)
    val badgeDAO = BadgeDAO()
    badgeDAO.save(badges.get(0))
    badgeDAO.save(badges.get(1))
    badgeDAO.save(badges.get(2))
    return badgeDAO
}

fun populateWeightTable(): WeightDAO {
    SchemaUtils.create(userWeight)
    val weightDAO = WeightDAO()
    weightDAO.save(weights.get(0))
    weightDAO.save(weights.get(1))
    weightDAO.save(weights.get(2))
    return weightDAO
}

fun populateGoalTable(): GoalDAO {
    SchemaUtils.create(Goals)
    val goalDAO = GoalDAO()
    goalDAO.save(goals.get(0))
    goalDAO.save(goals.get(1))
    goalDAO.save(goals.get(2))
    return goalDAO
}

fun populateFoodItemTable(): FoodItemDAO {
    SchemaUtils.create(FoodItems)
    val foodItemDAO = FoodItemDAO()
    foodItemDAO.save(foodItems.get(0))
    foodItemDAO.save(foodItems.get(1))
    foodItemDAO.save(foodItems.get(2))
    return foodItemDAO
}