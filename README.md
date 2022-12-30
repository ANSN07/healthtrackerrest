#### healthtrackerrest

# Health Tracker App

This application basically collects data from the user and tracks the health status of user by summarizing data.
Basic details of user are collected like name and email, each user will have a unique ID.
Each User can add several activities they had done done during the day where each activity will have a short description, unique ID, calories burned, duration of the activity, start time and date and finally the user id.
User can add the intakes they had which includes the name of the food, amount of calories, number of units consumed, unit measure , user id and a unique id.
User can add their goals for weight, calories and levels of badges.
Badges are obtained based on the user activities where milestones are set to 20, 40, 80, 100 kms.
Weight endpoint tracks user's weight over time. This is illustrated graphically.



## Features

| Feature                                | Summary                                                                                                                                                                                                                                                     |
|----------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Users and Activities | Interactive UI showing users list and activities performed. |
| Food tracking | Track every intake, when you ate it and its calories. |
| Weight tracking | Graphical representation of weight over time. |
| Goal setting | Set goals and keep track of your health |
| Achievements | Badges obtained over completion of levels |



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

//FOOD ITEMS 

get("/api/users/:user-id/food-items", HealthTrackerAPI::getFoodItemsByUserId)

post("/api/food/:{food-id}", HealthTrackerAPI::addFoodItem) 

delete("/api/food/:{food-id}", HealthTrackerAPI::deleteFoodItemsByFoodId) 

patch("/api/food/:{food-id}", HealthTrackerAPI::addFoodItem) 

//BADGES

get("/api/users/:user-id/badges", HealthTrackerAPI::getBadgesByUserId)

//WEIGHT

get("/api/users/:user-id/weight", HealthTrackerAPI::getWeightByUserId)

post("/api/users/:user-id/weight", HealthTrackerAPI::addWeight)

patch("/api/weight/:id", HealthTrackerAPI::updateWeightById)

delete("/api/weight/:id", HealthTrackerAPI::deleteWeightById)

//GOALS

get("/api/users/:user-id/goals", HealthTrackerAPI::getGoalsByUserId)

post("/api/users/:user-id/goals", HealthTrackerAPI::addGoal)

delete("/api/users/:user-id/goals", HealthTrackerAPI::deleteGoalsByUserId)

patch("/api/users/:user-id/goals", HealthTrackerAPI::updateGoalsByUserId)




Swagger Documentation of the project: [Health Tracker](https://health-tracker-20100677.herokuapp.com/swagger-ui)

## Use the app
Railway link : [Railway](https://healthtrackerrest-production-9807.up.railway.app/)


