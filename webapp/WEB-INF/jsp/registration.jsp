<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/registration" method="post">
    <label for="name">Name:
        <input type="text" name="name" id="name">
    </label>
    <br/><br/>
    <label for="name">Birthday:
        <input type="date" name="birthday" id="birthday">
    </label>
    <br/><br/>
    <label for="email">Phone:
        <input type="text" name="phone" id="phone">
    </label>
    <br/><br/>
    <label for="email">Email:
        <input type="text" name="email" id="email">
    </label>
    <br/><br/>
    <label for="pwd">Password:
        <input type="password" name="pwd" id="pwd">
    </label>
    <br/><br/>

    <c:forEach var="role" items="${requestScope.roles}">
        <input type="radio" name="role" value="${role}"> ${role}
        <br>
    </c:forEach>
    <br/>
    <c:forEach var="gender" items="${requestScope.genders}">
        <input type="radio" name="gender" value="${gender}"> ${gender}
        <br/>
    </c:forEach>
    <br/>
    <input type="submit" value="Send">
</form>
<c:if test="${not empty requestScope.errors}">
    <div style="color: red">
        <c:forEach var="error" items="${requestScope.errors}">
            <span>${error.message}</span>
        </c:forEach>
    </div>
</c:if>
</body>
</html>
