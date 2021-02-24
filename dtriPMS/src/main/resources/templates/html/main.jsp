<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>DTR_PMS(製程系統)</title>
<script src="./thirdparty/jquery-3.5.1/jquery.min.js"></script>
<script src="./thirdparty/jquery-ui-1.12.1/jquery-ui.min.js"></script>
<script src="./thirdparty/bootstrap-4.6.0-dist/js/bootstrap.min.js"></script>
<script src="./thirdparty/chart-2.9.4-dist/js/Chart.min.js"></script>
<script src="./thirdparty/js/vue-2.6.11.min.js"></script>

<link rel="stylesheet" href="./thirdparty/jquery-ui-1.12.1/jquery-ui.min.css" />
<link rel="stylesheet" href="./thirdparty/bootstrap-4.6.0-dist/css/bootstrap.min.css" />
<link rel="stylesheet" href="./thirdparty/bootstrap-1.18.2-table-master-dist/css/bootstrap-table.min.css" />
<link rel="stylesheet" href="./thirdparty/chart-2.9.4-dist/css/Chart.min.css" />

</head>
<body id="main">


</body>
<script type="text/javascript">
	//各類模型 Vue 位置(需註冊)
var templateHeader = "";
var templateNav = "";
var templateBody = "";
var templateFooter = "";
//控制端
var main = new Vue({
    el: "#main",
    data: {
        //每次回傳資料
       /* allData: ${ resp_content },*/
        //登入存入資料
        loginData: "",
        //各類模型 資料
        contentData: "",
        //Ajax 傳輸模組
        ajaxCell: {
            url: "",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "JSON",
            data: "",
        },
        //倒數計時
        countDownId: "",
        countDownSecond: 300
    },
    created: function () {
        //初始化
        console.log(this.allData);
        this.contentData = this.allData["r_content"];
        this.loginData = this.allData["r_content"]["longinInfo"];
        //取得模板
        $("#header").load(this.contentData["template"]["header"]);
        $("#nav").load(this.contentData["template"]["nav"]);
        $("#body").load(this.contentData["template"]["body"]);
        $("#footer").load(this.contentData["template"]["footer"]);
        //啟動計時
        this.timeOut(true);
    },
    methods: {
        ajaxSend: function (url, dataSend) {
            //進入倒數中
            clearInterval(this.countDownId);
            this.countDownSecond = 800;
            main.timeOut(true);
            //進入運作中
            main.loading(true);
            //排除空白
            var data = dataSend.replace(/\s+/g, "");
            $.ajax({
                url: url,
                type: this.ajaxCell.type,
                contentType: this.ajaxCell.contentType,
                dataType: this.ajaxCell.dataType,
                data: data,
                success: function (event) {
                    main.allData = event;
                    main.contentData = event.r_content;
                    main.alertshow(true, " 結果->" + event.r_message);
                    //模組 轉跳 畫面
                    console.log(event.r_cellBackName + " : to success");
                    //固定回傳模組
                    if (event.r_cellBackName != null) {
                        switch (event.r_cellBackName) {
                            case "navAfter":
                                templateNav["navAfter"](event);
                                break;
                            case "bodyAfter":
                                templateBody["bodyAfter"](event);
                                break;
                            case "headerAfter":
                                templateHeader["headerAfter"](event);
                                break;
                            case "footerAfter":
                                templateFooter["footerAfter"](event);
                                break;
                        }
                    }
                    main.loading(false);
                },
                error: function (event) {
                    main.alertshow(true, "Something to fail");
                    console.log("to index fail:" + event);
                    if (event.status == 200) {
                        //沒有session 拒絕時
                        window.location.replace("login.jsp");
                    }
                    main.loading(false);
                },
            });
        },
        alertshow: function (open, message) {
            if (open) {
                $("#alert_message").text("資訊: " + message);
                $(".alert").addClass("show");
                setTimeout(function () {
                    $(".alert").removeClass("show");
                    $("#alert_message").text("");
                }, 8000);
            }
        },
        //timeOut 到計時 登出(提醒失效)
        countDown: function () {
            this.countDownSecond -= 1;
            if (this.countDownSecond == 60) {
                $("#countDown").removeClass("d-none");
            }
            if (this.countDownSecond < 0) {
                clearInterval(this.countDownId);
                $("#countDown").addClass("d-none");
                templateNav.signout();
            }
            $("#timeOutmsg h2").text("程序登出中....請點我繼續..." + this.countDownSecond);
            //console.log("倒數登出 : "+this.countDownSecond);
        },
        timeOut: function (check) {
            if (check) {
                //開始計時
                this.countDownId = setInterval(this.countDown, 1000); //每秒執行一次，賦值
            } else {
                //啟動計時
                clearInterval(this.countDownId);
                this.countDownSecond = 800;
                $("#countDown").addClass("d-none");
                //觸發紀錄
                var url = "./sessionUpdate.do";
                var data = JSON.stringify({
                    data: {
                        datetime: "",
                        action: "R",
                        whoami: main.loginData.account,
                        cellBackName: "",
                        cellBackOrder: "",
                        content: "",
                    },
                });
                main.ajaxSend(url, data);
            }
        },
        //程序運作中~
        loading: function (open) {
            if (open) {
                $("#loading").removeClass("d-none");
            } else {
                $("#loading").addClass("d-none");
            }
        },
    }
});

</script>
</html>

