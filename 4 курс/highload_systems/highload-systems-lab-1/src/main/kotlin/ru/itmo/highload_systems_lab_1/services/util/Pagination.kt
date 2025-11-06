package ru.itmo.highload_systems_lab_1.services.util

import org.springframework.data.domain.PageRequest

fun pageRequest(page: Int, size: Int) =
    PageRequest.of(page.coerceAtLeast(0), size.coerceIn(1, 50))