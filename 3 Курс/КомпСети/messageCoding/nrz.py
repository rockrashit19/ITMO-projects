import matplotlib.pyplot as plt
import numpy as np

# Входное сообщение
message = "11000011110000001101000011000100"

# Параметры NRZ кодирования
bit_duration = 1  # Длительность одного бита
amplitude_high = 1  # Уровень для бита 1
amplitude_low = -1  # Уровень для бита 0

# Создаем массивы для времени и сигнала
t = []
signal = []
current_time = 0

# Формируем сигнал NRZ
for bit in message:
    amplitude = amplitude_high if bit == '1' else amplitude_low
    t.extend([current_time, current_time + bit_duration])
    signal.extend([amplitude, amplitude])
    current_time += bit_duration

# Создаем график
plt.figure(figsize=(12, 4))
plt.step(t, signal, where='post', color='blue',
         linewidth=2, label='NRZ Signal')

# Добавляем подписи битов
for i, bit in enumerate(message):
    plt.text(i + 0.5, 1.5, bit, ha='center', va='center', fontsize=10)

# Добавляем вертикальные пунктирные линии
for i in range(len(message) + 1):
    plt.axvline(x=i, color='gray', linestyle='--', linewidth=1)

# Настраиваем график
plt.title('NRZ Encoding of Message')
plt.xlabel('Time')
plt.ylabel('Amplitude')
plt.grid(True)
plt.ylim(-2, 2)
plt.xlim(0, len(message))
plt.legend()

# Сохраняем график
plt.savefig('nrz_plot.png')
