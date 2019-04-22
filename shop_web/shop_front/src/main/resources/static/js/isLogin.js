$(function () {
    $.ajax({
        url:"http://localhost:8084/sso/isLogin",
        dataType:"jsonp"
    })
})
function callback(data) {
    if(data=="0"){
        $("#login_text").html(" </b>[<a href=\"http://localhost:8084/sso/toLogin\">登录</a>]" +
            "                        [<a href=\"http://localhost:8084/sso/toRegister\">注册</a>]\n" +
            "                    </p>")
    }else{
        $("#login_text").html("<p>"+data+"您好，欢迎来到<b><a href=\"/\">ShopCZ商城</a>[<a href=\"http://localhost:8084/sso/exitLogin\">注销</a>]");
    }
}