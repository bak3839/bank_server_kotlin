package org.example.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ReadPreference
import com.mongodb.client.MongoClients
import org.bson.UuidRepresentation
import org.example.common.exception.CustomException
import org.example.common.exception.ErrorCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory

enum class MongoTableCollector(
    val table: String
) {
    BANK("bank"),
}

@Configuration
@EnableMongoAuditing
class MongoConfig(
    @Value("\${database.mongo.url}") val url: String,
) {
    @Bean
    fun template(): HashMap<String, MongoTemplate> {
        val mapper = HashMap<String, MongoTemplate>(10)
        val settings = MongoClientSettings.builder()
            .uuidRepresentation(UuidRepresentation.STANDARD) // BSON(mongo 에서 다루는 형태로 컴퓨터가 알아보기 쉬운 형태) 표준 -> 기본 설정을 따른다
            .applyConnectionString(ConnectionString(url))
            .readPreference(ReadPreference.primary()) // 모든 읽기 작업이 primary 노드에서 수행 -> 몽고는 클러스터 형태로 사용 -> 하나의 primary 노드가 있고 나머지는 복제본을 가져 무너지지 않고 고가용성을 유지
            .build()

        for(c in MongoTableCollector.entries) {
            try {
                val client = MongoClients.create(settings)
                mapper[c.table] = MongoTemplate(
                    SimpleMongoClientDatabaseFactory(client, c.table)
                )
            }catch (e: Exception){
                throw CustomException(ErrorCode.FAILED_TO_CONNECT_MONGO)
            }
        }

        return mapper
    }
}