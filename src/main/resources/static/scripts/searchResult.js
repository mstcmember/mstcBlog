var TempKeyword; //下面框的关键词
var TempType; //当前的分区选择
var keyword;//头页面的关键词
function onloadSearchResult() {
    document.getElementById('discuss_list').innerHTML = "";
    var json = {};
    var url = window.location.search;
    keyword = decodeURI(url.substring(url.indexOf('=') + 1, url.indexOf("&")));
    var type = url.substring(url.lastIndexOf('=') + 1, url.length);
    if($.isEmptyObject(keyword)){
        json.keyword = " ";
    }else {
        json.keyword = keyword;
    }
    if($.isEmptyObject(type)){
        json.type = -1;
    }else {
        json.type = type;
    }
    json.start = 0;
    json.num = 10;
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/search/searchResult",
        data: data,
        contentType: "application/json",
        error: function () {
            alert('smx失败 ');
        },
        success: function (data1) {
            document.getElementById('discuss_list').innerHTML = "";
            var temp ={};
            temp.question=data1.dataList;
            $("#each_question").tmpl(temp).appendTo("#discuss_list");
            document.getElementById("search-keyword-searchPage").value=keyword;

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
                    var keyword = url.substring(url.indexOf('=') + 1, url.indexOf("&"));
                    var type = url.substring(url.lastIndexOf('=') + 1, url.length);
                    var json = {};
                    if($.isEmptyObject(keyword)){
                        json.keyword = " ";
                    }else {
                        json.keyword = keyword;
                    }
                    if($.isEmptyObject(type)){
                        json.type = -1;
                    }else {
                        json.type = type;
                    }
                    json.start = (page-1)*10;
                    json.num = 10;
                    var data = JSON.stringify(json);
                    $.ajax({
                        type: "POST",
                        url: "/search/searchResult",
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
                                temp.question=data1.dataList;
                                $("#each_question").tmpl(temp).appendTo("#discuss_list");
                                document.getElementById("search-keyword-searchPage").value=keyword;
                                if($(".middle").height()>430)
                                    $("#footer").css('position','static');
                                else $("#footer").css('position','absolute');
                            }
                            document.getElementById('hidden').style.display='none';
                        }
                    })
                    //////////////////////////////
                }
            });
            document.getElementById("search-keyword-searchPage").value=keyword;
            if($(".middle").height()>430)
                $("#footer").css('position','static');
            document.getElementById('hidden').style.display='none';
        }
    });

}

function inputKeyword() {
    TempKeyword =document.getElementById("search-keyword-searchPage").value;
    searchKeyword();
}

function chooseKind(type) {
    TempType = type;
    document.getElementById('search-kind-1').className="";
    document.getElementById('search-kind-2').className="";
    document.getElementById('search-kind-3').className="";
    document.getElementById('search-kind-4').className="";
    document.getElementById('search-kind-5').className="";
    document.getElementById('search-kind-'+(TempType+2)).className="active";
    TempKeyword =document.getElementById("search-keyword-searchPage").value;
    searchKeyword();
}

function searchKeyword() {
    document.getElementById('hidden').style.display='flex';
    if(TempType!=-1&&TempType!=0&&TempType!=1&&TempType!=2&&TempType!=3){
        TempType=-1;
    }
    var json={};
    if($.isEmptyObject(TempKeyword)){
        json.keyword = " ";
    }else {
        json.keyword = TempKeyword;
    }
    json.type = TempType;
    json.start = 0;
    json.num = 10;
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/search/searchResult",
        data: data,
        contentType: "application/json",
        error: function () {
            alert('smx失败 ');
        },
        success: function (data1) {
            document.getElementById('discuss_list').innerHTML = "";
            var temp ={};
            temp.question=data1.dataList;
            //if(!temp.question.length) alert("搜索结果为空");
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
                    var json = {};
                    if($.isEmptyObject(TempKeyword)){
                        json.keyword = " ";
                    }else {
                        json.keyword = TempKeyword;
                    }
                    json.type = TempType;
                    json.start = (page-1)*10;
                    json.num = 10;
                    var data = JSON.stringify(json);
                    $.ajax({
                        type: "POST",
                        url: "/search/searchResult",
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
                                temp.question=data1.dataList;
                                $("#each_question").tmpl(temp).appendTo("#discuss_list");
                                if($(".middle").height()>430)
                                    $("#footer").css('position','static');
                                else $("#footer").css('position','absolute');
                                document.getElementById("search-keyword-searchPage").value=TempKeyword;
                            }
                            document.getElementById('hidden').style.display='none';
                        }
                    })
                    //////////////////////////////
                }
            });
            document.getElementById("search-keyword-searchPage").value=TempKeyword;
            if($(".middle").height()>430)
                $("#footer").css('position','static');
            else $("#footer").css('position','absolute');
            document.getElementById('hidden').style.display='none';
        }
    });
}
