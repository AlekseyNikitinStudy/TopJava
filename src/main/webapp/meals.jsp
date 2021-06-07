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
<h2>Meals</h2>

<jsp:useBean id="meals" scope="request" type="java.util.List"/>

<a href="?action=create">Add meal</a>
<table style="border: 1px solid black">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>

    <c:forEach var="meal" items="${meals}">
        <javatime:format value="${meal.getDateTime()}" pattern="yyyy-MM-dd HH:mm" var="parsedDateTime"/>
        <tr class="${meal.isExcess() ? 'td_mealExcess' : 'td_mealNotExcess'}">
            <td>${parsedDateTime}</td>
            <td>${meal.getDescription()}</td>
            <td>${meal.getCalories()}</td>
            <td><a href="?action=update&id=${meal.getId()}">update</a></td>
            <td><a href="?action=delete&id=${meal.getId()}">delete</a></td>
        </tr>
    </c:forEach>

</table>

</body>
</html>