<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%@ taglib prefix="fn2" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--<c:set var="req" value="${pageContext.request}" />--%>
<%--<c:set var="uri" value="${req.requestURI}" />--%>
<%--<c:set var="url" value="${req.requestURL}" />--%>
<%--<c:set var="path" value="${req.contextPath}" />--%>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<head>
    <%--<base href="${fn2:substring(url, 0, fn2:length(url) - fn2:length(uri))}${path}/" />--%>
    <title><spring:message code="meals.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
    <h3><spring:message code="meal.title"/></h3>
    <form method="post" action="${pageContext.request.contextPath}/meals">
        <dl>
            <dt><spring:message code="meals.filter.startDate"/></dt>
            <dd><input type="date" name="startDate" value="${param.startDate}"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meals.filter.endDate"/></dt>
            <dd><input type="date" name="endDate" value="${param.endDate}"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meals.filter.startTime"/></dt>
            <dd><input type="time" name="startTime" value="${param.startTime}"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meals.filter.endTime"/></dt>
            <dd><input type="time" name="endTime" value="${param.endTime}"></dd>
        </dl>
        <button type="submit"><spring:message code="meals.submit"/></button>
        <button type="reset" onClick="javascript:location.href='${pageContext.request.contextPath}/meals'"><spring:message code="meals.resetFilter"/></button>
    </form>
    <hr/>
    <a href="meals/create"><spring:message code="meals.addMeal"/></a>
    <hr/>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th><spring:message code="meals.date"/></th>
            <th><spring:message code="meals.description"/></th>
            <th><spring:message code="meals.calories"/></th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr data-mealExcess="${meal.excess}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <%--TODO--%>
                <td><a href=meals/${meal.id}><spring:message code="meals.update"/></a></td>
                <td><a href=meals/delete/${meal.id}><spring:message code="meals.delete"/></a></td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>