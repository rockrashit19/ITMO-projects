package ru.itmo.highload_systems_lab_1.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.highload_systems_lab_1.database.repositories.UserRepository
import ru.itmo.highload_systems_lab_1.database.repositories.AccessKeyRepository
import ru.itmo.highload_systems_lab_1.database.repositories.UserProjectRepository
import ru.itmo.highload_systems_lab_1.database.repositories.UserChatSubscriptionRepository
import ru.itmo.highload_systems_lab_1.database.repositories.FileRepository
import ru.itmo.highload_systems_lab_1.database.repositories.ProjectRepository
import ru.itmo.highload_systems_lab_1.dtos.CreateUserDTO
import ru.itmo.highload_systems_lab_1.dtos.ReadUserDTO
import ru.itmo.highload_systems_lab_1.dtos.UpdateUserDTO
import ru.itmo.highload_systems_lab_1.mappers.UserMapper
import ru.itmo.highload_systems_lab_1.exceptions.UserNotFoundException
import ru.itmo.highload_systems_lab_1.services.util.pageRequest
import ru.itmo.highload_systems_lab_1.services.ProjectService

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val accessKeyRepository: AccessKeyRepository,
    private val userProjectRepository: UserProjectRepository,
    private val userChatSubscriptionRepository: UserChatSubscriptionRepository,
    private val fileRepository: FileRepository,
    private val projectRepository: ProjectRepository,
    private val projectService: ProjectService
) {

    fun createUser(dto: CreateUserDTO): ReadUserDTO {
        val entity = userMapper.toEntity(dto)
        val saved = userRepository.save(entity)
        return userMapper.toReadDTO(saved)
    }

    fun getUser(login: String): ReadUserDTO {
        val user = userRepository.findById(login).orElseThrow { UserNotFoundException(login) }
        return userMapper.toReadDTO(user)
    }

    fun updateUser(login: String, dto: UpdateUserDTO): ReadUserDTO {
        val user = userRepository.findById(login).orElseThrow { UserNotFoundException(login) }
        userMapper.toEntity(dto, user)
        val saved = userRepository.save(user)
        return userMapper.toReadDTO(saved)
    }

    @Transactional
    fun deleteUser(login: String) {
        if (!userRepository.existsById(login)) {
            throw UserNotFoundException(login)
        }

        val userProjects = projectRepository.findAll().filter { it.creatorUserLogin.login == login }
        
        userProjects.forEach { project ->
            projectService.deleteProject(project.id)
        }

        accessKeyRepository.deleteAll(
            accessKeyRepository.findAll().filter { it.userLogin.login == login }
        )

        userProjectRepository.deleteAll(
            userProjectRepository.findAll().filter { it.id.userLogin == login }
        )

        userChatSubscriptionRepository.deleteAll(
            userChatSubscriptionRepository.findAll().filter { it.id.userLogin == login }
        )

        fileRepository.deleteAll(
            fileRepository.findAll().filter { it.uploaderUserLogin.login == login }
        )

        userRepository.deleteById(login)
    }

    fun findAll(page: Int, size: Int): Page<ReadUserDTO> {
        val pageable = pageRequest(page, size)
        return userRepository.findAll(pageable).map { userMapper.toReadDTO(it) }
    }
}
