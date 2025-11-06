package ru.itmo.highload_systems_lab_1.dtos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import ru.itmo.highload_systems_lab_1.utils.ChatConstraints

data class CreateChatDTO(
    @field:NotBlank(message = "Название чата обязательно")
    @field:Size(max = ChatConstraints.CHAT_NAME_MAX, message = "Название чата не должно превышать ${ChatConstraints.CHAT_NAME_MAX} символов")
    val chatName: String,

    @field:NotNull(message = "ID проекта обязателен")
    val projectId: Int
)

data class UpdateChatDTO(
    @field:NotBlank(message = "Название чата обязательно")
    @field:Size(max = ChatConstraints.CHAT_NAME_MAX, message = "Название чата не должно превышать ${ChatConstraints.CHAT_NAME_MAX} символов")
    val chatName: String
)

data class ReadChatDTO(
    val id: Int,
    val chatName: String,
    val projectId: Int
)
