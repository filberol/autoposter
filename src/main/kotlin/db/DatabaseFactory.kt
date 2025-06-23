package ru.social.ai.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder
import org.slf4j.LoggerFactory

object DatabaseFactory {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun init() {
        Database.connect("jdbc:sqlite:data.db", driver = "org.sqlite.JDBC")

        val classLoader = Thread.currentThread().contextClassLoader
        val reflections = Reflections(
            ConfigurationBuilder()
                .addClassLoaders(classLoader)
                .addUrls(classLoader.getResources("").toList())
                .setScanners(Scanners.TypesAnnotated)
        )

        val annotatedClasses = reflections
            .getTypesAnnotatedWith(Schema::class.java)

        val tables = annotatedClasses.mapNotNull { cls ->
            try {
                val obj = cls.kotlin.objectInstance
                if (obj is Table) obj else null
            } catch (e: Exception) {
                null
            }
        }

        logger.debug("Discovered tables: {}", tables.map { it.tableName })

        transaction {
            SchemaUtils.create(*tables.toTypedArray())
        }
    }
}
