org 0x49C
START:
    CLA
    ST 0x4B7

    LD 0x4B6
    DEC
    PUSH
    CALL 0x668
    POP
    DEC
    SUB 0x4B7
    ST 0x4B7

    LD 0x4B5
    INC
    PUSH
    CALL 0x668
    POP
    SUB 0x4B7
    ST 0x4B7

    LD 0x4B4
    PUSH
    CALL 0x668
    POP
    ADD 0x4B7
    ST 0x4B7
    HLT

org 0x668
F_FUNC:
    LD &1
    BEQ F_ZERO
    BPL F_POS

F_NEG:
    SUB CONST_A
    BMI F_NEG_A
    JUMP F_A

F_NEG_A:
    ADD CONST_A

F_POS:
    ASL
    ASL
    SUB SP_DEC
    ADD CONST_B
    JUMP F_END

F_ZERO:
    ASL
    ASL
    SUB SP_DEC
    ADD CONST_B
    JUMP F_END

F_A:
    LD CONST_A

F_END:
    ST &1
    RET

org 0x4B4
Z:      word 0x000A
Y:      word 0xFFFB
X:      word 0xF768
R:      word 0x0000

org 0x677
CONST_A:    word 0xF770
CONST_B:    word 0x011D
SP_DEC:     word 0x0001
