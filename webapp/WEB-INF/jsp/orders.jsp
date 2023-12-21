<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Orders</title>
</head>
<body>
<%@include file="header.jsp"%>
<h3>Список заказов</h3>
<c:if test="${not empty requestScope.orders}">
<table>
    <tr>
        <th>id</th>
        <th>Адрес - Статус - Дата доставки</th>
    </tr>
    <c:forEach var="order" items="${requestScope.orders}">
        <tr>
            <th>${order.id()}</th>
            <th>${order.description()}</th>
        </tr>
    </c:forEach>
</table>
</c:if>
</body>
</html>