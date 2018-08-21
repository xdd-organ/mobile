<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script type="text/javascript" src="../mobile/stat/js/jquery/jqeury.js"></script>
    <!--<script type="text/javascript" src="../../../stat/js/jquery/jqeury.js"></script>-->

    <script type="text/javascript">
        $(function () {
            $("#btn").click(function () {
                var lock_no = $("#lock_no").val();
                var url = '../mobile/unLock?uid=' + lock_no;
                $.ajax({
                    type: 'get',
                    url: url,
                    success: function (result) {
                        if (result.indexOf('OK') != -1) {
                            $("#msg").html("开锁成功")
                        } else if (result.indexOf('NOT_FIND') != -1) {
                            $("#msg").html("锁未连接上服务器")
                        } else {
                            $("#msg").html(result)
                        }
                    }
                });
            });
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
        <table>
            <tr>
                <td>锁编号：</td>
                <td><input type="text" name="lock_no" id="lock_no">&nbsp;&nbsp;&nbsp;<label id="msg"></label></td>
            </tr>
            <tr>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td></td>
                <td><button id="btn">开锁</button></td>
            </tr>
        </table>

</div>
</div>
</body>
</html>