package org.example.types.dto
import kotlinx.serialization.Serializable
import org.example.common.json.BigDecimalSerializer
import org.example.common.json.LocalDateTimeSerializer
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime

// API 에서 사용
@Serializable
data class History(
    val fromUlid: String,
    val fromUser: String,

    val toUlid: String,
    val toUser: String,

    @Serializable(with = BigDecimalSerializer::class)
    val value: BigDecimal,

    @Serializable(with = LocalDateTimeSerializer::class)
    val time: LocalDateTime,
) {
}