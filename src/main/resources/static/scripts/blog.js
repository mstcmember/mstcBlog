var user;                     //当前用户
var blogHotList = Array();    //热门博客
//document.getElementById("publicName").html("写博客");
//document.getElementById("publicName").attr("href","/pubBlog.html");
var value_id=0;
var toComment_Id=0;
var reply_name;
 //加载数据库中的博客
function onloadBlog() {
    var json ={};
    var flag=0;
    if(window.location.search.substr(1))
    {var str=window.location.search.substr(1).split("=");
    flag=str[1];}
    json.flag=flag;
    json.start = 0;
    json.num = 10;
    var data=JSON.stringify(json);
    $.ajax({                   //获取当前的用户
        type: "POST",
        url: "/blog/onLoad",
        data: data,
        contentType: "application/json",
        error: function () {
            // alert('加载博客请求失败 ');
        },
        success: function (data1) {
            document.getElementById('discuss_list').innerHTML = "";
            var blogList = Array();
            blogList = data1.data;
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
                                   url: "/blog/onLoad",
                                   data: data,
                                   contentType: "application/json",
                                   error: function () {
                                       // alert('加载分页博客失败 ');
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
function onloadHotBlog() {
    var json ={};
    var begin=0;
    var end=4;
    json.begin=begin;
    json.end=end;
    var data=JSON.stringify(json);
    $.ajax({                   //获取当前的用户
        type: "POST",
        url: "/blog/onLoadHot",
        data: data,
        contentType: "application/json",
        error: function () {
            // alert('加载热门博客请求失败');
        },
        success: function (data1) {
            blogHotList = data1.data;
            if (!$.isEmptyObject(blogHotList)) {
                var obj_lis = document.getElementById("hotBlog_content").getElementsByTagName("a");
                var random=[];
                while(random.length<=obj_lis.length){
                var rand=rnd(0, blogHotList.length - 1);
                if(random.indexOf(rand)==-1) random.push(rand);
                }
                for (i = 0; i < obj_lis.length; i++) {
                    var b=random[i];
                    obj_lis[i].innerHTML = blogHotList[b].title;
                    obj_lis[i].href = "/blogDetail.html?blogId=" + blogHotList[b].id;
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
           obj_lis[i].href = "/blogDetail.html?blogId=" + blogHotList[b].id;
         }
}
function cancelPublicQuestion(){
    document.getElementById("zh-add-question-form").style.display="none";
    document.getElementById("display-question").style.display="";
}
function onclickPublic(){
    document.getElementById("zh-add-question-form").style.display="";
    document.getElementById("display-question").style.display="none";
}
//发布博客
function publish_blog(){
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
      var status=$("#status option:selected").text();
      switch(status)
      {case "是":status=1;break;//0表示公开，1表示私密
      case "否":status=0;break;}
      //console.log(status);
      var json={};
      json.title=title;
      json.content=content;
      json.keyword=keyword;
      json.flag=flag;
      json.status=status;
      json.userId=user.id;
      var data=JSON.stringify(json);
      $.ajax({                   //获取当前的用户
        type: "POST",
        url: "/blog/publish",
        data: data,
        contentType: "application/json",
        error: function () {
            alert('发布博客请求失败 ');
        },
        success: function (data) {
            if(data.status==200){
                alert('发布成功！');
                window.location.href="/blog.html";
                // window.location.href = 'blogDetail.html?blogId=' + loc;
            }else{
                alert('发布失败！')
            }

        }
    })
    }
//加载博客详情页面
function onloadBlogDetail() {      //加载博客详情页
    var url = window.location.search;
    var loc = url.substring(url.lastIndexOf('=') + 1, url.length);
    var json = {};
    json.blogId = loc;
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/blog/onLoadDetail",
        data: data,
        contentType: "application/json",
        error: function () {
            alert("ajx失败!");
        },
        success: function (data) {
            question = data.data;
            document.getElementById('question-detail-title').innerHTML = question.title;
            var text=question.content;
             $("#content_detail").val(text);//将需要转换的内容加到转换后展示容器的textarea隐藏标签中
             editormd.markdownToHTML("blog_content", {
                    htmlDecode: "style,script,iframe", //可以过滤标签解码
                });
            $(".question-content").find("img").each(function () {
                $(this).css("width", "300px");
                $(this).css("margin-right", "10px");
            });
            document.getElementById('question-detail-time').innerHTML = question.createTime;
            document.getElementById("question-author-headUrl").src = question.headUrl;
            if(!$.isEmptyObject(user)) {
                document.getElementById("comment-avatar-headUrl").src = user.headUrl;
            }
            document.getElementById("public-author").href = "personalCenter.html?userId="+question.userId;
            document.getElementById("question-author-name").href = "personalCenter.html?userId="+question.userId;
            document.getElementById('question-author-name').innerHTML = question.nickname;
            document.getElementById('comment-count').innerHTML = question.commentCount;
            document.getElementById('likeCount').innerHTML = question.likeCount;
            if(question.isLike==1){
                document.getElementById("like").style.background = "#8c8c8c";
            }
            if(question.isCollection==1){
                document.getElementById('isCollection').className = "glyphicon glyphicon-heart";
                document.getElementById('isCollection').innerHTML = "已收藏";
            }
              if(user!=null){
                        if(user.flag !=0){
                        document.getElementById('IsTopBlog').style.display = "";
                        }}
             if(question.isTop==1){
                  document.getElementById('isTopText').innerHTML = "取消置顶";
             }
            if(user!=null){
            if(user.id == question.userId||user.flag !=0){
                document.getElementById('deleteBlog').style.display = "";
            }}


        }
    });
}

var blogCommentCount;
var blogCommentNum;

function onloadBlogComment(){
onloadPartBlogComment(0);             //加载时先获取6条评论
blogCommentCount=6;
}
//加载博客的评论
function onloadPartBlogComment(start) {
   var url = window.location.search;
      var loc = url.substring(url.lastIndexOf('=') + 1, url.length);
      var json = {};
      json.start = start;
      json.num = 6;
      json.entityId = loc;
      json.entityType = 0;   //此处的entityType指的是评论对应的问题类型
      var data = JSON.stringify(json);
      $.ajax({
          type: "POST",
          url: "/blog/loadBlogComment",
          data: data,
          contentType: "application/json",
          error: function () {
              alert("加载博客评论失败!");
          },
          success: function (data) {
              blogCommentNum=data.msg;
              var blogCommentList = data.data;
              var temp ={};
              temp.hostHolderId=0;
              temp.flag=0;
              if(user)
              {temp.hostHolderId=user.id;
              temp.flag=user.flag;}
              temp.blogComment=data.data;
              $("#each_blog_comment").tmpl(temp).appendTo("#blog-comment-list");
              var i =0;
              if($.isEmptyObject(blogCommentList)){
                  document.getElementById('hidden').style.display='none';
                  return;
              }
              for(i=0;i<blogCommentList.length;i++) {
                  //从这儿开始写
                  var text=blogCommentList[i].content;
                  $("#appendTest"+blogCommentList[i].id).val(text);//将需要转换的内容加到转换后展示容器的textarea隐藏标签中

                  //转换开始,第一个参数是上面的div的id
                  editormd.markdownToHTML("testEditorMdview"+blogCommentList[i].id, {
                      htmlDecode: "style,script,iframe", //可以过滤标签解码
                      // emoji: true,
                      // taskList: true,
                      // tex: true,               // 默认不解析
                      // flowChart: true,         // 默认不解析
                      // sequenceDiagram: true,  // 默认不解析
                  });
                   var blogCommentReplyList = blogCommentList[i].blogCommentReplyList;
                       for(var j=0;j<blogCommentReplyList.length;j++){
                          var replyText=blogCommentReplyList[j].content;
                             $("#appendTestReply"+blogCommentReplyList[j].id).val(replyText);//将需要转换的内容加到转换后展示容器的textarea隐藏标签中
                                      //转换开始,第一个参数是上面的div的id
                                      editormd.markdownToHTML("testEditorMdviewReply"+blogCommentReplyList[j].id, {
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

//发表博客评论或对评论进行评论
function publishComment(){
    var content = document.getElementById("comment-content").value;

    if ($.isEmptyObject(user)) {
        alert("您还未登录，请先登录再发表评论！");
//        window.location.href="/login.html";
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
        url: "/blog/comment",
        data: data,
        contentType: "application/json",
        success: function (data) {
            if (data.status == 200) {
                window.location.href = 'blogDetail.html?blogId=' + loc;
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
//取消回复
function cancelReply(){
    document.getElementById('').style.display= "none";
    document.getElementById('').style.display= "block";
}
//回复框显示
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
        url: "/blog/comment",
        data: data,
        contentType: "application/json",
        success: function (data) {
            if (data.status == 200) {
                alert("回复成功！");
                window.location.reload();
//                self.location=document.referrer;
            } else {
                alert("回复失败！");
            }
        }
    });



}
//点赞点踩
function blogLike(id) {
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
    json.entityType = 0;//0 代表对原博客点赞,可能还会有1表示对评论点赞
    json.entityId = entityId;
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/blog/addBlogLike",
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
function  addBlogCollection(){
    if ($.isEmptyObject(user)) {
        alert("您还未登录，请先登录再收藏！");
        return;
    }
    var json = {};
    var url = window.location.search;
    var loc = url.substring(url.lastIndexOf('=') + 1, url.length);
    json.entityId = loc;
    json.entityType = 1; //1代表博客
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
//删除博客
function deleteBlog(type,id){
if(confirm("确认删除？")){
    var json = {};
    json.entityType = type; //0代表博客,1代表评论
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
        url: "/blog/deleteBlog",
        data: data,
        contentType: "application/json",
        success: function (data) {
            if (data.status == 200) {
                alert("删除成功！");
                window.location.href = "/blog.html";
            } else {
                alert("删除失败！");
            }
        }
    });
  }
}
//设置帖子是否置顶
function  IsTopBlog(){
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
            url: "/blog/isTop",
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
    if(blogCommentCount<blogCommentNum) {
        onloadPartBlogComment(blogCommentCount);
        blogCommentCount = blogCommentCount + 6;
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