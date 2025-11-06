package ru.itmo.highload_systems_lab_1.exceptions

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.transaction.TransactionSystemException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

@RestControllerAdvice
class RestExceptionHandler {

    private fun createErrorMessage(
        request: HttpServletRequest,
        httpStatus: HttpStatus,
        errorsList: List<Map<String, Any?>> = emptyList()
    ): Map<String, Any?> {
        val message = mutableMapOf<String, Any?>(
            "timestamp" to Instant.now().toString(),
            "status" to httpStatus.value(),
            "error" to httpStatus.reasonPhrase,
            "message" to "Validation failed",
            "path" to request.requestURI,
        )

        if (errorsList.isNotEmpty()) { message["errors"] = errorsList }

        return message
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): Map<String, Any?> {
        val fieldErrors: List<Map<String, Any?>> = ex.bindingResult.fieldErrors.map { fe ->
            mapOf(
                "field" to fe.field,
                "rejectedValue" to fe.rejectedValue,
                "message" to (fe.defaultMessage ?: "Invalid value")
            )
        }

        return createErrorMessage(request, HttpStatus.BAD_REQUEST, fieldErrors)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleConstraintViolation(
        ex: ConstraintViolationException,
        request: HttpServletRequest
    ): Map<String, Any?> {
        val violations: List<Map<String, Any?>> = ex.constraintViolations.map { cv ->
            mapOf(
                "path" to cv.propertyPath.toString(),
                "invalidValue" to cv.invalidValue,
                "message" to cv.message
            )
        }

        return createErrorMessage(request, HttpStatus.BAD_REQUEST, violations)
    }

    @ExceptionHandler(TransactionSystemException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleTransactionSystem(
        ex: TransactionSystemException,
        request: HttpServletRequest
    ): Map<String, Any?> {
        var cause: Throwable? = ex
        while (cause != null && cause !is ConstraintViolationException) {
            cause = cause.cause
        }
        if (cause is ConstraintViolationException) {
            return handleConstraintViolation(cause, request)
        }

        return createErrorMessage(request, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}