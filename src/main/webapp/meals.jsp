<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<body>
    <table border=1>
        <thead>
        <tr>
            <th>Date and Time</th>
            <th>Description</th>
            <th>Calories</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${mealsTo}" var="meal">
            <tr  style="background-color:${meal.excess ? 'maroon' : 'lime'}; color:${meal.excess ? 'white' : 'black'}">

                <td>
                <fmt:parseDate value="${ meal.dateTime }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime"
                               type="both"/>
                <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }"/>
                </td>

                <td><c:out value="${meal.description}" /></td>
                <td><c:out value="${meal.calories}" /></td>

            </tr>
        </c:forEach>
        </tbody>
    </table>
</body>
</html>
