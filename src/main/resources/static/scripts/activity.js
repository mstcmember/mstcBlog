var user;                     //当前用户

//加载数据库中的活动内容
function onloadActivity() {
    var json ={};
    json.start = 0;
    json.num = 10;
    var data=JSON.stringify(json);
    $.ajax({                   //获取当前的用户
        type: "POST",
        url: "/activity/onLoad",
        data: data,
        contentType: "application/json",
        error: function () {
            alert('加载活动失败');
        },
        success: function (data1) {
            var activity_list = Array();
            activity_list = data1.data;
            var temp ={};
            temp.activity=data1.data;
            $("#each_activity").tmpl(temp).appendTo("#discuss_list");
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
                                          url: "/activity/onLoad",
                                          data: data,
                                          contentType: "application/json",
                                          error: function () {
                                              alert('加载分页博客失败 ');
                                          },
                                          success: function (data1) {
                                              if(data1.status==999){
                                                  alert(data1.msg);
                                              }
                                              else {
                                                  var temp = {};
                                                  temp.activity=data1.data;
                                                  $("#each_activity").tmpl(temp).appendTo("#discuss_list");
                                                  // $('.left li').eq(flag).addClass('active');
                                                  if($(".middle").height()>430)
                                                      $("#footer").css('position','static');
                                                  else $("#footer").css('position','absolute');
                                              }
                                              document.getElementById('hidden').style.display='none';
                                          }
                                      })
                                  }
                              });
            // $('.left li').eq(flag).addClass('active');
            if($(".middle").height()>430)
                $("#footer").css('position','static');
            else $("#footer").css('position','absolute');
            document.getElementById('hidden').style.display='none';
        }
    });
}

//发布活动内容
function publish_activity(){
      var title=$("#title").val();
      var content = document.getElementById("comment-content").value;
      if(!(title&&content)) {alert("标题与内容不能为空!"); return;}
      var json={};
      json.title=title;
      json.content=content;
      json.userId=user.id;
      var data=JSON.stringify(json);
      $.ajax({                   //获取当前的用户
        type: "POST",
        url: "/activity/publish",
        data: data,
        contentType: "application/json",
        error: function () {
            alert('发布活动请求失败 ');
        },
        success: function (data) {
            if(data.status==200){
                alert('发布成功！');
                window.location.href="/activity.html";
            }else{
                alert('发布活动失败！')
            }

        }
    })
    }

function onloadActivityDetail() {      //加载活动详情页面
    var url = window.location.search;
    var loc = url.substring(url.lastIndexOf('=') + 1, url.length);
    var json = {};
    json.activityId = loc;
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/activity/onLoadDetail",
        data: data,
        contentType: "application/json",
        error: function () {
            alert("加载活动详情页面失败!");
        },
        success: function (data) {
            activity = data.data;
            document.getElementById('question-detail-title').innerHTML = activity.title;
            var text=activity.content;
             $("#content_detail").val(text);//将需要转换的内容加到转换后展示容器的textarea隐藏标签中
             editormd.markdownToHTML("blog_content", {
                    htmlDecode: "style,script,iframe", //可以过滤标签解码
                });
            $(".question-content").find("img").each(function () {
            $(this).css("width", "100%");
            $(this).css("margin-right", "10px");
            });
            document.getElementById('question-detail-time').innerHTML = activity.createTime;
            document.getElementById("question-author-headUrl").src = activity.headUrl;
            if(!$.isEmptyObject(user)) {
                document.getElementById("comment-avatar-headUrl").src = user.headUrl;
            }
            document.getElementById("public-author").href = "personalCenter.html?userId="+activity.userId;
            document.getElementById("question-author-name").href = "personalCenter.html?userId="+activity.userId;
            document.getElementById('question-author-name').innerHTML = activity.nickname;
            document.getElementById('comment-count').innerHTML = activity.commentCount;
            document.getElementById('likeCount').innerHTML = activity.likeCount;
            if(activity.isLike==1){
                document.getElementById("like").style.background = "#8c8c8c";
            }
              if(user!=null){
                        if(user.flag != 0){
                        document.getElementById('IsTopBlog').style.display = "";
                        }}
             if(activity.isTop==1){
                  document.getElementById('isTopText').innerHTML = "取消置顶";
             }
            if(user!=null){
            if(user.flag != 0){
                document.getElementById('deleteBlog').style.display = "";
            }}
        }
    });
}

var activityCommentCount;
var activityCommentNum;

function onloadActivityComment(){
onloadPartActivityComment(0);             //加载时先获取6条评论
activityCommentCount=6;
}


function onloadPartActivityComment(start) {      //加载活动评论
    var url = window.location.search;
    var loc = url.substring(url.lastIndexOf('=') + 1, url.length);
    var json = {};
    json.start = start;
    json.num = 6;
    json.entityId = loc;
    json.entityType = 0;   //有待改善，此处的entityType指的是评论对应的问题类型
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/activity/loadActivityComment",
        data: data,
        contentType: "application/json",
        error: function () {
            alert("加载活动评论页面失败!!");
        },
        success: function (data) {
            activityCommentNum = data.msg;
            var activityCommentList = data.data;
            var temp ={};
            temp.hostHolderId=0;
            temp.flag=0;
            if(user)
             {temp.hostHolderId=user.id;
              temp.flag=user.flag;}
            temp.activityComment=data.data;
            $("#each_activity_comment").tmpl(temp).appendTo("#blog-comment-list");
            var i =0;
            if($.isEmptyObject(activityCommentList)){
                document.getElementById('hidden').style.display='none';
                return;
            }
            for(i=0;i<activityCommentList.length;i++) {
                //从这儿开始写
                var text=activityCommentList[i].content;
                $("#appendTest"+activityCommentList[i].id).val(text);//将需要转换的内容加到转换后展示容器的textarea隐藏标签中

                //转换开始,第一个参数是上面的div的id
                editormd.markdownToHTML("testEditorMdview"+activityCommentList[i].id, {
                    htmlDecode: "style,script,iframe", //可以过滤标签解码

                });
                 var activityCommentReplyList = activityCommentList[i].activityCommentReplyList;
                                     for(var j=0;j<activityCommentReplyList.length;j++){
                                        var replyText=activityCommentReplyList[j].content;
                                           $("#appendTestReply"+activityCommentReplyList[j].id).val(replyText);//将需要转换的内容加到转换后展示容器的textarea隐藏标签中
                                                    //转换开始,第一个参数是上面的div的id
                                                    editormd.markdownToHTML("testEditorMdviewReply"+activityCommentReplyList[j].id, {
                                                        htmlDecode: "style,script,iframe", //可以过滤标签解码
                                      });
                                 }
            }
            $(".comment-body").find("img").each(function () {
            $(this).css("width", "300px");
            $(this).css("margin-right", "10px");
            });
            document.getElementById('hidden').style.display='none';
        }
    });
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
    json.toCommentId=0;
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/activity/comment",
        data: data,
        contentType: "application/json",
        success: function (data) {
            if (data.status == 200) {
                window.location.href = 'activityDetail.html?activityId=' + loc;
            } else {
                alert("评论失败！");
            }
        }
    });
}

function readQuestionCommentReply(id) {
    var CommentId = id.substring(id.lastIndexOf('-') + 1, id.length);
    document.getElementById('blogComment'+CommentId+'-reply').style.display = '';
    document.getElementById('displayReply-'+CommentId).style.display = 'none';
}

function ConcealReadQuestionCommentReply(id){
    var CommentId = id.substring(id.lastIndexOf('-') + 1, id.length);
    document.getElementById('blogComment'+CommentId+'-reply').style.display = 'none';
    document.getElementById('displayReply-'+CommentId).style.display = '';
}

function cancelReply(){
    document.getElementById('').style.display= "none";
    document.getElementById('').style.display= "block";
}
var value_id=0;
var toComment_Id=0;
var reply_name;
function replyDisplay(id,name){
value_id=id;
reply_name=name;
$("#reply_display").show();
$("#reply_content").attr('placeholder',"回复："+reply_name);
}
function replyDiscussDisplay(v_id,to_id,name){
value_id=v_id;
toComment_Id=to_id;
reply_name=name;
$("#reply_display").show();
$('#reply_content').attr('placeholder',"回复："+reply_name);
}
function replyNone(){
 $("#reply_display").hide();
}
function replyBlog(){
    var content = document.getElementById("reply_content").value;
    if ($.isEmptyObject(user)) {
        alert("您还未登录，请先登录再发表评论！");
        return;
    }
    if($.isEmptyObject(content)){
        alert("内容不能为空！");
        return;
    }
    var json={};
    json.entityId = value_id;
    json.entityType = 1;//1表示对评论的评论
    json.userId = user.id;
    json.content = content;
    json.toCommentId=toComment_Id;
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/activity/comment",
        data: data,
        contentType: "application/json",
        success: function (data) {
            if (data.status == 200) {
                alert("回复成功！");
                window.location.reload();
            } else {
                alert("回复失败！");
            }
        }
    });
}
//点赞点踩
function activityLike(id) {
    var url = window.location.search;
    var entityId = url.substring(url.lastIndexOf('=') + 1, url.length);
    if ($.isEmptyObject(user)) {
        alert("您还未登录，请先登录再点赞！");
        return;
    }
    var str = id;
    var isLike = 0;//0表示点赞，1表示取消点赞
    if (str.match("like")) {
        isLike = 0;
    }
    if (str.match("dislike")) {
        isLike = 1;
    }
    var json = {};
    json.isLike = isLike;//
    json.entityType = 0;//0 代表对原活动点赞,可能还会有1表示对评论点赞，但本活动栏没有对评论点赞
    json.entityId = entityId;
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/activity/addActivityLike",
        data: data,
        contentType: "application/json",
        success: function (data) {
            if (data.status == 200) {
                document.getElementById("likeCount").innerHTML=data.data;
                if (isLike == 0) {    //显示点赞
                    document.getElementById("like").style.background = "#8c8c8c";
                }else{
                    document.getElementById("dislike").style.background = "#ffffff";
                }
            } else {
                alert("点赞失败！");
            }
        }
    });
}

//删除活动或者评论
function deleteActivity(type,id){
if(confirm("确认删除？")){
    var json = {};
    json.entityType = type; //0代表问题,1代表评论
    if(type==0){
        var url = window.location.search;
        var loc = url.substring(url.lastIndexOf('=') + 1, url.length);
        json.entityId = loc;
    }else{
        json.entityId = id;
    }
    json.userId = user.id;//操作人的id（有可能是用户自己，也有可能是管理员）
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/activity/deleteActivity",
        data: data,
        contentType: "application/json",
        success: function (data) {
            if (data.status == 200) {
                alert("删除成功！");
                window.location.href = "/activity.html";
            } else {
                alert("删除失败！");
            }
        }
    });
    }
}

//设置活动是否置顶
function  IsTopActivity(){
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
            url: "/activity/isTop",
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
//     alert("自定义事件内容");
    if(activityCommentCount<activityCommentNum) {
        onloadPartActivityComment(activityCommentCount);
        activityCommentCount = activityCommentCount + 6;
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