package com.example.library.user

import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserRepository(private val jdbcClient: JdbcClient) {

    fun save(userEntity: UserEntity) {
        val sql = """
            INSERT INTO app_user (username, password)
            VALUES (:username, :password)
            """.trimIndent()
        jdbcClient.sql(sql)
            .params(mapOf("username" to userEntity.username, "password" to userEntity.password))
            .update()
    }

    fun findByUsername(username: String): Optional<UserEntity> {
        val sql = """
            SELECT id, username, password
            FROM app_user
            WHERE username = :username
        """.trimIndent()
        return jdbcClient.sql(sql)
            .param("username", username)
            .query(UserEntity::class.java)
            .optional()
    }
}