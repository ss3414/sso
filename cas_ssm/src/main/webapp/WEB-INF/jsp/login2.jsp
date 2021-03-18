<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
</head>
<body>

<h2>CAS登录页（SpringMVC）</h2>
<h3><a href="/">返回欢迎页</a></h3>

<!--************************************************************分割线************************************************************-->

<table border="1">
    <tr>
        <td>已登录用户</td>
        <td>注销</td>
    </tr>
    <tr>
        <td>
            ${user}
        </td>
        <td><a href="/logout2">注销</a></td>
    </tr>
</table>

</body>
</html>
