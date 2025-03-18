package com.example.library.integration

import com.example.library.book.BookRepository
import com.example.library.integration.db.DbCleaner
import com.example.library.user.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.lifecycle.Startables

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = [AbstractIntegrationTest.Companion.Initializer::class])
abstract class AbstractIntegrationTest {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    protected lateinit var bookRepository: BookRepository

    @Autowired
    protected lateinit var userRepository: UserRepository

    @Autowired
    protected lateinit var jdbcClient: JdbcClient

    @BeforeEach
    protected fun setup() {
        cleanAllTables()
    }

    private fun cleanAllTables() {
        val dbCleaner = DbCleaner(jdbcClient)
        dbCleaner.removeAllBooks()
        dbCleaner.removeAllUsers()
    }

    companion object {

        @Container
        private val postgres = PostgreSQLContainer<Nothing>("postgres:17")
            .apply {
                withDatabaseName("test")
                withUsername("test")
                withPassword("test")
            }

        internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

            init {
                Startables.deepStart(postgres).join()
            }

            override fun initialize(applicationContext: ConfigurableApplicationContext) {
                TestPropertyValues.of(
                    "spring.datasource.url=${postgres.jdbcUrl}",
                    "spring.datasource.username=${postgres.username}",
                    "spring.datasource.password=${postgres.password}",
                ).applyTo(applicationContext.environment)
            }
        }
    }
}
