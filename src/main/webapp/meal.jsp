<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<style>
    <%@include file="/WEB-INF/style.css"%>
</style>

<html>
<head>
    <title>Meal</title>
</head>
<body>
    <h3><a href="index.html">Home</a></h3>
    <form class="meal_form" method="POST" action="mealsServlet" name="updatedMeal">
        <ul>
            <li>
                <h2>Meal</h2>
            </li>
        </ul>
        <ul>
            <li>
            <label>id:</label>
                <input type="number" readonly="readonly" name="mealId" value="<c:out value="${mealTo.id}"/>"/><br/>
            </li>
            <li>
                <label>Date and time:</label>
                <input type="datetime-local" name="dateTime" placeholder="01.01.2019 12:00"
                       value="
<fmt:parseDate value="${ mealTo.dateTime }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime"
                               type="both"/>
<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }"/>"/>
                <br/>
            </li>
            <li>
                <label>Description:</label>
                <input type="text" name="mealDescription" placeholder="Завтрак" value="<c:out value="${mealTo.description}"/>"/><br/>
            </li>
            <li>
                <label>Calories:</label>
                <input type="text" name="mealCalories" placeholder="1000" value="<c:out value="${mealTo.calories}"/>"/><br/>
            </li>
            <li>
                <button class="submit" type="submit" value="save">Save</button>
            </li>
        </ul>
    </form>
</body>
</html>
