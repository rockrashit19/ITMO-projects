package ru.itmo.highload_systems_lab_1.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class FileNotFoundException(id: Int) : RuntimeException("File not found: $id")
