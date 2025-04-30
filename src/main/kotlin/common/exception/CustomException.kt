package org.example.common.exception

class CustomException(
    private val codeInterface: CodeInterface,
    private val additionalMessage: String? = null
): RuntimeException(
    if(additionalMessage == null) {
        codeInterface.message
    } else {
        "${codeInterface.message} - ${additionalMessage}"
    }
) {
    fun getCodeInterface(): CodeInterface {
        var codeInterface = codeInterface

        if(additionalMessage != null) {
            codeInterface.message += additionalMessage.toString()
        }

        return codeInterface
    }
}