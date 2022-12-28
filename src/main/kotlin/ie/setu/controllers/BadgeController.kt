package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*
import ie.setu.domain.Badge
import ie.setu.domain.repository.BadgeDAO

object BadgeController {

    private val userDao = UserDAO()
    private val badgeDao = BadgeDAO()

    @OpenApi(
        summary = "Get badges by user ID",
        operationId = "getBadgesByUserId",
        tags = ["Badge"],
        path = "/api/users/{user-id}/badges",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(Array<Badge>::class)]), OpenApiResponse("404")]
    )
    fun getBadgesByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val badges = badgeDao.findByUserId(ctx.pathParam("user-id").toInt())
            if (badges.isNotEmpty()) {
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(badges))
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

}
