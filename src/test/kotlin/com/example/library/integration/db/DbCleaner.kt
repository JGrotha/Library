package com.example.library.integration.db

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.simple.JdbcClient

class DbCleaner(private val jdbcClient: JdbcClient) {

    fun removeAllBooks() {
        val sql = "DELETE FROM books"
        jdbcClient.sql(sql).update()
    }

    fun removeAllUsers() {
        val sql = "DELETE FROM app_user"
        jdbcClient.sql(sql).update()
    }
}