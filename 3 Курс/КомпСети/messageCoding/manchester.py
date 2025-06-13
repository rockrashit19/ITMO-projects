import matplotlib.pyplot as plt
import numpy as np


def manchester_encode(message):
    # Параметры Манчестерского кодирования
    bit_duration = 1  # Длительность одного бита
    half_bit = bit_duration / 2  # Половина битового интервала
    amplitude_high = 1  # Высокий уровень
    amplitude_low = -1  # Низкий уровень

    # Массивы для времени и сигнала
    t = []
    signal = []
    current_time = 0

    # Формируем сигнал Манчестерского кода
    for bit in message:
        if bit == '1':
            # Бит 1: переход от -1 к +1
            t.extend([current_time, current_time + half_bit,
                     current_time + half_bit, current_time + bit_duration])
            signal.extend([amplitude_low, amplitude_low,
                          amplitude_high, amplitude_high])
        else:
            # Бит 0: переход от +1 к -1
            t.extend([current_time, current_time + half_bit,
                     current_time + half_bit, current_time + bit_duration])
            signal.extend([amplitude_high, amplitude_high,
                          amplitude_low, amplitude_low])
        current_time += bit_duration

    return t, signal


# Входное сообщение
message = "11011110111010101100001010001101"

# Получаем сигнал
t, signal = manchester_encode(message)

# Создаем график
plt.figure(figsize=(12, 4))
plt.step(t, signal, where='post', color='blue',
         linewidth=2, label='Manchester Signal')

# Добавляем подписи битов
for i, bit in enumerate(message):
    plt.text(i + 0.5, 1.5, bit, ha='center', va='center', fontsize=10)

# Добавляем вертикальные пунктирные линии
for i in range(len(message) + 1):
    plt.axvline(x=i, color='gray', linestyle='--', linewidth=1)

# Настраиваем график
plt.title('Manchester Encoding of Message')
plt.xlabel('Time')
plt.ylabel('Amplitude')
plt.grid(True)
plt.ylim(-2, 2)
plt.xlim(0, len(message))
plt.legend()

# Сохраняем график
plt.savefig('manchester_plot3.png')
