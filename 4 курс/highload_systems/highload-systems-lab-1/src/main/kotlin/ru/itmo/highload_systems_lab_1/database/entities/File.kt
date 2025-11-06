package ru.itmo.highload_systems_lab_1.database.entities

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import ru.itmo.highload_systems_lab_1.utils.FileConstraints

@Entity
@Table(name = "files")
class File(
    @field:NotNull(message = "Проект файла обязателен")
    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "project_id", nullable = false, referencedColumnName = "id")
    var projectId: Project,

    @field:NotNull(message = "Загружающий пользователь обязателен")
    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "uploader_user_login", nullable = false, referencedColumnName = "login")
    var uploaderUserLogin: User,

    @field:NotBlank(message = "Имя файла обязательно")
    @field:Size(max = FileConstraints.FILE_NAME_MAX, message = "Имя файла не должно превышать ${FileConstraints.FILE_NAME_MAX} символов")
    @field:Column(name = "file_name", length = FileConstraints.FILE_NAME_MAX, nullable = false)
    var fileName: String,

    @field:NotBlank(message = "Ссылка на хранилище обязательна")
    @field:Size(max = FileConstraints.STORAGE_LINK_MAX, message = "Ссылка на хранилище не должна превышать ${FileConstraints.STORAGE_LINK_MAX} символов")
    @field:Column(name = "storage_link", length = FileConstraints.STORAGE_LINK_MAX, nullable = false)
    var storageLink: String
) {
    @field:Id
    @field:Column(name = "id", nullable = false)
    @field:GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0
}