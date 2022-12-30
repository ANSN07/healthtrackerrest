package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*
import ie.setu.utils.jsonToObject
import ie.setu.domain.Goal
import ie.setu.domain.repository.GoalDAO

object GoalController {

    private val userDao = UserDAO()
    private val goalDao = GoalDAO()

    @OpenApi(
        summary = "Get goals by user ID",
        operationId = "getGoalsByUserId",
        tags = ["Goal"],
        path = "/api/users/{user-id}/goals",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(Array<Goal>::class)]), OpenApiResponse("404")]
    )
    fun getGoalsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val goals = goalDao.findByUserId(ctx.pathParam("user-id").toInt())
            if (goals.isNotEmpty()) {
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(goals))
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
        summary = "Add Goal",
        operationId = "addGoal",
        tags = ["Goal"],
        path = "/api/users/{user-id}/goals",
        method = HttpMethod.POST,
        requestBody = OpenApiRequestBody([OpenApiContent(Goal::class)]),
        responses  = [OpenApiResponse("201")]
    )
    fun addGoal(ctx: Context) {
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val goal : Goal = jsonToObject(ctx.body())
        val goalId = goalDao.save(goal)
        if (goalId != null) {
            goal.id = goalId
            ctx.json(mapper.writeValueAsString(goal))
            ctx.status(201)
        }
    }

    @OpenApi(
        summary = "Delete goal by user ID",
        operationId = "deleteGoalsByUserId",
        tags = ["Goal"],
        path = "/api/users/{user-id}/goals",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204"), OpenApiResponse("404")]
    )
    fun deleteGoalsByUserId(ctx: Context){
        if (goalDao.deleteByUserId(ctx.pathParam("user-id").toInt()) !=0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Update goal by user ID",
        operationId = "updateGoalsByUserId",
        tags = ["Goal"],
        path = "/api/goals/{userId}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("userId", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204"), OpenApiResponse("404")]
    )
    fun updateGoalsByUserId(ctx: Context){
        val goal : Goal = jsonToObject(ctx.body())
        if (goalDao.updateById(
                id=ctx.pathParam("user-id").toInt(),
                goalDTO=goal) !=0)
            ctx.status(204)
        else
            ctx.status(404)
    }

}