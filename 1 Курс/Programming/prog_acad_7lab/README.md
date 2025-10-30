Компиляция
mvn -U clean package -DskipTests 
Перенос
scp -P 2222 target/*-shaded.jar s490149@se.ifmo.ru:~   

Helious запуск
ssh -p 2222 s490149@se.ifmo.ru

Переменные
export DB_HOST=pg DB_PORT=5432 DB_NAME=studs
export DB_USER='s490149' DB_PASSWORD= (в ~/.pgpass)

Сервер запуск
java -jar prog_acad_7lab-1.0-SNAPSHOT-shaded.jar

Клиент запуск
java -cp prog_acad_7lab-1.0-SNAPSHOT-shaded.jar client.ClientMain

Процесс
sockstat -4 -l | grep 4242
kill -9 номер

