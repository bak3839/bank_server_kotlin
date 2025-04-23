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

}