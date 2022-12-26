package ie.setu.config

import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.name

class DbConfig{

    fun getDbConnection() :Database{

        val logger = KotlinLogging.logger {}
        logger.info{"Starting DB Connection..."}

        val PGUSER = "svqxvouy"
        val PGPASSWORD = "NbZjYLzKLcb-McbW7Bt9d-HfXMB9PYx4"
        val PGHOST = "mel.db.elephantsql.com"
        val PGPORT = "5432"
        val PGDATABASE = "svqxvouy"

        //url format should be jdbc:postgresql://host:port/database
        val url = "jdbc:postgresql://$PGHOST:$PGPORT/$PGDATABASE"

        val dbConfig = Database.connect(url,
            driver="org.postgresql.Driver",
            user = PGUSER,
            password = PGPASSWORD
        )

        logger.info{"db url - connection: " + dbConfig.url}

        return dbConfig
    }

}