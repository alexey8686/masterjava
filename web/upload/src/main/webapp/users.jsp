<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Meal list</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<section>

    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>UserName</th>
            <th>Mail</th>
            <th>Flag</th>

        </tr>
        </thead>
        <c:forEach items="${users}" var="user">
            <jsp:useBean id="user" scope="page" type="ru.javaops.masterjava.xml.schema.User"/>
            <tr data-mealExceed="${meal.exceed}">

                <td>${user.value}</td>
                <td>${user.email}</td>
                <td>${user.flag}</td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>