package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*
import ie.setu.domain.Intake
import ie.setu.domain.repository.IntakeDAO
import ie.setu.utils.jsonToObject

object IntakeController {

    private val intakeDAO = IntakeDAO()
    private val userDao = UserDAO()

    @OpenApi(
        summary = "Get all intakes",
        operationId = "getAllIntakes",
        tags = ["Intake"],
        path = "/api/intakes",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<Intake>::class)]), OpenApiResponse("404")]
    )
    fun getAllIntakes(ctx: Context) {
        val intakes = intakeDAO.getAll()
        if (intakes.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        ctx.json(mapper.writeValueAsString( intakes ))
    }

    @OpenApi(
        summary = "Get intakes by user ID",
        operationId = "getIntakesByUserId",
        tags = ["Intake"],
        path = "/api/users/{user-id}/intakes",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(Array<Intake>::class)]), OpenApiResponse("404")]
    )
    fun getIntakesByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val intakes = intakeDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (intakes.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(intakes))
                ctx.status(200)
            }
        } else {
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Add Intake",
        operationId = "addIntake",
        tags = ["Intake"],
        path = "/api/intakes",
        method = HttpMethod.POST,
        requestBody = OpenApiRequestBody([OpenApiContent(Intake::class)]),
        responses  = [OpenApiResponse("201")]
    )
    fun addIntake(ctx: Context) {
        val intake : Intake = jsonToObject(ctx.body())
        val intakeId = intakeDAO.save(intake)
        if (intakeId != null) {
            intake.id = intakeId
            ctx.json(intake)
            ctx.status(201)
        }
    }

    @OpenApi(
        summary = "Get intakes by intake ID",
        operationId = "getIntakesByIntakeId",
        tags = ["Intake"],
        path = "/api/intakes/{intake-id}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("intake-id", Int::class, "The intake ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(Intake::class)]), OpenApiResponse("404")]
    )
    fun getIntakesByIntakeId(ctx: Context) {
        val intake = intakeDAO.findByIntakeId((ctx.pathParam("intake-id").toInt()))
        if (intake != null) {
            val mapper = jacksonObjectMapper()
                .registerModule(JodaModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            ctx.json(mapper.writeValueAsString(intake))
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Delete intake by intake ID",
        operationId = "deleteIntakeByIntakeId",
        tags = ["Intake"],
        path = "/api/intakes/{intake-id}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("intake-id", Int::class, "The intake ID")],
        responses  = [OpenApiResponse("204"), OpenApiResponse("404")]
    )
    fun deleteIntakeByIntakeId(ctx: Context){
        if (intakeDAO.deleteByIntakeId(ctx.pathParam("intake-id").toInt()) !=0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Delete intake by user ID",
        operationId = "deleteIntakeByUserId",
        tags = ["Intake"],
        path = "/api/users/{user-id}/intakes",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204"), OpenApiResponse("404")]
    )
    fun deleteIntakeByUserId(ctx: Context){
        if (intakeDAO.deleteByUserId(ctx.pathParam("user-id").toInt()) !=0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Update intake by ID",
        operationId = "updateIntake",
        tags = ["Intake"],
        path = "/api/intakes/{intake-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("intake-id", Int::class, "The intake ID")],
        responses  = [OpenApiResponse("204"), OpenApiResponse("404")]
    )
    fun updateIntake(ctx: Context){
        val intake : Intake = jsonToObject(ctx.body())
        if (intakeDAO.updateByIntakeId(
                intakeId=ctx.pathParam("intake-id").toInt(),
                intakeDTO=intake) !=0)
            ctx.status(204)
        else
            ctx.status(404)
    }

}
