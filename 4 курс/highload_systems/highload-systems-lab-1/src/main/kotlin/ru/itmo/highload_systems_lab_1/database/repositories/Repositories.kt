package ru.itmo.highload_systems_lab_1.database.repositories

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmo.highload_systems_lab_1.database.entities.*

@Repository
interface AccessKeyRepository: JpaRepository<AccessKey, String> {}

@Repository
interface ChatRepository: JpaRepository<Chat, Int> {}

@Repository
interface FileRepository: JpaRepository<File, Int> {}

@Repository
interface ProjectRepository: JpaRepository<Project, Int> {
    fun findByIdGreaterThanOrderById(id: Int, pageable: Pageable): List<Project>
}

@Repository
interface UserRepository: JpaRepository<User, String> {}

@Repository
interface UserChatSubscriptionRepository: JpaRepository<UserChatSubscription, UserChatSubscriptionId> {}

@Repository
interface UserProjectRepository: JpaRepository<UserProject, UserProjectId> {}