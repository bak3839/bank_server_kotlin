package org.example.config

import com.zaxxer.hikari.util.DriverDataSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
class MySQLConfig(
    @Value("\${database.mysql.url}") val url: String,
    @Value("\${database.mysql.username}") val username: String,
    @Value("\${database.mysql.password}") val password: String,
    @Value("\${database.mysql.driver-class-name}") val driver: String,
) {
    @Bean
    fun dataSource() : DataSource {
        val dataSource = DriverManagerDataSource(url, username, password)
        dataSource.setDriverClassName(driver)
        return dataSource
    }

    @Bean
    fun transactionManager(dataSource: DataSource): PlatformTransactionManager {
        return DataSourceTransactionManager(dataSource)
    }
}