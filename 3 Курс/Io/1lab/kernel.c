struct sbiret
{
    long error;
    long value;
};

struct sbiret sbi_call(long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long fid, long eid)
{
    register long a0 __asm__("a0") = arg0;
    register long a1 __asm__("a1") = arg1;
    register long a2 __asm__("a2") = arg2;
    register long a3 __asm__("a3") = arg3;
    register long a4 __asm__("a4") = arg4;
    register long a5 __asm__("a5") = arg5;
    register long a6 __asm__("a6") = fid;
    register long a7 __asm__("a7") = eid;
    __asm__ __volatile__(
        "ecall"
        : "=r"(a0), "=r"(a1)
        : "r"(a0), "r"(a1), "r"(a2), "r"(a3), "r"(a4), "r"(a5), "r"(a6), "r"(a7)
        : "memory");
    return (struct sbiret){.error = a0, .value = a1};
}

void putchar(char ch)
{
    sbi_call(ch, 0, 0, 0, 0, 0, 0, 1); // Console Putchar (EID #0x01, FID #0)
}

int getchar(void)
{
    struct sbiret ret;
    ret.error = -1;
    while (ret.error == -1)
    {
        ret = sbi_call(0, 0, 0, 0, 0, 0, 0, 2);
    }
    return (char)ret.error;
}

void puts(const char *s)
{
    while (*s)
    {
        putchar(*s++);
    }
}

void print_hex(long num)
{
    const char *hex = "0123456789abcdef";
    char buf[11] = "0x00000000";
    for (int i = 9; i >= 2; i--)
    {
        buf[i] = hex[num & 0xF];
        num >>= 4;
    }
    puts(buf);
}

int read_number(void)
{
    int num = 0;
    int c;
    int has_input = 0;
    while ((c = getchar()) != -1)
    {
        if (c == '\r' || c == '\n')
        {
            if (has_input)
                break;
            continue;
        }
        if (c >= '0' && c <= '9')
        {
            num = num * 10 + (c - '0');
            has_input = 1;
            putchar(c);
        }
    }
    putchar('\n');
    return has_input ? num : -1;
}

void display_menu(void)
{
    puts("Menu:\n");
    puts("1. Get SBI specification version\n");
    puts("2. Get number of counters\n");
    puts("3. Get details of a counter\n");
    puts("4. System Shutdown\n");
    puts("Enter choice (1-4): ");
}

extern char __bss[], __bss_end[], __stack_top[];

void kernel_main(void)
{
    for (char *p = __bss; p < __bss_end; p++)
    {
        *p = 0;
    }

    while (1)
    {
        display_menu();
        int choice = getchar();
        putchar(choice);
        putchar('\n');

        switch (choice)
        {
        case '1':
        {
            struct sbiret ret = sbi_call(0, 0, 0, 0, 0, 0, 0, 0x10);
            puts("SBI Specification Version (value): ");
            print_hex(ret.value);
            puts("\nSBI Specification Version (error): ");
            print_hex(ret.error);
            puts("\n");
            break;
        }
        case '2':
        {
            // Get number of counters (EID #0x504D55, FID #0)
            struct sbiret ret = sbi_call(0, 0, 0, 0, 0, 0, 0, 0x504D55);
            puts("Number of counters: ");
            print_hex(ret.value);
            puts("\n");
            break;
        }
        case '3':
        {
            puts("Enter counter number: ");
            int counter_id = read_number();
            if (counter_id == -1)
            {
                puts("Invalid counter number.\n");
                break;
            }
            struct sbiret ret = sbi_call(counter_id, 0, 0, 0, 0, 0, 1, 0x504D55);
            puts("Counter details (value): ");
            print_hex(ret.value);
            puts("\nCounter details (error): ");
            print_hex(ret.error);
            puts("\n");
            break;
        }
        case '4':
        {
            puts("System Shutdown initiated\n");
            sbi_call(0, 0, 0, 0, 0, 0, 0, 0x08);
            break;
        }
        default:
            puts("Invalid choice. Please enter 1-4.\n");
        }
    }
}

__attribute__((section(".text.boot")))
__attribute__((naked)) void
boot(void)
{
    __asm__ volatile(
        "mv sp, %[stack_top] \n"
        "j kernel_main \n"
        :
        : [stack_top] "r"(__stack_top));
}