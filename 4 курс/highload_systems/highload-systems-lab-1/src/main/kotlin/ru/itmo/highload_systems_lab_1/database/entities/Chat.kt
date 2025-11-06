package ru.itmo.highload_systems_lab_1.database.entities

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import ru.itmo.highload_systems_lab_1.utils.ChatConstraints

@Entity
@Table(name = "chats")
class Chat(
    @field:NotBlank(message = "Название чата обязательно")
    @field:Size(max = ChatConstraints.CHAT_NAME_MAX, message = "Название чата не должно превышать ${ChatConstraints.CHAT_NAME_MAX} символов")
    @field:Column(name = "chat_name", length = ChatConstraints.CHAT_NAME_MAX, nullable = false)
    var chatName: String,

    @field:NotNull(message = "Проект чата обязателен")
    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "project_id", nullable = false, referencedColumnName = "id")
    var projectId: Project
) {
    @field:Id
    @field:Column(name = "id", nullable = false)
    @field:GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0
}