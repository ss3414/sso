<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
</head>
<body>

<h2>欢迎页</h2>
<h3><a href="/">返回欢迎页</a></h3>

<!--************************************************************分割线************************************************************-->

<table border="1">
    <tr>
        <td><a href="http://cas.test.com/login">CAS登录地址（默认casuser+Mellon）</a></td>
    </tr>
    <tr>
        <td><a href="/login1.jsp">直接访问login1.jsp</a></td>
    </tr>
    <tr>
        <td><a href="http://cas.test.com/login?service=http%3A%2F%2F127.0.0.1%3A8080%2Flogin1.jsp">访问CAS登录地址再重定向回login1.jsp</a></td>
    </tr>
    <tr>
        <td><a href="/login2">直接访问login2</a></td>
    </tr>
</table>

</body>
</html>
