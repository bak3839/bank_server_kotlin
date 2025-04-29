package org.example.domains.history.repository

import org.example.common.exception.CustomException
import org.example.common.exception.ErrorCode
import org.example.config.MongoTableCollector
import org.example.types.dto.History
import org.example.types.entity.TransactionHistoryDocument
import org.example.types.entity.User
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class HistoryMongoRepository(
    private val mongoTemplate: HashMap<String, MongoTemplate>,
    private val historyUserRepository: HistoryUserRepository,
    // username -> 불변 필드임으로 한번 조회 후 변경되지 않아 메모리에 올려두고 사용해 디비 접근 최소화
    private val userNameMapper: ConcurrentHashMap<String, String> = ConcurrentHashMap(),
) {
    fun findLatestTransactionHistory(ulid: String, limit: Int = 30): List<History> {
        val criteria = Criteria().orOperator(
            Criteria.where("fromUlid").`is`(ulid),
            Criteria.where("toUlid").`is`(ulid)
        )

        val query = Query(criteria)
            .with(Sort.by(Sort.Direction.DESC, "time"))
            .limit(limit)

        query.fields().exclude("_id")

        val result: List<TransactionHistoryDocument> =
            getTemplate(MongoTableCollector.BANK).find(query, TransactionHistoryDocument::class.java)

        return result.map {
            val fromUser = getUserName(it.fromUlid)
            val toUser = getUserName(it.toUlid)
            it.toHistory(fromUser, toUser)
        }
    }

    private fun getUserName(ulid: String): String {
        val value = userNameMapper[ulid] ?: ""

        if(value.isEmpty()) {
            val user = historyUserRepository.findByUlid(ulid)
            userNameMapper[ulid] = user.username
            return user.username
        } else {
            return value
        }
    }

    private fun getTemplate(c: MongoTableCollector): MongoTemplate {
        val template = mongoTemplate[c.table] ?: throw CustomException(ErrorCode.FAILED_TO_FIND_MONGO_TEMPLATE)
        return template
    }
}