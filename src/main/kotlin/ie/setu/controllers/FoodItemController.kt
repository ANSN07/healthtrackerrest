package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ie.setu.domain.Badge
import ie.setu.domain.FoodItem
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*
import ie.setu.utils.jsonToObject
import ie.setu.domain.repository.FoodItemDAO

object FoodItemController {

    private val userDao = UserDAO()
    private val foodItemDao = FoodItemDAO()



    @OpenApi(
        summary = "Get food items by user ID",
        operationId = "getFoodItemsByUserId",
        tags = ["FoodItem"],
        path = "/api/users/{user-id}/food-items",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(Array<FoodItem>::class)]), OpenApiResponse("404")]
    )
    fun getFoodItemsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val foodItem = foodItemDao.findByUserId(ctx.pathParam("user-id").toInt())
            if (foodItem.isNotEmpty()) {
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(foodItem))
                ctx.status(200)
            }
            else {
                ctx.status(404)
            }
        }
        else{
            ctx.status(404)
        }

    }

    @OpenApi(
        summary = "Add FoodItem",
        operationId = "addFoodItem",
        tags = ["FoodItem"],
        path = "/api/food",
        method = HttpMethod.POST,
        requestBody = OpenApiRequestBody([OpenApiContent(FoodItem::class)]),
        responses  = [OpenApiResponse("201")]
    )
    fun addFoodItem(ctx: Context) {
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val foodItem : FoodItem = jsonToObject(ctx.body())
        val foodItemId = foodItemDao.save(foodItem)
        if (foodItemId != null) {
            foodItem.foodId = foodItemId
            ctx.json(mapper.writeValueAsString(foodItem))
            ctx.status(201)
        }
    }

    @OpenApi(
        summary = "Delete foodItem by food ID",
        operationId = "deleteFoodItemsByFoodId",
        tags = ["FoodItem"],
        path = "/api/food/{food-id}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("food-id", Int::class, "The food ID")],
        responses  = [OpenApiResponse("204"), OpenApiResponse("404")]
    )
    fun deleteFoodItemsByFoodId(ctx: Context){
        if (foodItemDao.deleteByFoodId(ctx.pathParam("food-id").toInt()) !=0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Update foodItem by food ID",
        operationId = "updateFoodItemsByFoodId",
        tags = ["FoodItem"],
        path = "/api/food/{food-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("food-id", Int::class, "The food ID")],
        responses  = [OpenApiResponse("204"), OpenApiResponse("404")]
    )
    fun updateFoodItemsByFoodId(ctx: Context){
        val foodItem : FoodItem = jsonToObject(ctx.body())
        if (foodItemDao.updateByFoodId(
                foodId=ctx.pathParam("food-id").toInt(),
                foodDTO=foodItem) !=0)
            ctx.status(204)
        else
            ctx.status(404)
    }

}