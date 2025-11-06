package ru.itmo.highload_systems_lab_1.database.entities

import jakarta.persistence.*
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import ru.itmo.highload_systems_lab_1.utils.AccessKeyConstraints
import java.time.LocalDateTime

@Entity
@Table(name = "access_keys")
class AccessKey(
    @field:Id
    @field:Size(min = AccessKeyConstraints.KEY_LENGTH, max = AccessKeyConstraints.KEY_LENGTH,
        message = "Длина ключа должна быть ровно ${AccessKeyConstraints.KEY_LENGTH} символов")
    @field:Column(name = "key", length = AccessKeyConstraints.KEY_LENGTH, nullable = false)
    var key: String,

    @field:NotNull(message = "Пользователь обязателен")
    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "user_login", nullable = false, referencedColumnName = "login")
    var userLogin: User,

    @field:NotNull(message = "Дата окончания срока действия обязательна")
    @field:Column(name = "lifespan_end_date", nullable = false)
    var lifespanEndDate: LocalDateTime,

    @field:Column(name = "usages_limit", nullable = true)
    var usagesLimit: Int? = null,

    @field:Min(value = 0, message = "Количество использований не может быть отрицательным")
    @field:Column(name = "usages_count", nullable = false)
    var usagesCount: Int = 0
)