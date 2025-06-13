import matplotlib.pyplot as plt
import numpy as np

# Функция для NRZ кодирования


def nrz_encode(message):
    bit_duration = 1
    amplitude_high = 1
    amplitude_low = -1
    t = []
    signal = []
    current_time = 0
    for bit in message:
        amplitude = amplitude_high if bit == '1' else amplitude_low
        t.extend([current_time, current_time + bit_duration])
        signal.extend([amplitude, amplitude])
        current_time += bit_duration
    return t, signal

# Функция для Манчестерского кодирования


def manchester_encode(message):
    bit_duration = 1
    half_bit = bit_duration / 2
    amplitude_high = 1
    amplitude_low = -1
    t = []
    signal = []
    current_time = 0
    for bit in message:
        if bit == '1':
            t.extend([current_time, current_time + half_bit,
                     current_time + half_bit, current_time + bit_duration])
            signal.extend([amplitude_low, amplitude_low,
                          amplitude_high, amplitude_high])
        else:
            t.extend([current_time, current_time + half_bit,
                     current_time + half_bit, current_time + bit_duration])
            signal.extend([amplitude_high, amplitude_high,
                          amplitude_low, amplitude_low])
        current_time += bit_duration
    return t, signal

# Функция для NRZI кодирования


def nrzi_encode(message):
    bit_duration = 1
    amplitude_high = 1
    amplitude_low = -1
    current_level = amplitude_high
    t = []
    signal = []
    for bit in message:
        if bit == '1':
            current_level = amplitude_low if current_level == amplitude_high else amplitude_high
        t.extend([t[-1] if t else 0, t[-1] + bit_duration]
                 if t else [0, bit_duration])
        signal.extend([current_level, current_level])
    return t, signal

# Функция для RZ кодирования


def rz_encode(message):
    bit_duration = 1
    half_bit = bit_duration / 2
    amplitude_high = 1
    amplitude_low = -1
    amplitude_zero = 0
    t = []
    signal = []
    current_time = 0
    for bit in message:
        if bit == '1':
            t.extend([current_time, current_time + half_bit,
                     current_time + half_bit, current_time + bit_duration])
            signal.extend([amplitude_zero, amplitude_high,
                          amplitude_high, amplitude_zero])
        else:
            t.extend([current_time, current_time + half_bit,
                     current_time + half_bit, current_time + bit_duration])
            signal.extend([amplitude_zero, amplitude_low,
                          amplitude_low, amplitude_zero])
        current_time += bit_duration
    return t, signal


# Входное сообщение
message = "11000011110000001101000011000100"

# Создаем фигуру с четырьмя подграфиками
plt.figure(figsize=(12, 12))

# Список кодировок и их функций
encodings = [
    ('NRZ', nrz_encode),
    ('Manchester', manchester_encode),
    ('NRZI', nrzi_encode),
    ('RZ', rz_encode)
]

# Построение графиков
for i, (title, encode_func) in enumerate(encodings, 1):
    # Получаем сигнал
    t, signal = encode_func(message)

    # Создаем подграфик
    plt.subplot(4, 1, i)
    plt.step(t, signal, where='post', color='blue',
             linewidth=2, label=f'{title} Signal')

    # Добавляем подписи битов
    for j, bit in enumerate(message):
        plt.text(j + 0.5, 1.5, bit, ha='center', va='center', fontsize=10)

    # Добавляем вертикальные пунктирные линии
    for j in range(len(message) + 1):
        plt.axvline(x=j, color='gray', linestyle='--', linewidth=1)

    # Настраиваем подграфик
    plt.title(f'{title} Encoding of Message')
    plt.xlabel('Time')
    plt.ylabel('Amplitude')
    plt.grid(True)
    plt.ylim(-2, 2)
    plt.xlim(0, len(message))
    plt.legend()

# Оптимизируем расположение подграфиков
plt.tight_layout()

# Сохраняем объединенный график
plt.savefig('combined_plots.png')
