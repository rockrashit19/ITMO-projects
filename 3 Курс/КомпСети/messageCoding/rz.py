import matplotlib.pyplot as plt
import numpy as np


def rz_encode(message):
    # Параметры RZ кодирования
    bit_duration = 1  # Длительность одного бита
    half_bit = bit_duration / 2  # Половина битового интервала
    amplitude_high = 1  # Высокий уровень для бита 1
    amplitude_low = -1  # Низкий уровень для бита 0
    amplitude_zero = 0  # Нулевой уровень

    # Массивы для времени и сигнала
    t = []
    signal = []
    current_time = 0

    # Формируем сигнал RZ
    for bit in message:
        if bit == '1':
            # Бит 1: 0 -> +1 на первой половине, затем -> 0
            t.extend([current_time, current_time + half_bit,
                     current_time + half_bit, current_time + bit_duration])
            signal.extend([amplitude_zero, amplitude_high,
                          amplitude_high, amplitude_zero])
        else:
            # Бит 0: 0 -> -1 на первой половине, затем -> 0
            t.extend([current_time, current_time + half_bit,
                     current_time + half_bit, current_time + bit_duration])
            signal.extend([amplitude_zero, amplitude_low,
                          amplitude_low, amplitude_zero])
        current_time += bit_duration

    return t, signal


# Входное сообщение
message = "11000011110000001101000011000100"

# Получаем сигнал
t, signal = rz_encode(message)

# Создаем график
plt.figure(figsize=(12, 4))
plt.step(t, signal, where='post', color='blue', linewidth=2, label='RZ Signal')

# Добавляем подписи битов
for i, bit in enumerate(message):
    plt.text(i + 0.5, 1.5, bit, ha='center', va='center', fontsize=10)

# Добавляем вертикальные пунктирные линии
for i in range(len(message) + 1):
    plt.axvline(x=i, color='gray', linestyle='--', linewidth=1)

# Настраиваем график
plt.title('RZ Encoding of Message')
plt.xlabel('Time')
plt.ylabel('Amplitude')
plt.grid(True)
plt.ylim(-2, 2)
plt.xlim(0, len(message))
plt.legend()

# Сохраняем график
plt.savefig('rz_plot.png')
