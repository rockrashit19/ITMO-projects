package ru.itmo.highload_systems_lab_1.dtos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import ru.itmo.highload_systems_lab_1.utils.FileConstraints

data class CreateFileDTO(
    @field:NotNull(message = "ID проекта обязателен")
    val projectId: Int,

    @field:NotNull(message = "Логин загружающего пользователя обязателен")
    val uploaderUserLogin: String,

    @field:NotBlank(message = "Имя файла обязательно")
    @field:Size(max = FileConstraints.FILE_NAME_MAX, message = "Имя файла не должно превышать ${FileConstraints.FILE_NAME_MAX} символов")
    val fileName: String,

    @field:Size(max = FileConstraints.STORAGE_LINK_MAX, message = "Ссылка на хранилище не должна превышать ${FileConstraints.STORAGE_LINK_MAX} символов")
    val storageLink: String?
)

data class UpdateFileDTO(
    @field:Size(max = FileConstraints.FILE_NAME_MAX, message = "Имя файла не должно превышать ${FileConstraints.FILE_NAME_MAX} символов")
    val fileName: String?,

    @field:Size(max = FileConstraints.STORAGE_LINK_MAX, message = "Ссылка на хранилище не должна превышать ${FileConstraints.STORAGE_LINK_MAX} символов")
    val storageLink: String?
)

data class ReadFileDTO(
    val id: Int,
    val projectId: Int,
    val uploaderUserLogin: String,
    val fileName: String,
    val storageLink: String
)
