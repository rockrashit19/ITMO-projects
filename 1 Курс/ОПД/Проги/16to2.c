#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>

// Функция для перевода числа в двоичное представление
void print_binary(uint16_t num)
{
    for (int i = 15; i >= 0; i--)
    {
        printf("%d", (num >> i) & 1);
        if (i % 4 == 0 && i != 0)
        {
            printf(" ");
        }
    }
    printf("\n");
}

int main()
{
    char hex[20]; // Буфер для ввода шестнадцатеричного числа

    while (1)
    {
        printf("Введите шестнадцатеричное число (до 4 символов) или 'end' для выхода: ");
        scanf("%s", hex);

        // Проверяем, ввёл ли пользователь "end"
        if (strcmp(hex, "end") == 0)
        {
            printf("Программа завершена.\n");
            break; // Выход из цикла
        }

        // Преобразуем шестнадцатеричное число в целое число
        uint16_t num = (uint16_t)strtol(hex, NULL, 16);

        // Выводим двоичное представление
        printf("Двоичное представление: ");
        print_binary(num);
    }

    return 0;
}