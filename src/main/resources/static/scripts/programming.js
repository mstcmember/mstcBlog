var user;                     //当前用户
var questionCommentId;            //当前评论帖id

function publicProgramming(){  //发布技术帖
    if ($.isEmptyObject(user)) {
        alert("您还未登录，请先登录再发表博客！");
        return;
    }
    var title=$("#title").val();
    var content = document.getElementById("comment-content").value;
    if(!(title&&content)) {alert("标题与内容不能为空!"); return;}
    var keyword1=$("#keyWord1").val();
    var keyword2=$("#keyWord2").val();
    var keyword3=$("#keyWord3").val();
    var keyword=keyword1+","+keyword2+","+keyword3;
    var flag=$("#blog_flag option:selected").val();
    var status=0;
    switch(status)
    {case "是":status=0;break;
        case "否":status=1;break;}
    //console.log(status);
    var json={};
    json.title=title;
    json.content=content;
    // json.keyword=keyword;
    json.flag=flag;
    json.status=status;
    json.userId=user.id;
    var data=JSON.stringify(json);
    $.ajax({                   //获取当前的用户
        type: "POST",
        url: "/programming/public",
        data: data,
        contentType: "application/json",
        error: function () {
            alert('smx失败 ');
        },
        success: function (data) {
            if(data.status==200){
                alert('发布成功！');
                window.location.href="/programming.html";
            }else{
                alert('发布失败！')
            }

        }
    })
}

function cancelPublicProgramming() {
    document.getElementById("zh-add-question-form").style.display = "none";
    document.getElementById("display-question").style.display = "";
}

function onclickPublic() {
    document.getElementById("zh-add-question-form").style.display = "";
    document.getElementById("display-question").style.display = "none";
}


function publicQuestionComment() {       //发布问题评论
    var content = $("#question-comment-content").val();
    if ($.isEmptyObject(user)) {
        alert("您还未登录，请先登录再发表评论！");
        return;
    }
    var json = {};
    var url = window.location.search;
    var loc = url.substring(url.lastIndexOf('=') + 1, url.length);
    json.entityId = loc;
    json.entityType = 0;
    json.userId = user.id;
    json.content = content;
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/programming/publicProgrammingComment",
        data: data,
        contentType: "application/json",
        success: function (data) {
            if (data.status == 200) {
                alert("发表评论成功！");
                window.location.href = 'programmingDetail.html?programmingId=' + loc;
            } else {
                alert("评论失败！");
            }
        }
    });

}

//点赞点踩
function addQuestionCommentLike(id) {
    if ($.isEmptyObject(user)) {
        alert("您还未登录，请先登录再点赞！");
        return;
    }
    var str = id;
    var isLike = 0;
    if (str.match("Like")) {
        isLike = 0;
    }
    if (str.match("Dislike")) {
        isLike = 1;
    }
    var entityId = str.substring(str.lastIndexOf('_') + 1, str.length);
    var json = {};
    json.isLike = isLike;
    json.entityType = 1;//1代表对评论点赞
    json.entityId = entityId;
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/programming/addProgrammingLike",
        data: data,
        contentType: "application/json",
        success: function (data) {
            if (data.status == 200) {
                if(str.match("Reply")) {
                    document.getElementById("questionReplyLike" + entityId).innerHTML=data.data;
                    if (isLike == 0) {    //显示点赞
                        document.getElementById("questionCommentReply2Like_" + entityId).style.background = "#8c8c8c";
                    }
                    if (isLike == 1) {    //显示点赞
                        document.getElementById("questionCommentReply2Like_" + entityId).style.background = "white";
                    }
                }else{
                    document.getElementById("questionLike" + entityId).innerHTML=data.data;
                    if (isLike == 0) {    //显示点赞
                        document.getElementById("questionComment2Like_" + entityId).style.background = "#8c8c8c";
                    }
                    if (isLike == 1) {    //显示点赞
                        document.getElementById("questionComment2Like_" + entityId).style.background = "white";
                    }
                }
            } else {
                alert("点赞失败！");
            }
        }
    });

}

//点赞点踩
function addQuestionLike(id) {
    var url = window.location.search;
    var entityId = url.substring(url.lastIndexOf('=') + 1, url.length);
    if ($.isEmptyObject(user)) {
        alert("您还未登录，请先登录再点赞！");
        return;
    }
    var str = id;
    var isLike = 0;
    if (str.match("Like")) {
        isLike = 0;
    }
    if (str.match("Dislike")) {
        isLike = 1;
    }
    var json = {};
    json.isLike = isLike;
    json.entityType = 0;//0代表对问题点赞
    json.entityId = entityId;
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/programming/addProgrammingLike",
        data: data,
        contentType: "application/json",
        success: function (data) {
            if (data.status == 200) {
                document.getElementById("questionLikeCount").innerHTML=data.data;
                if (isLike == 0) {    //显示点赞
                    document.getElementById("questionLike").style.background = "#8c8c8c";
                }else{
                    document.getElementById("questionLike").style.background = "#ffffff";
                }
            } else {
                alert("点赞失败！");
            }
        }
    });

}

// var questionPageId;       //当前页数的id
var questionCommentNum;   //技术帖总数
function aquireQuestion(id) {
    var page = document.getElementById(id).innerHTML;
    var start = (page-1)*6;
    var json = {};
    json.start = start;
    json.num = 6;
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/programming/onLoad",
        data: data,
        contentType: "application/json",
        error: function () {
            alert('smx失败 ');
        },
        success: function (data1) {
            document.getElementById('middle').innerHTML = "";
            var questionList = Array();
            questionList = data1.data;
            var temp ={};
            temp.question=data1.data;
            temp.currentPage = id;
            temp.questionCommentNum=questionCommentNum;
            temp.totalPage = Math.ceil(questionCommentNum/6);
            $("#each_question").tmpl(temp).appendTo("#middle");
            var num = id.substring(id.lastIndexOf('-') + 1, id.length);
            document.getElementById("question-page-"+num).className = "active";
        }
    });
}

function onloadProgramming() {
    var json = {};
    var url = window.location.search;
    var flag = url.substring(url.lastIndexOf('=') + 1, url.length);
    if($.isEmptyObject(flag))
    {
        flag = 0;
    }
    json.flag= flag;
    json.start = 0;
    json.num = 10;
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/programming/onLoad",
        data: data,
        contentType: "application/json",
        error: function () {
            alert('smx失败 ');
        },
        success: function (data1) {
            document.getElementById('discuss_list').innerHTML = "";
            var temp ={};
            temp.question=data1.data;
            $("#each_question").tmpl(temp).appendTo("#discuss_list");

            new Page({
                id: 'pagination',
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
                    var url = window.location.search;
                    var flag = url.substring(url.lastIndexOf('=') + 1, url.length);
                    if($.isEmptyObject(flag))
                    {
                        flag = 0;
                    }
                    var json = {};
                    json.flag= flag;
                    json.start = (page-1)*10;
                    json.num = 10;
                    var data = JSON.stringify(json);
                    $.ajax({
                        type: "POST",
                        url: "/programming/onLoad",
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
                                temp.question=data1.data;
                                $("#each_question").tmpl(temp).appendTo("#discuss_list");
                                $('.left li').eq(flag).addClass('active');
                                if($(".middle").height()>430)
                                    $("#footer").css('position','static');
                                else $("#footer").css('position','absolute');
                            }
                        }
                    })
                    //////////////////////////////
                }
            });
            $('.left li').eq(flag).addClass('active');
            if($(".middle").height()>430)
                $("#footer").css('position','static');
            else $("#footer").css('position','absolute');
        }
    });
}

//加载热门博客
function onloadHotProgramming() {
    var json ={};
    var begin=0;
    var end=4;
    json.begin=begin;
    json.end=end;
    var data=JSON.stringify(json);
    $.ajax({                   //获取当前的用户
        type: "POST",
        url: "/programming/onLoadHot",
        data: data,
        contentType: "application/json",
        error: function () {
            alert('加载热门编程题请求失败');
        },
        success: function (data1) {
            blogHotList = data1.data;
            if(!$.isEmptyObject(blogHotList)) {
             var obj_lis = document.getElementById("hotBlog_content").getElementsByTagName("a");
                 var random=[];
                 while(random.length<=obj_lis.length){
                 var rand=rnd(0, blogHotList.length - 1);
                 if(random.indexOf(rand)==-1) random.push(rand);
                  }
                 for (i = 0; i < obj_lis.length; i++) {
                     var b=random[i];
                     obj_lis[i].innerHTML = blogHotList[b].title;
                     obj_lis[i].href = "/programmingDetail.html?programmingId=" + blogHotList[b].id;
                 }
            }
            document.getElementById('hidden').style.display='none';
        }
    });
}
//获得随机数
function rnd(n, m){
    var random = Math.floor(Math.random()*(m-n+1)+n);
    return random;
}
//热门推荐换一换
function changeHot(){
    var obj_lis = document.getElementById("hotBlog_content").getElementsByTagName("a");
     var random1=[];
            while(random1.length<=obj_lis.length){
            var rand=rnd(0, blogHotList.length - 1);
            if(random1.indexOf(rand)==-1) random1.push(rand);
            }
            for (i = 0; i < obj_lis.length; i++) {
            var b=random1[i];
            obj_lis[i].innerHTML = blogHotList[b].title;
            obj_lis[i].href = "/programmingDetail.html?programmingId=" + blogHotList[b].id;
        }
}

var programmingDetail;
function onloadProgrammingDetail() {      //加载问题详情页
    var url = window.location.search;
    var loc = url.substring(url.lastIndexOf('=') + 1, url.length);
    var json = {};
    json.programmingId = loc;
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/programming/onLoadDetail",
        data: data,
        contentType: "application/json",
        error: function () {
            alert("ajx失败!");
        },
        success: function (data) {
            if(data.status==404){
                window.location="errorPage.html"
            }
            programmingDetail = data.data;
            var question = data.data;
            var text=question.ideas;
            $("#question-detail-content").val(text);//将需要转换的内容加到转换后展示容器的textarea隐藏标签中

            //转换开始,第一个参数是上面的div的id
            editormd.markdownToHTML("question-content", {
                htmlDecode: true,
                emoji: true,
                taskList: true,
                tocm: true,
                tex: true, // 默认不解析
                flowChart: true, // 默认不解析
                sequenceDiagram: true, // 默认不解析
                codeFold: true
            });
            $(".question-content").find("img").each(function () {
                $(this).css("width", "300px");
                $(this).css("margin-right", "10px");
            });
            document.getElementById('question-detail-title').innerHTML = question.title;
            document.getElementById('question-detail-time').innerHTML = question.createTime;
            document.getElementById("question-author-headUrl").src = question.headUrl;
            document.getElementById("public-author").href = "personalCenter.html?userId="+question.userId;
            document.getElementById("question-author-name").href = "personalCenter.html?userId="+question.userId;
            if(!$.isEmptyObject(user)) {
                document.getElementById("comment-avatar-headUrl").src = user.headUrl;
            }
            document.getElementById('question-author-name').innerHTML = question.nickname;
            document.getElementById('comment-count').innerHTML = question.commentCount;
            document.getElementById('questionLikeCount').innerHTML = question.questionLikeCount;
            if(question.isLike==1){
                document.getElementById("questionLike").style.background = "#8c8c8c";
            }
            if(question.isCollection==1){
                document.getElementById('isCollection').className = "glyphicon glyphicon-heart";
                document.getElementById('isCollection').innerHTML = "已收藏";
            }
            if(user!=null){
                if(user.flag !=0){
                    document.getElementById('IsTopBlog').style.display = "";
                    document.getElementById('uploadTest').style.display = "";
                }}
            if(question.isTop==1){
                document.getElementById('isTopText').innerHTML = "取消置顶";
            }
            if(!$.isEmptyObject(user)&&(user.id == question.userId||user.flag !=0)){
                document.getElementById('deleteBlog').style.display = "";
            }

        }
    });
}

var questionCommentCount;
var questionCommentNum;
function aquireQuestionComment(start) {   //获取评论（从第start条开始向后获取10条）
    var url = window.location.search;
    var loc = url.substring(url.lastIndexOf('=') + 1, url.length);
    var json = {};
    json.start = start;
    json.num = 6;
    json.entityId = loc;
    json.entityType = 0;
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/programming/programmingComment/onLoad",
        data: data,
        contentType: "application/json",
        error: function () {
            alert("ajx失败!");
        },
        success: function (data) {
            questionCommentNum = data.msg;
            var questionCommentList = data.data;
            var temp ={};
            temp.questionComment=data.data;
            $("#each_question_comment").tmpl(temp).appendTo("#question-comment-list");

            var i =0;
            if($.isEmptyObject(questionCommentList)){
                document.getElementById('hidden').style.display='none';
                return;
            }
            for(i=0;i<questionCommentList.length;i++) {
                var text=questionCommentList[i].content;
                $("#appendTest"+questionCommentList[i].id).val(text);//将需要转换的内容加到转换后展示容器的textarea隐藏标签中

                //转换开始,第一个参数是上面的div的id
                editormd.markdownToHTML("testEditorMdview"+questionCommentList[i].id, {
                    htmlDecode: "style,script,iframe", //可以过滤标签解码
                    // emoji: true,
                    // taskList: true,
                    // tex: true,               // 默认不解析
                    // flowChart: true,         // 默认不解析
                    // sequenceDiagram: true,  // 默认不解析
                });
                var questionCommentReplyList = questionCommentList[i].questionCommentReplyList;
                for(var j=0;j<questionCommentReplyList.length;j++){
                    var replyText=questionCommentReplyList[j].content;
                    $("#appendTestReply"+questionCommentReplyList[j].id).val(replyText);//将需要转换的内容加到转换后展示容器的textarea隐藏标签中

                    //转换开始,第一个参数是上面的div的id
                    editormd.markdownToHTML("testEditorMdviewReply"+questionCommentReplyList[j].id, {
                        htmlDecode: "style,script,iframe", //可以过滤标签解码
                        // emoji: true,
                        // taskList: true,
                        // tex: true,               // 默认不解析
                        // flowChart: true,         // 默认不解析
                        // sequenceDiagram: true,  // 默认不解析
                    });
                }
            }
            document.getElementById('hidden').style.display='none';


        }
    });
}

function onloadProgrammingComment() {      //加载问题评论
    aquireQuestionComment(0);             //加载时先获取10条评论
    questionCommentCount=6;
}

function publishComment(){
    var content = document.getElementById("comment-content").value;

    if ($.isEmptyObject(user)) {
        alert("您还未登录，请先登录再发表评论！");
        return;
    }
    var json = {};
    var url = window.location.search;
    var loc = url.substring(url.lastIndexOf('=') + 1, url.length);
    json.entityId = loc;
    json.entityType = 0;
    json.userId = user.id;
    json.content = content;
    json.toCommentId = 0;
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/programming/publicProgrammingComment",
        data: data,
        contentType: "application/json",
        success: function (data) {
            if (data.status == 200) {
                window.location.href = 'programmingDetail.html?programmingId=' + loc;
            } else {
                alert("评论失败！");
            }
        }
    });

}
function publishReply(){
    var content = document.getElementById("comment-content").value;
    if ($.isEmptyObject(user)) {
        alert("您还未登录，请先登录再发表评论！");
        return;
    }
    if($.isEmptyObject(content)){
        alert("内容不能为空！");
        return;
    }
    var url = window.location.search;
    var json = {};
    if(!url.match("toCommentId")) {
        var id = url.substring(url.lastIndexOf('=') + 1, url.length);
        json.toCommentId=0;
    }else{
        var id = url.substring(url.indexOf('=') + 1, url.indexOf('&'));
        var toId = url.substring(url.lastIndexOf('=') + 1, url.length);
        json.toCommentId=toId;
    }
    json.entityId = id;
    json.entityType = 1;//1表示对评论的评论
    json.userId = user.id;
    json.content = content;
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/programming/publicProgrammingComment",
        data: data,
        contentType: "application/json",
        success: function (data) {
            if (data.status == 200) {
                alert("发表评论成功！");
                self.location=document.referrer;
            } else {
                alert("评论失败！");
            }
        }
    });

}

function ConcealReadQuestionCommentReply(id){
    var CommentId = id.substring(id.lastIndexOf('-') + 1, id.length);
    document.getElementById('questionComment'+CommentId+'-reply').style.display = 'none';
    document.getElementById('displayReply-'+CommentId).style.display = '';
}

function readQuestionCommentReply(id) {
    var CommentId = id.substring(id.lastIndexOf('-') + 1, id.length);
    document.getElementById('questionComment'+CommentId+'-reply').style.display = '';
    document.getElementById('displayReply-'+CommentId).style.display = 'none';
}

function  addQuestionCollection(){
    if ($.isEmptyObject(user)) {
        alert("您还未登录，请先登录再收藏！");
        return;
    }
    var json = {};
    var url = window.location.search;
    var loc = url.substring(url.lastIndexOf('=') + 1, url.length);
    json.entityId = loc;
    json.entityType = 0; //0代表算法编程题
    json.userId = user.id;
    var data = JSON.stringify(json);
    if(document.getElementById('isCollection').className == "glyphicon glyphicon-heart-empty") {
        $.ajax({
            type: "POST",
            url: "/collection/add",
            data: data,
            contentType: "application/json",
            success: function (data) {
                if (data.status == 200) {
                    alert("收藏成功！");
                    document.getElementById('isCollection').className = "glyphicon glyphicon-heart";
                    document.getElementById('isCollection').innerHTML = "已收藏";

                } else {
                    alert("收藏失败！");
                }
            }
        });
    }else{
        $.ajax({
            type: "POST",
            url: "/collection/cancel",
            data: data,
            contentType: "application/json",
            success: function (data) {
                if (data.status == 200) {
                    alert("取消收藏成功！");
                    document.getElementById('isCollection').className = "glyphicon glyphicon-heart-empty";
                    document.getElementById('isCollection').innerHTML = "收藏";

                } else {
                    alert("取消收藏失败！");
                }
            }
        });
    }
}

//删除算法编程
function deleteProgramming(type,id,owerId){
if(confirm("确认删除？")){
    var json = {};
    json.entityType = type; //0代表问题,1代表评论
    if(type==0){
        var url = window.location.search;
        var loc = url.substring(url.lastIndexOf('=') + 1, url.length);
        json.entityId = loc;
    }else{
        json.entityId = id.substring(id.lastIndexOf('-') + 1, id.length);
    }
    json.userId = user.id;//操作人的id（有可能是用户自己，也有可能是管理员）
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/programming/deleteProgramming",
        data: data,
        contentType: "application/json",
        success: function (data) {
            if (data.status == 200) {
                alert("删除成功！");
                window.location.reload();
            } else {
                alert("删除失败！");
            }
        }
    });
    }
}

//下载编程题测试代码
function jumpToTest(){
    window.open( programmingDetail.answer);
}

//设置编程题是否置顶
function  IsTopProgramming(){
    var json = {};
    var url = window.location.search;
    var loc = url.substring(url.lastIndexOf('=') + 1, url.length);
    json.entityId = loc;
    if(document.getElementById('isTopText').innerHTML == "置顶")
        json.topStatus = 1;//1表示需要设置为置顶
    if(document.getElementById('isTopText').innerHTML == "取消置顶")
        json.topStatus = 0;//0表示需要设为不置顶
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/programming/isTop",
        data: data,
        contentType: "application/json",
        success: function (data) {
            if (data.status == 200 && json.topStatus==1) {
                alert("置顶成功！");
                document.getElementById('isTopText').innerHTML = "取消置顶";
            } else if(data.status == 200 && json.topStatus==0){
                alert("取消置顶成功！");
                document.getElementById('isTopText').innerHTML = "置顶";
            } else alert("操作失败！");
        }
    });
}


function ajax_function() {
    // alert("自定义事件内容");
    if(questionCommentCount<questionCommentNum) {
        aquireQuestionComment(questionCommentCount);
        questionCommentCount = questionCommentCount + 6;
    }
}

var timeoutInt;   // 要保证最后要运行一次


//文档高度
function getDocumentTop() {
    var scrollTop =  0, bodyScrollTop = 0, documentScrollTop = 0;
    if (document.body) {
        bodyScrollTop = document.body.scrollTop;
    }
    if (document.documentElement) {
        documentScrollTop = document.documentElement.scrollTop;
    }
    scrollTop = (bodyScrollTop - documentScrollTop > 0) ? bodyScrollTop : documentScrollTop;
    console.log("scrollTop:"+scrollTop);
    return scrollTop;
}

//可视窗口高度
function getWindowHeight() {
    var windowHeight = 0;
    if (document.compatMode == "CSS1Compat") {
        windowHeight = document.documentElement.clientHeight;
    } else {
        windowHeight = document.body.clientHeight;
    }
    console.log("windowHeight:"+windowHeight);
    return windowHeight;
}

//滚动条滚动高度
function getScrollHeight() {
    var scrollHeight = 0, bodyScrollHeight = 0, documentScrollHeight = 0;
    if (document.body) {
        bodyScrollHeight = document.body.scrollHeight;
    }
    if (document.documentElement) {
        documentScrollHeight = document.documentElement.scrollHeight;
    }
    scrollHeight = (bodyScrollHeight - documentScrollHeight > 0) ? bodyScrollHeight : documentScrollHeight;
    console.log("scrollHeight:"+scrollHeight);
    return scrollHeight;
}
