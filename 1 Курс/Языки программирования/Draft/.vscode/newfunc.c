#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>

// Функция для проверки, является ли строка двоичным числом
int is_binary(const char* str) {
    for (size_t i = 0; i < strlen(str); i++) {
        if (str[i] != '0' && str[i] != '1') {
            return 0; // Не двоичное число
        }
    }
    return 1; // Двоичное число
}

// Функция для перевода двоичной строки в целое число
uint16_t binary_to_int(const char* binary) {
    uint16_t result = 0;
    for (size_t i = 0; i < strlen(binary); i++) {
        result = (result << 1) | (binary[i] - '0');
    }
    return result;
}

int main() {
    char binary[20]; // Буфер для ввода двоичного числа

    while (1) {
        printf("Введите двоичное число (до 16 символов) или 'end' для выхода: ");
        scanf("%s", binary);

        // Проверяем, ввёл ли пользователь "end"
        if (strcmp(binary, "end") == 0) {
            printf("Программа завершена.\n");
            break; // Выход из цикла
        }

        // Проверяем, является ли ввод двоичным числом
        if (!is_binary(binary)) {
            printf("Ошибка: введено не двоичное число.\n");
            continue; // Пропускаем итерацию
        }

        // Преобразуем двоичное число в целое число
        uint16_t num = binary_to_int(binary);

        // Выводим шестнадцатеричное представление
        printf("Шестнадцатеричное представление: %04X\n", num);
    }

    return 0;
}