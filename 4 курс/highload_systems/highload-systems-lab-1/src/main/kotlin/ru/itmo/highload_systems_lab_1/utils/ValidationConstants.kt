package ru.itmo.highload_systems_lab_1.utils

object UserConstraints {
    const val LOGIN_MIN = 5
    const val LOGIN_MAX = 16
    const val PASSWORD_HASH_LENGTH = 64
    const val ROLE_LENGTH = 20
}

object ProjectConstraints {
    const val TOKEN_BALANCE_PRECISION = 14
    const val TOKEN_BALANCE_SCALE = 2
}

object AccessKeyConstraints {
    const val KEY_LENGTH = 36
}

object ChatConstraints {
    const val CHAT_NAME_MAX = 255
}

object FileConstraints {
    const val FILE_NAME_MAX = 255
    const val STORAGE_LINK_MAX = 512
}
