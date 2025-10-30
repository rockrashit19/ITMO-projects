<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Форма и график</title>
    <link rel="stylesheet" href="style/styles.css">
    <script src="scripts/draw.js"></script>
    <script src="scripts/events.js"></script>
</head>
<body>
<header>
    <h1>Galiullin Rashit, P3334, Variant 8080</h1>
</header>
<table class="input-canvas-table">
    <tr>
        <td class="form-cell">
            <form id="pointForm" action="controller" method="get">
                <fieldset>
                    <legend>Ввод координат</legend>
                    <div class="form-group">
                        <label>X:</label><br>
                        <c:forEach var="x" items="${[-5, -4, -3, -2, -1, 0, 1, 2, 3]}">
                            <label>
                                <input type="radio" name="x" value="${x}" required> ${x}
                            </label>
                        </c:forEach>
                    </div>
                    <div class="form-group">
                        <label>Y (-3 … 3):</label><br>
                        <input type="text" name="y" id="yInput" required>
                    </div>
                    <div class="form-group">
                        <label>R:</label><br>
                        <select name="r" id="rSelect" required>
                            <option value="">Выберите R</option>
                            <c:forEach var="r" items="${[1, 2, 3, 4, 5]}">
                                <option value="${r}">${r}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <input type="submit" value="Проверить">
                    </div>
                </fieldset>
            </form>
        </td>
        <td class="canvas-cell">
            <canvas id="areaCanvas" width="400" height="400"></canvas>
        </td>
    </tr>
</table>
<table class="results-table">
    <thead>
    <tr>
        <th>X</th><th>Y</th><th>R</th><th>Результат</th><th>Время</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="result" items="${sessionScope.resultBean.results}">
        <tr>
            <td>${result.x}</td>
            <td>${result.y}</td>
            <td>${result.r}</td>
            <td>${result.inside ? "Попадание" : "Промах"}</td>
            <td>${result.timestamp}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<script>
    window.initialPoints = [
        <c:forEach var="result" items="${sessionScope.resultBean.results}" varStatus="loop">
        {
            x: ${result.x},
            y: ${result.y},
            r: ${result.r},
            inside: ${result.inside}
        }<c:if test="${!loop.last}">,</c:if>
        </c:forEach>
    ];
    <c:choose>
    <c:when test="${not empty latestX}">
    window.latestData = {
        x: ${latestX},
        y: ${latestY},
        r: ${latestR},
        inside: ${latestInside}
    };
    </c:when>
    <c:otherwise>
    window.latestData = null;
    </c:otherwise>
    </c:choose>
</script>
</body>
</html>
