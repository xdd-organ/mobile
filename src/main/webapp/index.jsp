<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script type="text/javascript" src="../mobile/stat/js/jquery/jqeury.js"></script>

    <script type="text/javascript">
        $(function () {

        })
    </script>

    <style type="text/css">
        #abc {
            margin-top: 10%;
            margin-left: 40%;
        }
        table{
            font-size: 20px;
        }

    </style>
</head>
<body>
<div>
<div id="abc">
    <form action="../mobile/login" method="post">
        <table>
            <tr>
                <td>用户名：</td>
                <td><input type="text" name="username"></td>
            </tr>
            <tr>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td>密码：</td>
                <td><input type="password" name="password"></td>
            </tr>
            <tr>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="登录">&nbsp;&nbsp;&nbsp;<label>${errMsg}</label></td>
            </tr>

        </table>
    </form>
</div>
</div>
</body>
</html>