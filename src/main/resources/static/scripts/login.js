/**
 * Created by lk on 2018/5/3.
 */

var constcode="";//验证码全局变量

var count = 120; //间隔函数，1秒执行
var curCount;//当前剩余秒数




function login(){
    var email = $("#email").val();
    var password = $("#password").val();
    var state= $("#rememberme")[0];
    var json={};
    json.email=email;
    json.password=password;
    if(state.checked){                     //checked用于判断rememberme对象（checkbox对象）有没有被选中
        json.rememberme=true;
    }else{
        json.rememberme=false;
    }

    var data=JSON.stringify(json);
    $.ajax({
        type:"POST",
        url:"/user/login",
        data:data,
        contentType:"application/json",
        success:function(data){
            var data1=data;
            if(data1.status==200){
                alert("登录成功！");
                window.location.href='index.html';
            }else{
                alert("登录失败,用户名或者密码错误！");
            }
        }
    });
}


function register(){
    var email = $("#email").val();
    var password = $("#password").val();
    var json={};
    json.email=email;
    json.password=password;
    var data=JSON.stringify(json);
    var code = $("#code").val();
    var code1=code.toString();
    if(constcode==code1&&!$.isEmptyObject(code1)){
        $.ajax({
            type:"POST",
            url:"/user/registeremail",
            data:data,
            contentType:"application/json",
            success:function(data){
                var data1=data;
                if(data1.status==200){

                    alert("注册成功！");
                    self.location='index.html';
                }else{
                    alert("注册失败！");
                }
                //显示数据
            }
        });
    }else{
        alert("验证码不正确！");
    }

}

function getcode(){
    var email = $("#email").val();
    var reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    var isValid=reg.test(email);
    if(!isValid){
        alert('提示:请输入有效的E_mail！');
        $("#email").val("");
        return;
    }
    var code="";
    for(i=0;i<6;i++)
    {
        code=code+(parseInt(Math.random()*10)).toString();
    }
    constcode=code;
    var json={};
    json.code=code;
    json.email=email;
    var data=JSON.stringify(json);


    curCount=count;
    //设置button效果，开始计时
    $("#btnSendCode").attr("disabled", "true");
    $("#btnSendCode").val("请在" + curCount + "秒内输入验证码");
    InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次
    //向后台发送处理数据

    $.ajax({
        type:"POST",
        url:"/user/captcha",
        data:data,
        contentType:"application/json",
        success:function(data){
            //显示数据
        }
    });


    function SetRemainTime() {
        if (curCount == 0) {
            window.clearInterval(InterValObj);//停止计时器
            $("#btnSendCode").removeAttr("disabled");//启用按钮
            $("#btnSendCode").val("重新获取验证码");
            constcode="";//清除验证码。如果不清除，过时间后，输入收到的验证码依然有效
        }
        else {
            curCount--;
            $("#btnSendCode").val("请在" + curCount + "秒内输入验证码");
        }
    };


}

//检查邮箱格式的正确性，以及邮箱是否被注册
function checkemail(){
    var email = $("#email").val();
    //对电子邮件的验证
    var reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    var isValid=reg.test(email);
    if(!isValid){
        alert('提示:请输入有效的E_mail！');
        $("#email").val("");
    }else{
        var email = $("#email").val();
        var json={};
        json.email=email;
        var data=JSON.stringify(json);
        $.ajax({
            type:"POST",
            url:"/user/check",
            data:data,
            contentType:"application/json",
            success:function(data){
                var data1=data;
                if(data1.data==true){
                    alert("邮箱已注册！");
                    $("#email").val("");
                }
            }
        })
    }
}

//检查两次输入的密码是否一致
function checkpassword(){
    var password = document.getElementById("password").value;
    var confirm_password = document.getElementById("confirm_password").value;

    if(password!=confirm_password){
            alert('提示:两次输入的密码不一致，请重新输入！');
    }
}

//找回密码时判断格式是否正确并检查是否有该邮箱注册的记录
function checkemailexistence(){
    var email = $("#email").val();
    //对电子邮件的验证
    var reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    var isValid=reg.test(email);
    if(!isValid){
        alert('提示:请输入有效的E_mail！');
        $("#email").val("");
    }else{
        var email = $("#email").val();
        var json={};
        json.email=email;
        var data=JSON.stringify(json);
        $.ajax({
            type:"POST",
            url:"/user/check",
            data:data,
            contentType:"application/json",
            success:function(data){
                var data1=data;
                if(data1.data==false){
                    alert("该邮箱尚未注册！");
                    $("#email").val("");
                }
            }
        })
    }
}

function retrievePassword(){
    var email = $("#email").val();
    var password = $("#new-password").val();
    var json={};
    json.email=email;
    json.password=password;
    var data=JSON.stringify(json);
    var code = $("#code").val();
    var code1=code.toString();
    if(constcode==code1&&!$.isEmptyObject(code1)){
        $.ajax({
            type:"POST",
            url:"/user/retrievePassword",
            data:data,
            contentType:"application/json",
            success:function(data){
                var data1=data;
                if(data1.status==200){

                    alert("修改密码成功！");
                    self.location='index.html';
                }else{
                    alert("修改密码失败！");
                }
                //显示数据
            }
        });
    }else{
        alert("验证码不正确！");
    }

}


