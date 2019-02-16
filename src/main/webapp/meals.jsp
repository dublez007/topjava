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
    <table style="width:25%; border: 1px">
        <thead>
        <tr>
            <th>ID</th>
            <th>Date and Time</th>
            <th>Description</th>
            <th>Calories</th>
            <th colspan="2">Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${mealsTo}" var="meal">
            <tr  style="background-color:${meal.excess ? 'maroon' : 'lime'}; color:${meal.excess ? 'white' : 'black'}">

                <td><c:out value="${meal.id}" /></td>
                <td>
                <fmt:parseDate value="${ meal.dateTime }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime"
                               type="both"/>
                <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }"/>
                </td>

                <td><c:out value="${meal.description}" /></td>
                <td><c:out value="${meal.calories}" /></td>
                <td style="background: white"><a href="mealsServlet?action=update&mealId=${meal.id}">Update</a></td>
                <td style="background: white"><a href="mealsServlet?action=delete&mealId=${meal.id}">Delete</a></td>

            </tr>
        </c:forEach>
        </tbody>
    </table>
    <br/>
    <p><a href="mealsServlet?action=insert">Add Meal</a></p>
</body>
</html>
