var user;                     //当前用户
function onloadHeader(){
    var json ={};
    json.id = 1;
    var data=JSON.stringify(json);
    $.ajax({                   //获取当前的用户
        type:"POST",
        url:"/user/getUser",
        data:data,
        contentType:"application/json",
        error : function(){
            alert('加载当前用户失败 ');
        },
        success:function(data1){
            window.user = data1.data;
            var userId =-1;
            var userName = '';
            var headUrl = '';
            if(user!=null){
                userId = window.user.id;
                userName = window.user.nickname;
                headUrl =window.user.headUrl;
            }
            if(userId==-1)  //用户未登录
            {
                document.getElementById('reg').style.display ="";
            }
            else
            {
                document.getElementById('message').style.display ="";
                document.getElementById('jpg').src=headUrl;
                document.getElementById('name').text=userName;
                var url=window.location.href;
                if(url.indexOf("index.html")!=-1)
                {document.getElementById('publicName').style.display ="none";
                    document.getElementById('public_jpg').style.display ="none";}
                if(url.indexOf("blog.html")!=-1)
                {document.getElementById('public_jpg').style.display ="inline";
                    $("#publicName").text("写博客");
                    $("#publicName").attr("href","/pubBlog.html");
                }
                if(url.indexOf("question.html")!=-1)
                {document.getElementById('public_jpg').style.display ="inline";
                    $("#publicName").text("写技术帖");
                    $("#publicName").attr("href","/pubQuestion.html");
                }
                if(url.indexOf("programming.html")!=-1)
                {document.getElementById('public_jpg').style.display ="inline";
                    $("#publicName").text("写编程题");
                    $("#publicName").attr("href","/pubProgramming.html");
                }
                if(user!=null){
                     if(url.indexOf("activity.html")!=-1 && user.flag!=0)
                        {document.getElementById('public_jpg').style.display ="inline";
                        $("#publicName").text("发布活动");
                        $("#publicName").attr("href","/pubActivity.html");
                        }
                        }
            }

        }
    });
}
function logout() {
    if(confirm("确认退出？")){
        var json ={};
        json.id = 1;
        var data=JSON.stringify(json);
        $.ajax({                   //获取当前的用户
            type: "POST",
            url: "/user/logout",
            data: data,
            contentType: "application/json",
            error: function () {
                alert('注销登陆失败 ');
            },
            success: function (data) {
                if(data.status==200){
                    delAllCookie();
                    location.reload(true);
                }else{
                    alert('您还没有登录！')
                }

            }
        })
    }
}

function getCookie($name){
    var data=document.cookie;
    var dataArray=data.split("; ");
    for(var i=0;i<dataArray.length;i++){
        var varName=dataArray[i].split("=");
        if(varName[0]==$name){
            return decodeURI(varName[1]);
        }

    }
}
//删除cookie中所有定变量函数
function delAllCookie(){
    var myDate=new Date();
    myDate.setTime(-1000);//设置时间
    var data=document.cookie;
    var dataArray=data.split("; ");
    for(var i=0;i<dataArray.length;i++){
        var varName=dataArray[i].split("=");
        document.cookie=varName[0]+"=''; expires="+myDate.toGMTString();
    }

}

function search() {
    var keyword = document.getElementById("search-keyword").value;
    window.location = "searchResult.html?keyword="+keyword+"&type=-1";
}

function toPersonalCenter(){
    window.location = "personalCenter.html?userId="+user.id;
}



