package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*
import ie.setu.utils.jsonToObject
import ie.setu.domain.Weight
import ie.setu.domain.repository.WeightDAO

object WeightController {

    private val userDao = UserDAO()
    private val weightDao = WeightDAO()

    @OpenApi(
        summary = "Get weight by user ID",
        operationId = "getWeightByUserId",
        tags = ["Weight"],
        path = "/api/users/{user-id}/weight",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(Array<Weight>::class)]), OpenApiResponse("404")]
    )
    fun getWeightByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val weight = weightDao.findByUserId(ctx.pathParam("user-id").toInt())
            if (weight.isNotEmpty()) {
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(weight))
                ctx.status(200)
            }
//            else {
//                ctx.status(404)
//            }
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Add Weight",
        operationId = "addWeight",
        tags = ["Weight"],
        path = "/api/users/{user-id}/weight",
        method = HttpMethod.POST,
        requestBody = OpenApiRequestBody([OpenApiContent(Weight::class)]),
        responses  = [OpenApiResponse("201")]
    )
    fun addWeight(ctx: Context) {
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val weight : Weight = jsonToObject(ctx.body())
        val weightId = weightDao.save(weight)
        if (weightId != null) {
            weight.id = weightId
            ctx.json(mapper.writeValueAsString(weight))
            ctx.status(201)
        }
    }

    @OpenApi(
        summary = "Delete weight by weight ID",
        operationId = "deleteWeightById",
        tags = ["Weight"],
        path = "/api/users/{id}/weight",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("id", Int::class, "The weight ID")],
        responses  = [OpenApiResponse("204"), OpenApiResponse("404")]
    )
    fun deleteWeightById(ctx: Context){
        if (weightDao.deleteById(ctx.pathParam("id").toInt()) !=0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Update weight by ID",
        operationId = "updateWeightById",
        tags = ["Weight"],
        path = "/api/weight/{id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("id", Int::class, "The weight ID")],
        responses  = [OpenApiResponse("204"), OpenApiResponse("404")]
    )
    fun updateWeightById(ctx: Context){
        val weight : Weight = jsonToObject(ctx.body())
        if (weightDao.updateById(
                weightId=ctx.pathParam("id").toInt(),
                weightDTO=weight) !=0)
            ctx.status(204)
        else
            ctx.status(404)
    }

}