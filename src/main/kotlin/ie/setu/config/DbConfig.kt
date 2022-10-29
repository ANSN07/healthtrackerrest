package ie.setu.config

import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.name

class DbConfig{

    private val logger = KotlinLogging.logger {}

    //NOTE: you need the ?sslmode=require otherwise you get an error complaining about the ssl certificate
    fun getDbConnection() :Database{
        logger.info{"Starting DB Connection..."}
        val dbConfig = Database.connect(
            "jdbc:postgresql://ec2-3-220-207-90.compute-1.amazonaws.com:5432/dt3u74u0blv4o?sslmode=require",
            driver = "org.postgresql.Driver",
            user = "dqztmqsbcydtdi",
            password = "cb90087341cac7f97f504719455ca40a23cac9170dcf6007a11fb7b79665e488")

        logger.info{"DbConfig name = " + dbConfig.name}
        logger.info{"DbConfig url = " + dbConfig.url}

        return dbConfig
    }

}