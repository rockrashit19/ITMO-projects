#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>

// Контекст для обработчиков событий
struct context {
    int64_t counter;
};

// У кнопки есть имя и обработчик
struct button {
    const char* label;
    void (*handler)(const struct button*, struct context*);
};

// Массив из нескольких кнопок
struct buttons {
    struct button* array;
    size_t count;
};

// Вызвать событие "Нажатие на кнопку"
void click(struct button* b, struct context* ctx) {
    b->handler(b, ctx);
}

// Обработчик нажатия на первую кнопку
void print_meow_handler(const struct button* b, struct context* ctx) {
    printf("Meow!\n");
    ctx->counter = ctx->counter + 1;
}

// Обработчик нажатия на вторую кнопку
void print_ctx_handler(const struct button* b, struct context* c) {
    printf("Said \"Meow!\" %" PRId64 " times.\n", c->counter);
}

// Показать кнопки
void print_buttons(struct buttons buttons) {
    for (size_t i = 0; i < buttons.count; i = i + 1) {
        printf("%zu : %s \n", i, buttons.array[i].label);
    }
}

// Цикл для ввода номера кнопки и нажатия на неё
void prompt_click_button(struct buttons buttons, struct context* ctx) {
    for (;;) {
        print_buttons(buttons);
        printf("Input button index: ");
        size_t idx;
        scanf("%zu", &idx); // Простое чтение индекса
        if (idx >= buttons.count) {
            printf("No such button, bye.\n");
            break;
        } else {
            click(buttons.array + idx, ctx);
        }
    }
}

int main() {
    // Две кнопки
    struct button buttons[] = {
        {"Say Meow", print_meow_handler},
        {"Status", print_ctx_handler},
    };
    // Количество кнопок
    const size_t count = sizeof(buttons) / sizeof(buttons[0]);

    // Экземпляр контекста
    struct context ctx = {0};

    prompt_click_button((struct buttons){buttons, count}, &ctx);

    return 0;
}