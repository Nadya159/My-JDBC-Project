<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Products</title>
    <style type="text/css">
        TABLE {
            border-collapse: collapse; /* Убираем двойные линии между ячейками */
        }
        TD, TH {
            padding: 3px; /* Поля вокруг содержимого таблицы */
            border: 1px solid black; /* Параметры рамки */
        }
    </style>
</head>
<body>
<%@include file="header.jsp"%>
<h3>Список продуктов JSP</h3>
<table>
    <tr>
        <th>id</th>
        <th>Наименование - Цена - Остаток</th>
    </tr>
    <c:forEach var="product" items="${requestScope.products}">
        <tr>
            <th>${product.id()}</th>
            <th>${product.description()}</th>
        </tr>
    </c:forEach>
</table>
</body>
</html>
