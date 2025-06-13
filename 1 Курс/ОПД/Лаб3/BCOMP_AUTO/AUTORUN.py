import os
import re
import time
STEPS = 256
CSV_SEP = ';'


prog = open("input.txt", "r").read().strip().split('\n')
vals = {};
point = 16;
start_point = -1;
error = 0;
asm = {};
for line in prog:
    if re.search(r"\w+\s*=\s*[0-9A-F]{4}", line):
        m = re.search(r"(\w+)\s*=\s*([0-9A-F]{4})", line);
        vals[m.group(1)] = m.group(2)
    elif re.search(r"[0-9A-F]{3}\s*:", line):
        m = re.search(r"([0-9A-F]{3})\s*:", line);
        point = int(m.group(1), 16)
    elif re.search(r"^\s*("+"|".join(vals.keys())+")\s*$", line):
        m = re.search(r"("+"|".join(vals.keys())+")", line);
        asm[point] = vals[m.group(1)]
        point += 1;
    elif re.search(r"\+?\s*[0-9A-F]{4}", line):
        m = re.search(r"(\+?)\s*([0-9A-F]{4})", line);
        if m.group(1)=="+":
            start_point = point;
        asm[point] = m.group(2)
        point += 1;
    else:
        print('unknown command "' + line +'"');
        error += 1

if start_point < 0:
    print('Not found start command')
    error += 1

if error>0:
    time.sleep(3)
    exit(1)

java_inp = 'asm\n'

for line in asm.keys():
    java_inp += 'ORG '+hex(line)+'\n'
    java_inp += 'WORD 0x'+asm[line]+'\n'

java_inp += 'END\n'
java_inp += hex(start_point)[2:]+' a\n'
java_inp += 'c '*STEPS+'\n'

open("__raw_input.txt", "w").write(java_inp)

if os.system('java -jar -Dmode=cli bcomp-ng.jar < __raw_input.txt > __raw_output.txt') > 0:
    print('system error')
    time.sleep(3)
    exit(1)


time.sleep(0.5)
tras = open("__raw_output.txt", "r").read().strip().split('\n')
if (len(tras)<STEPS):
    print('FATAL ERROR')
    time.sleep(3)
    exit(1)

tras = tras[8:]
csv = 'Адрес;Код;IP;CR;AR;DR;SP;BR;AC;NZVC;Адрес;Новый код\n'
csv2 = 'Адрес\tКод\tIP\tCR\tAR\tDR\tSP\tBR\tAC\tNZVC\tАдрес\tНовый код\n'
for line in tras:
    csv += '="'+('"'+CSV_SEP+'="').join(line.split(' '))+'"\n';
    csv2 += ('\t').join(line.split(' '))+'\n';
    if line.split(' ')[1] == '0100':
        break;



open("output.csv", "w").write(csv)
open("output2.txt", "w").write(csv2)
