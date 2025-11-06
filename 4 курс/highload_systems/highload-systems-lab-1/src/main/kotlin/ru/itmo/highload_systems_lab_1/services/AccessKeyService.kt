package ru.itmo.highload_systems_lab_1.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.itmo.highload_systems_lab_1.database.repositories.AccessKeyRepository
import ru.itmo.highload_systems_lab_1.dtos.CreateAccessKeyDTO
import ru.itmo.highload_systems_lab_1.dtos.ReadAccessKeyDTO
import ru.itmo.highload_systems_lab_1.dtos.UpdateAccessKeyDTO
import ru.itmo.highload_systems_lab_1.mappers.AccessKeyMapper
import ru.itmo.highload_systems_lab_1.exceptions.AccessKeyNotFoundException
import ru.itmo.highload_systems_lab_1.services.util.pageRequest

@Service
class AccessKeyService(
    private val accessKeyRepository: AccessKeyRepository,
    private val accessKeyMapper: AccessKeyMapper
) {

    fun createAccessKey(dto: CreateAccessKeyDTO): ReadAccessKeyDTO {
        val entity = accessKeyMapper.toEntity(dto)
        val saved = accessKeyRepository.save(entity)
        return accessKeyMapper.toReadDTO(saved)
    }

    fun getAccessKey(key: String): ReadAccessKeyDTO {
        val accessKey = accessKeyRepository.findById(key).orElseThrow { AccessKeyNotFoundException(key) }
        return accessKeyMapper.toReadDTO(accessKey)
    }

    fun updateAccessKey(key: String, dto: UpdateAccessKeyDTO): ReadAccessKeyDTO {
        val accessKey = accessKeyRepository.findById(key).orElseThrow { AccessKeyNotFoundException(key) }
        accessKeyMapper.toEntity(dto, accessKey)
        val saved = accessKeyRepository.save(accessKey)
        return accessKeyMapper.toReadDTO(saved)
    }

    fun deleteAccessKey(key: String) {
        if (!accessKeyRepository.existsById(key)) {
            throw AccessKeyNotFoundException(key)
        }
        accessKeyRepository.deleteById(key)
    }

    fun findAll(page: Int, size: Int): Page<ReadAccessKeyDTO> {
        val pageable = pageRequest(page, size)
        return accessKeyRepository.findAll(pageable).map { accessKeyMapper.toReadDTO(it) }
    }
}
