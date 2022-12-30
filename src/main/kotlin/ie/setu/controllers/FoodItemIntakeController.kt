package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ie.setu.domain.FoodItemIntake
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*
import ie.setu.utils.jsonToObject
import ie.setu.domain.repository.FoodItemIntakeDAO

object FoodItemIntakeController {

    private val userDao = UserDAO()
    private val foodItemIntakeDao = FoodItemIntakeDAO()

    @OpenApi(
        summary = "Add FoodItem Intake",
        operationId = "addFoodItemIntake",
        tags = ["FoodItemIntake"],
        path = "/api/food-intake",
        method = HttpMethod.POST,
        requestBody = OpenApiRequestBody([OpenApiContent(FoodItemIntake::class)]),
        responses  = [OpenApiResponse("201")]
    )
    fun addFoodItemIntake(ctx: Context) {
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val foodItemIntake : FoodItemIntake = jsonToObject(ctx.body())
        val foodItemIntakeId = foodItemIntakeDao.save(foodItemIntake)
        if (foodItemIntakeId != null) {
            foodItemIntake.foodIntakeId = foodItemIntakeId
            ctx.json(mapper.writeValueAsString(foodItemIntake))
            ctx.status(201)
        }
    }

    @OpenApi(
        summary = "Delete foodItem Intake by food intake ID",
        operationId = "deleteFoodItemIntakeByFoodIntakeId",
        tags = ["FoodItemIntake"],
        path = "/api/food-intake/{foodIntake-id}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("foodIntake-id", Int::class, "The food Intake ID")],
        responses  = [OpenApiResponse("204"), OpenApiResponse("404")]
    )
    fun deleteFoodItemIntakeByFoodIntakeId(ctx: Context){
        if (foodItemIntakeDao.deleteByFoodIntakeId(ctx.pathParam("foodIntake-id").toInt()) !=0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Update foodItem Intake by food ID",
        operationId = "updateFoodItemIntakeByFoodIntakeId",
        tags = ["FoodItemIntake"],
        path = "/api/food-intake/{foodIntake-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("foodIntake-id", Int::class, "The food Intake ID")],
        responses  = [OpenApiResponse("204"), OpenApiResponse("404")]
    )
    fun updateFoodItemIntakeByFoodIntakeId(ctx: Context){
        val foodItemIntake : FoodItemIntake = jsonToObject(ctx.body())
        if (foodItemIntakeDao.updateByFoodIntakeId(
                foodIntakeId=ctx.pathParam("foodIntake-id").toInt(),
                foodItemIntakeDTO=foodItemIntake) !=0)
            ctx.status(204)
        else
            ctx.status(404)
    }

}