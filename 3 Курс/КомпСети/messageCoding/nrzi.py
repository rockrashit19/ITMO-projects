import matplotlib.pyplot as plt
import numpy as np


def nrzi_encode(message):
    # Параметры NRZI кодирования
    bit_duration = 1  # Длительность одного бита
    amplitude_high = 1  # Высокий уровень
    amplitude_low = -1  # Низкий уровень
    current_level = amplitude_high  # Начальный уровень сигнала (+1)

    # Массивы для времени и сигнала
    t = []
    signal = []

    # Формируем сигнал NRZI
    for bit in message:
        if bit == '1':
            # Бит 1: инвертировать уровень
            current_level = amplitude_low if current_level == amplitude_high else amplitude_high
        # Бит 0: уровень не меняется
        t.extend([t[-1] if t else 0, t[-1] + bit_duration]
                 if t else [0, bit_duration])
        signal.extend([current_level, current_level])

    return t, signal


# Входное сообщение
message = "11000011110000001101000011000100"

# Получаем сигнал
t, signal = nrzi_encode(message)

# Создаем график
plt.figure(figsize=(12, 4))
plt.step(t, signal, where='post', color='blue',
         linewidth=2, label='NRZI Signal')

# Добавляем подписи битов
for i, bit in enumerate(message):
    plt.text(i + 0.5, 1.5, bit, ha='center', va='center', fontsize=10)

# Добавляем вертикальные пунктирные линии
for i in range(len(message) + 1):
    plt.axvline(x=i, color='gray', linestyle='--', linewidth=1)

# Настраиваем график
plt.title('NRZI Encoding of Message')
plt.xlabel('Time')
plt.ylabel('Amplitude')
plt.grid(True)
plt.ylim(-2, 2)
plt.xlim(0, len(message))
plt.legend()

# Сохраняем график
plt.savefig('nrzi_plot.png')
