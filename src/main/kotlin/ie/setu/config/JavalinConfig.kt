package ie.setu.config

import ie.setu.controllers.*
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.ui.ReDocOptions
import io.javalin.plugin.openapi.ui.SwaggerOptions
import io.javalin.plugin.rendering.vue.VueComponent
import io.swagger.v3.oas.models.info.Info

class JavalinConfig {

    fun startJavalinService(): Javalin {
        val app = Javalin.create {
            it.registerPlugin(getConfiguredOpenApiPlugin())
            it.defaultContentType = "application/json"
            it.enableWebjars()
        }.apply {
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 - Not Found") }
        }.start(getRemoteAssignedPort())

        registerRoutes(app)
        return app
    }

    fun getConfiguredOpenApiPlugin() = OpenApiPlugin(
        OpenApiOptions(
            Info().apply {
                title("Health Tracker App")
                version("1.0")
                description("Health Tracker API")
            }
        ).apply {
            path("/swagger-docs") // endpoint for OpenAPI json
            swagger(SwaggerOptions("/swagger-ui")) // endpoint for swagger-ui
            reDoc(ReDocOptions("/redoc")) // endpoint for redoc
        }
    )

    private fun registerRoutes(app: Javalin) {
        app.routes {
            path("/api/users") {
                get(UserController::getAllUsers)
                post(UserController::addUser)
                path("{user-id}") {
                    get(UserController::getUserByUserId)
                    delete(UserController::deleteUser)
                    patch(UserController::updateUser)
                    //The overall path is: "/api/users/:user-id/activities"
                    path("activities"){
                        get(ActivityController::getActivitiesByUserId)
                        delete(ActivityController::deleteActivityByUserId)
                    }
                    path("intakes"){
                        get(IntakeController::getIntakesByUserId)
                        delete(IntakeController::deleteIntakeByUserId)
                    }
                    path("food-items"){
                        get(FoodItemController::getFoodItemsByUserId)
                    }
                    path("badges"){
                        get(BadgeController::getBadgesByUserId)
                    }
                    path("goals"){
                        post(GoalController::addGoal)
                        get(GoalController::getGoalsByUserId)
                        delete(GoalController::deleteGoalsByUserId)
                        patch(GoalController::updateGoalsByUserId)
                    }
                    path("weight"){
                        post(WeightController::addWeight)
                        get(WeightController::getWeightByUserId)
                    }
                }
                path("summary") {
//                    get(SummaryController::getUserSummary)
                }
                path("email/{email}") {
                    get(UserController::getUserByEmail)
                }
            }
            path("/api/activities") {
                get(ActivityController::getAllActivities)
                post(ActivityController::addActivity)
                path("{activity-id}") {
                    get(ActivityController::getActivitiesByActivityId)
                    delete(ActivityController::deleteActivityByActivityId)
                    patch(ActivityController::updateActivity)
                }
            }
            path("/api/intakes") {
                get(IntakeController::getAllIntakes)
                post(IntakeController::addIntake)
                path("{intake-id}") {
                    get(IntakeController::getIntakesByIntakeId)
                    delete(IntakeController::deleteIntakeByIntakeId)
                    patch(IntakeController::updateIntake)
                }
            }
            path("/api/weight/{id}") {
                patch(WeightController::updateWeightById)
                delete(WeightController::deleteWeightById)
            }
            path("/api/food") {
                post(FoodItemController::addFoodItem)
                path("{food-id}") {
                    delete(FoodItemController::deleteFoodItemsByFoodId)
                    patch(FoodItemController::updateFoodItemsByFoodId)
                }
            }

            // The @routeComponent that we added in layout.html earlier will be replaced
            // by the String inside of VueComponent. This means a call to / will load
            // the layout and display our <home-page> component.
            get("/", VueComponent("<home-page></home-page>"))
            get("/users", VueComponent("<user-overview></user-overview>"))
            get("/activities", VueComponent("<activity-overview></activity-overview>"))
            get("/users/{user-id}", VueComponent("<user-profile></user-profile>"))
            get("/users/{user-id}/activities", VueComponent("<user-activity-overview></user-activity-overview>"))
            get("/users/{user-id}/weight", VueComponent("<user-weight></user-weight>"))
            get("/users/{user-id}/goals", VueComponent("<user-goals></user-goals>"))
            get("/users/{user-id}/intakes", VueComponent("<user-intake></user-intake>"))
        }
    }

    private fun getRemoteAssignedPort(): Int {
        val herokuPort = System.getenv("PORT")
        return if (herokuPort != null) {
            Integer.parseInt(herokuPort)
        } else 7000
    }

}