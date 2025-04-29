package org.example.types.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.common.json.BigDecimalSerializer
import org.example.common.json.LocalDateTimeSerializer
import java.math.BigDecimal
import java.time.LocalDateTime

@Serializable
data class TransactionMessage(
    @SerialName("fromUlid")
    val fromUlid: String,

    @SerialName("fromName")
    val fromName: String,

    @SerialName("fromAccountID")
    val fromAccountID: String,

    @SerialName("toUlid")
    val toUlid: String,

    @SerialName("toName")
    val toName: String,

    @SerialName("toAccountID")
    val toAccountID: String,

    @SerialName("value")
    @Serializable(with = BigDecimalSerializer::class)
    val value: BigDecimal,

    @SerialName("time")
    @Serializable(with = LocalDateTimeSerializer::class)
    val time: LocalDateTime,
)