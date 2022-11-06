#### healthtrackerrest

# Health Tracker App

This application basically collects data from the user and tracks the health status of user by summarizing data.
Basic details of user are collected like name and email, each user will have a unique ID.
Each User can add several activities they had done done during the day where each activity will have a short description, unique ID, calories burned, duration of the activity, start time and date and finally the user id.
User can add the intakes they had which includes the name of the food, amount of calories, number of units consumed, user id and a unique id.

//USERS - API CRUD

get("/api/users", HealthTrackerAPI::getAllUsers)

get("/api/users/:user-id", HealthTrackerAPI::getUserByUserId)

get("/api/users/email/:email", HealthTrackerAPI::getUserByEmail)

get("/api/users/:user-id/activities", HealthTrackerAPI::getActivitiesByUserId)

get("/api/users/:user-id/intakes", HealthTrackerAPI::getIntakesByUserId)

post("/api/users", HealthTrackerAPI::addUser)

delete("/api/users/:user-id", HealthTrackerAPI::deleteUser)

delete("/api/users/:user-id/activities", HealthTrackerAPI::deleteActivityByUserId)

delete("/api/users/:user-id/intakes", HealthTrackerAPI::deleteIntakeByUserId)

patch("/api/users/:user-id", HealthTrackerAPI::updateUser)

//ACTIVITIES - API CRUD

get("/api/activities", HealthTrackerAPI::getAllActivities)

get("/api/activities/:activity-id", HealthTrackerAPI::getActivitiesByActivityId)

post("/api/activities", HealthTrackerAPI::addActivity)

delete("/api/activities/:activity-id", HealthTrackerAPI::deleteActivityByActivityId)

patch("/api/activities/:activity-id", HealthTrackerAPI::updateActivity)

//INTAKES - API CRUD

get("/api/intakes", HealthTrackerAPI::getAllIntakes)

get("/api/intakes/:intake-id", HealthTrackerAPI::getIntakesByIntakeId)

post("/api/intakes", HealthTrackerAPI::addIntake)

delete("/api/intakes/:intake-id", HealthTrackerAPI::deleteIntakeByIntakeId)

patch("/api/intakes/:intake-id", HealthTrackerAPI::updateIntake)



Swagger Documentation of the project: [Health Tracker](https://health-tracker-20100677.herokuapp.com/swagger-ui)

Heroku link : [Heroku](https://health-tracker-20100677.herokuapp.com/api/users)


