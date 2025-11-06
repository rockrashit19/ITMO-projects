package ru.itmo.highload_systems_lab_1.dtos

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import ru.itmo.highload_systems_lab_1.utils.ProjectConstraints
import java.math.BigDecimal

data class CreateProjectDTO(
    @field:NotNull(message = "Логин создателя проекта обязателен")
    val creatorUserLogin: String,

    val tokenBalance: BigDecimal = BigDecimal("0.00")
)

data class UpdateProjectDTO(
    @field:DecimalMin(value = "0.00", message = "Баланс токенов не может быть отрицательным")
    val tokenBalance: BigDecimal
)

data class ReadProjectDTO(
    val id: Int,
    val creatorUserLogin: String,
    val tokenBalance: BigDecimal
)
