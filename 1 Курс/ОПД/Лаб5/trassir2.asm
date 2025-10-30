ORG 0x292

START:
    CLA
    LD STR
    ST ABUF

S1:
    IN 5
    AND #0x40
    BEQ S1
    IN 4
    ST (ABUF)+
    ST CNT
    LD CNT
    BEQ F

S2:
    IN 5
    AND #0x40
    BEQ S2
    IN 4
    SWAB
    ST BUF
    ST (ABUF)
    LD CNT
    DEC
    ST CNT
    BEQ F
S3:
    IN 5
    AND #0x40
    BEQ S3
    LD BUF
    IN 4
    ST (ABUF)+
    LOOP CNT
    JUMP S2
F:
    HLT

STR: WORD 0x5D3 ; адрес начала
ABUF: WORD ?
CNT: WORD ?
BUF: WORD ?