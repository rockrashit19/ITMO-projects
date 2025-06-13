#!/bin/bash
set -xue

qemu-system-riscv64 \
    -machine virt \
    -nographic \
    -m 5G \
    -smp 1 \
    -bios /usr/lib/riscv64-linux-gnu/opensbi/generic/fw_jump.bin \
    -kernel '/Users/admin/Documents/Documents - MacBook Pro/Рашит/Университеты/Итмо/ITMO/3 Курс/Io/2lab/uboot.elf'
    -device virtio-rng-pci \
    -drive file=/Users/admin/riscv-tools/ubuntu-20.04.5-preinstalled-server-riscv64+unmatched.img,format=raw,if=virtio \
    -device virtio-net-device,netdev=net \
    -netdev user,id=net,hostfwd=tcp::2222-:22
