<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/common.css">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<javatime:format value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime"/>

<h2>${meal.id == 0 ? 'New meal' : 'Edit meal'}</h2>

<form method="post" action="meals" enctype="application/x-www-form-urlencoded">
    <input type="hidden" name="id" value="${meal.id}">
    <dl>
        <dt>Datetime:</dt>
        <dd><input type="datetime-local" name="datetime" size=50 value="${parsedDateTime}"></dd>
    </dl>
    <dl>
        <dt>Description:</dt>
        <dd><input type="text" name="description" size=50 value="${meal.description}"></dd>
    </dl>
    <dl>
        <dt>Calories:</dt>
        <dd><input type="number" name="calories" size=50 value="${meal.calories}"></dd>
    </dl>

    <hr>
    <button onclick="window.history.back()" type="reset">Отменить</button>
    <button type="submit">Сохранить</button>
</form>

</body>
</html>