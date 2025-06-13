struct maybe_int64
{
    int64_t value;
    bool valid;
};

struct maybe_int64 some_int64(int64_t i);

extern const struct maybe_int64 none_int64;

void maybe_int64_print(struct maybe_int64 i);
struct maybe_int64 maybe_read_int64();
void print_int64(int64_t i);

void interpret_push(struct vm_state *state)
{
    stack_push(&state->data_stack, state->ip->as_arg64.arg);
}

void interpret_iread(struct vm_state *state)
{
    struct maybe_int64 val = maybe_read_int64();
    stack_push(&state->data_stack, val.value);
}
void interpret_iadd(struct vm_state *state)
{
    struct maybe_int64 b = stack_pop(&state->data_stack);
    struct maybe_int64 a = stack_pop(&state->data_stack);
    stack_push(&state->data_stack, a.value + b.value);
}
void interpret_iprint(struct vm_state *state)
{
    print_int64(stack_pop(&state->data_stack).value);
}

/* Подсказка: можно выполнять программу пока ip != NULL,
    тогда чтобы её остановить достаточно обнулить ip */
void interpret_stop(struct vm_state *state)
{
    state->ip = NULL;
}

typedef void (*handler)(struct vm_state *);

handler interpreters[] = {
    [BC_PUSH] = interpret_push,
    [BC_IPRINT] = interpret_iprint,
    [BC_IREAD] = interpret_iread,
    [BC_IADD] = interpret_iadd,
    [BC_STOP] = interpret_stop,
};

void interpret(struct vm_state *state)
{
    while (state->ip != NULL)
    {
        interpreters[state->ip->opcode](state);
        if (state->ip != NULL)
        {
            state->ip++;
        }
    }
}

