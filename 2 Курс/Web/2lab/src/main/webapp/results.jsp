<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Результаты</title>
    <style>
        table { border-collapse: collapse; }
        th, td { border: 1px solid black; padding: 5px; }
    </style>
</head>
<body>
<table>
    <tr><th>Параметр</th><th>Значение</th></tr>
    <tr><td>X</td><td>${requestScope.latestX}</td></tr>
    <tr><td>Y</td><td>${requestScope.latestY}</td></tr>
    <tr><td>R</td><td>${requestScope.latestR}</td></tr>
    <tr><td>Результат</td><td>${requestScope.latestInside ? 'Попадание' : 'Промах'}</td></tr>
</table>
<a href="controller">Вернуться к форме</a>
</body>
</html>