package ru.itmo.highload_systems_lab_1.services

import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.highload_systems_lab_1.database.repositories.ProjectRepository
import ru.itmo.highload_systems_lab_1.database.repositories.FileRepository
import ru.itmo.highload_systems_lab_1.database.repositories.ChatRepository
import ru.itmo.highload_systems_lab_1.database.repositories.UserProjectRepository
import ru.itmo.highload_systems_lab_1.database.repositories.UserChatSubscriptionRepository
import ru.itmo.highload_systems_lab_1.dtos.CreateProjectDTO
import ru.itmo.highload_systems_lab_1.dtos.ReadProjectDTO
import ru.itmo.highload_systems_lab_1.dtos.UpdateProjectDTO
import ru.itmo.highload_systems_lab_1.mappers.ProjectMapper
import ru.itmo.highload_systems_lab_1.exceptions.ProjectNotFoundException
import ru.itmo.highload_systems_lab_1.services.util.pageRequest

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
    private val projectMapper: ProjectMapper,
    private val fileRepository: FileRepository,
    private val chatRepository: ChatRepository,
    private val userProjectRepository: UserProjectRepository,
    private val userChatSubscriptionRepository: UserChatSubscriptionRepository
) {

    fun createProject(dto: CreateProjectDTO): ReadProjectDTO {
        val entity = projectMapper.toEntity(dto)
        val saved = projectRepository.save(entity)
        return projectMapper.toReadDTO(saved)
    }

    fun getProject(id: Int): ReadProjectDTO {
        val project = projectRepository.findById(id).orElseThrow { ProjectNotFoundException(id) }
        return projectMapper.toReadDTO(project)
    }

    fun updateProject(id: Int, dto: UpdateProjectDTO): ReadProjectDTO {
        val project = projectRepository.findById(id).orElseThrow { ProjectNotFoundException(id) }
        projectMapper.toEntity(dto, project)
        val saved = projectRepository.save(project)
        return projectMapper.toReadDTO(saved)
    }

    @Transactional
    fun deleteProject(id: Int) {
        if (!projectRepository.existsById(id)) {
            throw ProjectNotFoundException(id)
        }
        val chats = chatRepository.findAll().filter { it.projectId.id == id }
        chats.forEach { chat ->
            userChatSubscriptionRepository.deleteAll(
                userChatSubscriptionRepository.findAll().filter { it.id.chatId == chat.id }
            )
        }
        
        chatRepository.deleteAll(chats)

        fileRepository.deleteAll(
            fileRepository.findAll().filter { it.projectId.id == id }
        )

        userProjectRepository.deleteAll(
            userProjectRepository.findAll().filter { it.id.projectId == id }
        )

        projectRepository.deleteById(id)
    }

    fun findAll(page: Int, size: Int): Page<ReadProjectDTO> {
        val pageable = pageRequest(page, size)
        return projectRepository.findAll(pageable).map { projectMapper.toReadDTO(it) }
    }

    fun findAllInfinite(cursor: Int?, size: Int): List<ReadProjectDTO> {
        val pageable = pageRequest(0, size)
        val projects = if (cursor != null) {
            projectRepository.findByIdGreaterThanOrderById(cursor, pageable)
        } else {
            projectRepository.findAll(pageable).content
        }
        return projects.map { projectMapper.toReadDTO(it) }
    }
}
