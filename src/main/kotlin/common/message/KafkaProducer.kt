package org.example.common.message

import org.example.common.exception.CustomException
import org.example.common.exception.ErrorCode
import org.example.common.logging.Logging
import org.slf4j.Logger
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.time.LocalDateTime

enum class Topics(
    val topic: String,
) {
    Transactions("transactions"),
}


@Component
class KafkaProducer(
    private val template: KafkaTemplate<String, Any>,
    private val log: Logger = Logging.getLogger(KafkaProducer::class.java)
) {
    // 토픽은 특정 메세지를 수신할 수 있는 기준값
    fun sendMessage(topic: String, message: String) {
        val future = template.send(topic, message)

        future.whenComplete { result, ex ->
            if(ex == null){
                log.info("메시지 발행 성공 - topic: $topic time: ${LocalDateTime.now()}")
            } else {
                log.error("메시지 전송 실패 - $ex.message")
                throw CustomException(ErrorCode.FAILED_TO_SEND_MESSAGE, topic)
            }
        }
    }
}