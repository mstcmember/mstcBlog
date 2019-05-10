
var user;              //当前用户
var image = '';         //用户上传的头像
var file;                //图片文件
var userId;              //表示是谁的个人中心
var personalUser;         //表示是谁的个人中心
//我的信息------------------------------------------------------------------------------------------------------------
function submitPersonalMessage(){   //提交个人信息
    var json={};
    //nickname
    var inputNickname=document.getElementById("personalCenter-nickname");
    if(inputNickname!=null && document.getElementById("personalCenter-nickname").value!="")
        json.nickname=document.getElementById("personalCenter-nickname").value;
    //sex
    var sex=document.getElementsByName("el-radio-button__orig-radio");
    for(var i=0;i<sex.length;i++){
        if(sex[i].checked==true){
            json.sex=sex[i].value;
            break;
        }
    }
    //birthday
    if($('#personalCenter-date')!=null)
        json.birthday=$('#personalCenter-date').val();   //2018-08-09
    //school
    var inputSchool=document.getElementById("personalCenter-school");
    if(inputSchool!=null && document.getElementById("personalCenter-school").value!="")
        json.school=document.getElementById("personalCenter-school").value;
    //hobby
    var inputhobby=document.getElementById("personalCenter-hobby");
    if(inputhobby!=null && document.getElementById("personalCenter-hobby").value!="")
        json.hobby=document.getElementById("personalCenter-hobby").value;

    var data=JSON.stringify(json);

    if($.isEmptyObject(user)){
        alert("请先登录");
        return;
    }

    // if($.isEmptyObject(json.nickname)){
    //     alert("请先输入");
    //     return;
    // }

    $.ajax({
        type: "POST",
        url: "/user/personalCenter/editPersonalInformation",
        data: data,
        contentType: "application/json",
        error: function () {
            alert('smx失败 ');
        },
        success: function (data1) {
            if(data1.status==200){
                alert("修改成功");
                location.reload();
            }
            else{
                alert("修改失败");
            }
        }
    });
}

function onloadPersonalMessage(){

    var url = window.location.search;
    var loc = url.substring(url.lastIndexOf('personalCenterId=')+17, url.length);
    if(url.indexOf('personalCenterId=') == -1 ) {
        userId = url.substring(url.indexOf('userId=') + 7, url.length);
    }else{
        userId = url.substring(url.indexOf('userId=') + 7, url.lastIndexOf('&&personalCenterId='));
    }
    if(url.indexOf('userId=') == -1){
        window.location = "errorPage.html";
        return;
    }
    var json ={};
    json.userId = userId;
    var data=JSON.stringify(json);
    $.ajax({
        type:"POST",
        url:"/user/getPersonalCenterUser",
        data:data,
        contentType:"application/json",
        error : function(){
            alert('smx失败 ');
        },
        success:function(data1){
            if($.isEmptyObject(user)){
                alert("您还未登录！");
                window.location = "login.html";
                return;
            }
            window.personalUser = data1.data;
            if(user.id==userId) {  //进入自己的个人中心
                document.getElementById('security-list-3').style.display = "";
                document.getElementById('security-list-4').style.display = "";
                document.getElementById('security-list-5').style.display = "";
                document.getElementById('security-list-6').style.display = "";
                document.getElementById('security-list-7').style.display = "";
                for (var i = 0; i < 8; i++) {
                    if (i == loc)
                        document.getElementById('personalCenter-right' + i).style.display = "";
                    else
                        document.getElementById('personalCenter-right' + i).style.display = "none";
                    document.getElementById('personalCenter-right').style.display = "none";
                }
            }else{   //进入别人的个人中心
                document.getElementById('security-list-1').style.display = "";
                document.getElementById('security-list-2').style.display = "";
                document.getElementById('security-list-3').style.display = "none";
                document.getElementById('security-list-4').style.display = "none";
                document.getElementById('security-list-5').style.display = "none";
                document.getElementById('security-list-6').style.display = "none";
                document.getElementById('security-list-7').style.display = "";
                document.getElementById('security-nav-name-1').innerHTML = "Ta的信息";
                document.getElementById('security-nav-name-2').innerHTML = "Ta的头像";
                document.getElementById('security-right-title-text-0').innerHTML = "Ta的首页";
                document.getElementById('security-right-title-text-1').innerHTML = "Ta的信息";
                document.getElementById('security-right-title-text-2').innerHTML = "Ta的头像";
                document.getElementById('security-title').innerHTML = "Ta的个人中心";
                document.getElementById('security-nav-name').innerHTML = "Ta的首页";
                document.getElementById('el-input').innerHTML = "<span class=\"userinfo-useracount\" id=\"personalCenter-nickname\"> </span>";
                document.getElementById('personalCenter-nickname').innerHTML =personalUser.nickname;
                document.getElementById('el-radio-group').innerHTML = "<span class=\"userinfo-useracount\" id=\"user-my-sex\"> </span>";
                document.getElementById('personal-headerUrl').innerHTML = "<div data-v-15325571=\"\" class=\"img-preview-wrap\">\n" +
                    "                                <div data-v-15325571=\"\" class=\"pre-container\" id=\"pre-container\" >\n" +
                    "                                    <img data-v-15325571=\"\"  style=\"display: none;\">\n" +
                    "                                </div>\n" +
                    "                                <div data-v-15325571=\"\" class=\"pre-info\">当前头像</div>\n" +
                    "                            </div>";
                if(personalUser.sex==1){
                    document.getElementById('user-my-sex').innerHTML ="男";
                }else if(personalUser.sex==2){
                    document.getElementById('user-my-sex').innerHTML ="女";
                }else{
                    document.getElementById('user-my-sex').innerHTML ="保密";
                }
                document.getElementById('el-date-pick').innerHTML = "<span class=\"userinfo-useracount\" id=\"personalCenter-date\"> </span>";
                document.getElementById('personalCenter-date').innerHTML =personalUser.birthdayString;
                document.getElementById('el-input-school').innerHTML = "<span class=\"userinfo-useracount\" id=\"personalCenter-school\"> </span>";
                document.getElementById('personalCenter-school').innerHTML =personalUser.school;
                document.getElementById('el-textarea').innerHTML = "<span class=\"userinfo-useracount\" id=\"personalCenter-hobby\"> </span>";
                document.getElementById('personalCenter-hobby').innerHTML =personalUser.hobby;
                document.getElementById('save-button').style.display ="none";
                document.getElementById('header-button').style.display ="none";
                for (var i = 0; i < 3; i++) {
                    if (i == loc)
                        document.getElementById('personalCenter-right' + i).style.display = "";
                    else {
                        document.getElementById('personalCenter-right' + i).style.display = "none";
                        document.getElementById('personalCenter-right7').style.display = "none";
                    }
                    document.getElementById('personalCenter-right').style.display = "none";
                }
                if(loc==7){
                    document.getElementById('personalCenter-right7').style.display = "";
                }
            }



            if(loc==0){                                    // 首页
                if($.isEmptyObject(user)){
                    window.location.href="login.html";
                    return;
                }
                else{
                    onloadPersonalShow(1,userId);
                }
            }

            if (loc==1) {                                  // 我的信息
                if($.isEmptyObject(user)){
                    window.location.href="login.html";
                    return;
                }
                else {
                    var nickname = personalUser.nickname;
                    var id = personalUser.email;
                    var sex1 = personalUser.sex;
                    var birthday1 = personalUser.birthdayString;
                    var school = personalUser.school;
                    var hobby = personalUser.hobby;

                    if (personalUser != null) {
                        //nickname
                        document.getElementById("personalCenter-nickname").setAttribute("placeholder", nickname);
                        //id
                        document.getElementById("personalCenter-Id").innerHTML = id;
                        //sex
                        if (sex1 == 1)
                            var sex = "男";
                        else if (sex1 == 2)
                            sex = "女";
                        else
                            sex = "保密";
                        $(":radio[name='el-radio-button__orig-radio'][value='" + sex + "']").prop("checked", "checked");
                        //birthday
                        if (birthday1 != undefined) {
                            $('#personalCenter-date').val(birthday1);
                        }
                        //school
                        if (school != undefined)
                            document.getElementById("personalCenter-school").setAttribute("placeholder", school);
                        //hobby
                        if (hobby != undefined)
                            document.getElementById("personalCenter-hobby").setAttribute("placeholder", hobby);

                    }
                }
            }

            if(loc==2){                                  // 我的头像
                if($.isEmptyObject(user)){
                    window.location.href="login.html";
                    return;
                }
                else {
                    var headUrl = personalUser.headUrl;
                    // $("#face-g-avatar").attr("src",headUrl);
                    $("#pre-container").css("background-image", "url(" + headUrl + ")");

                    if(user.id==userId) {
                        document.getElementById("file_input").addEventListener("change", readImg, false);
                    }
                    // $('input[type="file"]').on('change',uploadImage);
                }
            }

            if(loc==3){                                  // 修改密码
                if($.isEmptyObject(user)){
                    window.location.href="login.html";
                    return;
                }
                else{

                }
            }

            if(loc==4){                                  // 黑名单管理
                if($.isEmptyObject(user)){
                    window.location.href="login.html";
                    return;
                }
                else{

                }
            }

            if(loc==5){                                  // 我的消息
                if($.isEmptyObject(user)){
                    window.location.href="login.html";
                    return;
                }
                else{
                    var json = {};
                    json.commentPage = 1;
                    var data = JSON.stringify(json);
                    if($.isEmptyObject(user)){
                        window.location.href="login.html";
                        return;
                    }
                    else {
                        $.ajax({
                            type: "POST",
                            url: "/message/personalCenter/getUserComment1",
                            data: data,
                            contentType: "application/json",
                            error: function () {
                                alert('smx失败 ');
                            },
                            success: function (data1) {
                                if(data1.status==999){
                                    alert(data1.msg);
                                }
                                else {
                                    var temp = {};
                                    temp.systemMessage = data1.data[1]
                                    // temp.commentActive = data1.data[2][0];
                                    // temp.commentBeforeArrow = data1.data[2][1];
                                    // temp.commentAfterArrow = data1.data[2][2];
                                    $("#systemMessage").tmpl(temp).appendTo("#list-group-comment");
                                        new Page({
                                            id: 'pagination',
                                            pageTotal: data1.data[0][0], //必填,总页数
                                            pageAmount: data1.data[0][1],  //每页多少条
                                            dataTotal: data1.data[0][2], //总共多少条数据
                                            curPage:1, //初始页码,不填默认为1
                                            pageSize: 5, //分页个数,不填默认为5
                                            showPageTotalFlag:true, //是否显示数据统计,不填默认不显示
                                            showSkipInputFlag:true, //是否支持跳转,不填默认不显示
                                            getPage: function (page) {
                                                //获取当前页数
                                                //console.log(page);
                                                document.getElementById('list-group-comment').innerHTML="";

                                                var json = {};
                                                json.commentPage = page;
                                                var data = JSON.stringify(json);
                                                $.ajax({
                                                    type: "POST",
                                                    url: "/message/personalCenter/getUserComment2",
                                                    data: data,
                                                    contentType: "application/json",
                                                    error: function () {
                                                        alert('smx失败 ');
                                                    },
                                                    success: function (data1) {
                                                        if(data1.status==999){
                                                            alert(data1.msg);
                                                        }
                                                        else {
                                                            var temp = {};
                                                            temp.systemMessage = data1.data;
                                                            // temp.commentActive = data1.data[2][0];
                                                            // temp.commentBeforeArrow = data1.data[2][1];
                                                            // temp.commentAfterArrow = data1.data[2][2];
                                                            $("#systemMessage").tmpl(temp).appendTo("#list-group-comment");
                                                        }
                                                    }
                                                })
                                                //////////////////////////////
                                            }
                                        });

                                }
                            }
                        })
                    }
                }
            }

            if(loc==6){                                  // 我的收藏
                if($.isEmptyObject(user)){
                    window.location.href="login.html";
                    return;
                }
                else{
                    onloadCollectionShow(1,userId);
                }
            }

            if(loc==7){                                  // 我的贡献值
                if($.isEmptyObject(user)){
                    window.location.href="login.html";
                    return;
                }
                else{
                    document.getElementById('personalCenter-gong-xian-du').innerHTML=personalUser.rankScore;
                    document.getElementById('personalCenter-pai-ming').innerHTML=personalUser.scoreRanking;
                }
            }
            document.getElementById('hidden').style.display='none';

        }


    })
}

//我的头像------------------------------------------------------------------------------------------------------------
function readImg() {
    file = this.files[0];
    if( file.size > 2*1024*1024 ){  //用size属性判断文件大小不能超过5M
        alert( "你上传的文件太大了！" )
    }
    else {
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = function (e) {
            document.getElementById("img-container").innerHTML = '<img width="180"  height="180" src="' + this.result + '" alt=""/>';
            image = this.result;
        }
    }
}

function uploadImage() {
    var formData = new FormData($("#upLoadForm")[0]);
    $.ajax({
        type:"POST",
        url: "/user/personalCenter/editPersonalHead",
        data: formData,
        dataType: "json",
        async: false,
        cache: false,
        contentType:false,
        processData: false,
        success: function(data){
            if(data.status==200){
                    alert("修改成功");
                    location.reload();
                }
                else{
                    alert("修改失败");
                }
            },
        error: function(err){
                alert('通信故障');
            }
        });
}

//我的消息------------------------------------------------------------------------------------------------------------
//-------评论---------------------------------------------------------------------------------
function personalCenter_comment(){
    document.getElementById('personal-center-comment').className="active";
    document.getElementById('personal-center-message').className=" ";
    document.getElementById('personal-center-system-message').className=" ";
    document.getElementById('list-group-comment').style.display = "";
    document.getElementById('list-group-message').style.display = "none";
    document.getElementById('list-group-system-message').style.display = "none";
}
function readMessage(id,entityType,entityId){
    var json={};
    json.commentId=id;
    var data=JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/message/personalCenter/readComment",
        data: data,
        contentType: "application/json",
        error: function () {
            alert('smx失败 ');
        },
        success: function (data1) {
            // alert(data1.msg);
            if (entityType==1)
                window.location="blogDetail.html?blogId="+entityId;
            else if (entityType==2)
                window.location="questionDetail.html?questionId="+entityId;
            else if (entityType==0)
                window.location="programmingDetail.html?programmingId="+entityId;
            else if (entityType==3)
                window.location="activityDetail.html?activityId="+entityId;
    }
    })
}
function commentChangePage(limit){
        var json = {};
        json.commentPage = limit;
        var data = JSON.stringify(json);
        $.ajax({
            type: "POST",
            url: "/message/personalCenter/getUserComment",
            data: data,
            contentType: "application/json",
            error: function () {
                alert('smx失败 ');
            },
            success: function (data1) {
                if (data1.status == 999) {
                    alert(data1.msg);
                }
                else {
                    // document.getElementById('list-group-comment').innerHTML="";
                    // var temp = {};
                    // temp.systemMessage = data1.data[1];
                    // temp.commentAmount = data1.data[0];
                    // temp.commentActive = data1.data[2][0];
                    // temp.commentBeforeArrow = data1.data[2][1];
                    // temp.commentAfterArrow = data1.data[2][2];
                    // $("#systemMessage").tmpl(temp).appendTo("#list-group-comment");
                    // window.onload = function () {
                    //      new Page({
                    //         id: 'pagination',
                    //         pageTotal: 50, //必填,总页数
                    //         pageAmount: 10,  //每页多少条
                    //         dataTotal: 500, //总共多少条数据
                    //         curPage:1, //初始页码,不填默认为1
                    //         pageSize: 6, //分页个数,不填默认为5
                    //         showPageTotalFlag: true, //是否显示数据统计,不填默认不显示
                    //         showSkipInputFlag:true, //是否支持跳转,不填默认不显示
                    //         getPage: function (page) {
                    //             //获取当前页数
                    //             console.log(page);
                    //         }
                    //     })
                    //
                    //     //如有问题可联系VX(base64): REo5ODc4OQ==
                    // }
                }
            }
        })
}
//----私信--------------------------------------------------------------------------------------
function personalCenter_message() {
    document.getElementById('personal-center-comment').className=" ";
    document.getElementById('personal-center-message').className="active";
    document.getElementById('personal-center-system-message').className=" ";
    document.getElementById('list-group-comment').style.display = "none";
    document.getElementById('list-group-message').style.display = "";
    document.getElementById('list-group-system-message').style.display = "none";
}


//---滑到底部加载--------------------------------------------------------------------------------



//加载个人中心的首页
function onloadPersonalShow(type,userId) {
    document.getElementById('hidden').style.display='flex';
    var json = {};
    json.type= type;
    json.start = 0;
    json.num = 10;
    json.userId = userId;
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/user/personalCenter/allPost",
        data: data,
        contentType: "application/json",
        error: function () {
            alert('加载技术贴失败 ');
        },
        success: function (data1) {
            document.getElementById('discuss_list').innerHTML = "";
            var temp ={};
            temp.question=data1.dataList;
            temp.type = type;
            $("#each_question").tmpl(temp).appendTo("#discuss_list");

            new Page({
                id: 'personal_show',
                pageTotal: Math.ceil(data1.msg/10), //必填,总页数
                pageAmount: 10,  //每页多少条
                dataTotal: data1.msg, //总共多少条数据
                curPage:1, //初始页码,不填默认为1
                pageSize: 5, //分页个数,不填默认为5
                showPageTotalFlag:true, //是否显示数据统计,不填默认不显示
                showSkipInputFlag:true, //是否支持跳转,不填默认不显示
                getPage: function (page) {
                    //获取当前页数
                    document.getElementById('discuss_list').innerHTML="";
                    var json = {};
                    json.type= type;
                    json.start = (page-1)*10;
                    json.num = 10;
                    json.userId = userId;
                    var data = JSON.stringify(json);
                    $.ajax({
                        type: "POST",
                        url: "/user/personalCenter/allPost",
                        data: data,
                        contentType: "application/json",
                        error: function () {
                            alert('加载分页技术贴失败 ');
                        },
                        success: function (data1) {
                            if(data1.status==999){
                                //alert(data1.msg);
                            }
                            else {
                                var temp = {};
                                temp.question=data1.dataList;
                                temp.type = type;
                                $("#each_question").tmpl(temp).appendTo("#discuss_list");
                            }
                            document.getElementById('hidden').style.display='none';
                        }
                    })
                    //////////////////////////////
                }
            });
            document.getElementById('hidden').style.display='none';
        }
    });
}

function personalChooseKind(type) {
    document.getElementById('personal-search-kind-2').className="";
    document.getElementById('personal-search-kind-3').className="";
    document.getElementById('personal-search-kind-4').className="";
    document.getElementById('personal-search-kind-5').className="";
    document.getElementById('personal-search-kind-'+(type+2)).className="active";
    onloadPersonalShow(type,userId);
}
function collectionChooseKind(type) {
    document.getElementById('collection-search-kind-2').className="";
    document.getElementById('collection-search-kind-3').className="";
    document.getElementById('collection-search-kind-4').className="";
    document.getElementById('collection-search-kind-5').className="";
    document.getElementById('collection-search-kind-'+(type+2)).className="active";
    onloadCollectionShow(type,userId);
}
function onloadCollectionShow(type,userId) {
    document.getElementById('hidden').style.display='flex';
    var json = {};
    json.type= type;
    json.start = 0;
    json.num = 10;
    json.userId = userId;
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/user/personalCenter/allCollection",
        data: data,
        contentType: "application/json",
        error: function () {
            alert('加载技术贴失败 ');
        },
        success: function (data1) {
            document.getElementById('collection_list').innerHTML = "";
            var temp ={};
            temp.question=data1.dataList;
            $("#each_collection").tmpl(temp).appendTo("#collection_list");

            new Page({
                id: 'collection_show',
                pageTotal: Math.ceil(data1.msg/10), //必填,总页数
                pageAmount: 10,  //每页多少条
                dataTotal: data1.msg, //总共多少条数据
                curPage:1, //初始页码,不填默认为1
                pageSize: 5, //分页个数,不填默认为5
                showPageTotalFlag:true, //是否显示数据统计,不填默认不显示
                showSkipInputFlag:true, //是否支持跳转,不填默认不显示
                getPage: function (page) {
                    //获取当前页数
                    document.getElementById('collection_list').innerHTML="";
                    var json = {};
                    json.type= type;
                    json.start = (page-1)*10;
                    json.num = 10;
                    json.userId = userId;
                    var data = JSON.stringify(json);
                    $.ajax({
                        type: "POST",
                        url: "/user/personalCenter/allCollection",
                        data: data,
                        contentType: "application/json",
                        error: function () {
                            alert('加载分页技术贴失败 ');
                        },
                        success: function (data1) {
                            if(data1.status==999){
                                //alert(data1.msg);
                            }
                            else {
                                var temp = {};
                                temp.question=data1.dataList;
                                $("#each_collection").tmpl(temp).appendTo("#collection_list");
                            }
                            document.getElementById('hidden').style.display='none';
                        }
                    })
                    //////////////////////////////
                }
            });
            document.getElementById('hidden').style.display='none';
        }
    });
}

function turnPage(id) {
    window.location = "personalCenter.html?userId="+userId+"&&personalCenterId="+id;
}
