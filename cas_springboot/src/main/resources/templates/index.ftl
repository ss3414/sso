<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
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
        <td><a href="/login1">直接访问login1</a></td>
    </tr>
    <tr>
        <td><a href="http://cas.test.com/login?service=http%3A%2F%2F127.0.0.1%3A8000%2Flogin1">访问CAS登录地址再重定向回login1</a></td>
    </tr>
    <tr>
        <td><a href="/login2">直接访问login2</a></td>
    </tr>
</table>

</body>
</html>
