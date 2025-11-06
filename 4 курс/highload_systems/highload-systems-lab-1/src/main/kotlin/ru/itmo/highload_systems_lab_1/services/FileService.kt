package ru.itmo.highload_systems_lab_1.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.itmo.highload_systems_lab_1.database.repositories.FileRepository
import ru.itmo.highload_systems_lab_1.dtos.CreateFileDTO
import ru.itmo.highload_systems_lab_1.dtos.ReadFileDTO
import ru.itmo.highload_systems_lab_1.dtos.UpdateFileDTO
import ru.itmo.highload_systems_lab_1.mappers.FileMapper
import ru.itmo.highload_systems_lab_1.exceptions.FileNotFoundException
import ru.itmo.highload_systems_lab_1.services.util.pageRequest

@Service
class FileService(
    private val fileRepository: FileRepository,
    private val fileMapper: FileMapper
) {

    fun createFile(dto: CreateFileDTO): ReadFileDTO {
        val entity = fileMapper.toEntity(dto)
        val saved = fileRepository.save(entity)
        return fileMapper.toReadDTO(saved)
    }

    fun getFile(id: Int): ReadFileDTO {
        val file = fileRepository.findById(id).orElseThrow { FileNotFoundException(id) }
        return fileMapper.toReadDTO(file)
    }

    fun updateFile(id: Int, dto: UpdateFileDTO): ReadFileDTO {
        val file = fileRepository.findById(id).orElseThrow { FileNotFoundException(id) }
        fileMapper.toEntity(dto, file)
        val saved = fileRepository.save(file)
        return fileMapper.toReadDTO(saved)
    }

    fun deleteFile(id: Int) {
        if (!fileRepository.existsById(id)) {
            throw FileNotFoundException(id)
        }
        fileRepository.deleteById(id)
    }

    fun findAll(page: Int, size: Int): Page<ReadFileDTO> {
        val pageable = pageRequest(page, size)
        return fileRepository.findAll(pageable).map { fileMapper.toReadDTO(it) }
    }
}
