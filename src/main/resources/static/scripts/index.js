function onloadImg() {
    var json={};
    json.start = 0;
    json.num = 4;
    var data = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/activity/onLoad",
        data: data,
        contentType: "application/json",
        error: function () {
            alert('主页加载图片失败 ');
        },
        success: function (data1) {
            if(data1.status==999){
                alert(data1.msg);
            }
            else {
                var temp = {};
                temp.activity=data1.data;
                $("#each_img").tmpl(temp).appendTo("#shutter-img");
            }
        }
    })
}