package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*
import ie.setu.domain.Activity
import ie.setu.domain.repository.ActivityDAO
import ie.setu.utils.jsonToObject
import ie.setu.domain.Badge
import ie.setu.domain.repository.BadgeDAO
import org.joda.time.LocalDateTime

object ActivityController {

    private val activityDAO = ActivityDAO()
    private val userDao = UserDAO()
    private val badgeDao = BadgeDAO()

    @OpenApi(
        summary = "Get all activities",
        operationId = "getAllActivities",
        tags = ["Activity"],
        path = "/api/activities",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<Activity>::class)]),
            OpenApiResponse("404")]
    )
    fun getAllActivities(ctx: Context) {
        val activities = activityDAO.getAll()
        if (activities.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        ctx.json(mapper.writeValueAsString( activities ))
    }

    @OpenApi(
        summary = "Get activities by user ID",
        operationId = "getActivitiesByUserId",
        tags = ["Activity"],
        path = "/api/users/{user-id}/activities",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(Array<Activity>::class)]), OpenApiResponse("404")]
    )
    fun getActivitiesByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val activities = activityDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (activities.isNotEmpty()) {
                var totalDistance = 0.0
                for (activity in activities) {
                    totalDistance += activity.distance
                }

                if (totalDistance >= 20.0 && totalDistance < 40.0) {
                    val badge : Badge = jsonToObject("""{"name": "20 kms", "level": 1, "date": "${LocalDateTime.now()}", "userId": ${ctx.pathParam("user-id")}}""")
                    badgeDao.save(badge)
                } else if (totalDistance >= 40.0 && totalDistance < 80.0) {
                    val badge : Badge = jsonToObject("""{"name": "40 kms", "level": 2, "date": "${LocalDateTime.now()}", "userId": ${ctx.pathParam("user-id")}}""")
                    badgeDao.save(badge)
                } else if (totalDistance >= 80.0) {
                    val badge : Badge = jsonToObject("""{"name": "80 kms", "level": 3, "date": "${LocalDateTime.now()}", "userId": ${ctx.pathParam("user-id")}}""")
                    badgeDao.save(badge)
                }

                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(activities))
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
        summary = "Add Activity",
        operationId = "addActivity",
        tags = ["Activity"],
        path = "/api/activities",
        method = HttpMethod.POST,
        requestBody = OpenApiRequestBody([OpenApiContent(Activity::class)]),
        responses  = [OpenApiResponse("201")]
    )
    fun addActivity(ctx: Context) {
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val activity : Activity = jsonToObject(ctx.body())
        val activityId = activityDAO.save(activity)
        if (activityId != null) {
            activity.id = activityId
            ctx.json(mapper.writeValueAsString(activity))
            ctx.status(201)
        }
    }

    @OpenApi(
        summary = "Get activities by activity ID",
        operationId = "getActivitiesByActivityId",
        tags = ["Activity"],
        path = "/api/activities/{activity-id}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("activity-id", Int::class, "The activity ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(Activity::class)]), OpenApiResponse("404")]
    )
    fun getActivitiesByActivityId(ctx: Context) {
        val activity = activityDAO.findByActivityId((ctx.pathParam("activity-id").toInt()))
        if (activity != null) {
            val mapper = jacksonObjectMapper()
                .registerModule(JodaModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            ctx.json(mapper.writeValueAsString(activity))
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Delete activity by activity ID",
        operationId = "deleteActivityByActivityId",
        tags = ["Activity"],
        path = "/api/activities/{activity-id}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("activity-id", Int::class, "The activity ID")],
        responses  = [OpenApiResponse("204"), OpenApiResponse("404")]
    )
    fun deleteActivityByActivityId(ctx: Context){
        if (activityDAO.deleteByActivityId(ctx.pathParam("activity-id").toInt()) !=0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Delete activity by user ID",
        operationId = "deleteActivityByUserId",
        tags = ["Activity"],
        path = "/api/users/{user-id}/activities",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204"), OpenApiResponse("404")]
    )
    fun deleteActivityByUserId(ctx: Context){
        if (activityDAO.deleteByUserId(ctx.pathParam("user-id").toInt()) !=0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Update activity by ID",
        operationId = "updateActivity",
        tags = ["Activity"],
        path = "/api/activities/{activity-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("activity-id", Int::class, "The activity ID")],
        responses  = [OpenApiResponse("204"), OpenApiResponse("404")]
    )
    fun updateActivity(ctx: Context){
        val activity : Activity = jsonToObject(ctx.body())
        if (activityDAO.updateByActivityId(
            activityId=ctx.pathParam("activity-id").toInt(),
            activityDTO=activity) !=0)
            ctx.status(204)
        else
            ctx.status(404)
    }

}
