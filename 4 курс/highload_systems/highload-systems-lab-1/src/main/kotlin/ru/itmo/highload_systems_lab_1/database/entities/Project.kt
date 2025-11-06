package ru.itmo.highload_systems_lab_1.database.entities

import jakarta.persistence.*
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import ru.itmo.highload_systems_lab_1.utils.ProjectConstraints
import java.math.BigDecimal

@Entity
@Table(name = "projects")
class Project(
    @field:NotNull(message = "Пользователь обязателен")
    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "creator_user_login", nullable = false, referencedColumnName = "login")
    var creatorUserLogin: User,

    @field:DecimalMin(value = "0.00", message = "Баланс токенов не может быть отрицательным")
    @field:Column(name = "token_balance", precision = ProjectConstraints.TOKEN_BALANCE_PRECISION,
        scale = ProjectConstraints.TOKEN_BALANCE_SCALE, nullable = false)
    var tokenBalance: BigDecimal = BigDecimal("0.00")
) {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.AUTO)
    @field:Column(name = "id", nullable = false)
    var id: Int = 0
}
