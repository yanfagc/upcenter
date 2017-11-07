$(function() {
    $("#submitForm").validate({
        focusCleanup:true,
        focusInvalid:false,
        ignore:'',
        showText:true,
        rules:{
            account:{
                required:true,
                maxlength:15
            }
        },
        messages:{
            account:{
                required:"登录帐号不允许为空！",
                maxlength:"最大长度不能长于15位！"
            }
        }
    });
    $(".submit").on("click",function() {
        var options={
            beforeSubmit:function() {
                var check=$.fn.validForm();
                if(!check){
                    return false;
                }
                showTipsDialog("操作提示","服务器处理中，请稍候...");
            },
            success:function(rsp) {
                if(rsp.code=='1000'){
                    showTipsDialog("保存成功","数据保存成功！",function() {
                        closeDialog();
                        window.close();
                    });
                    if(window.opener){
                        opener.search();
                    }
                }else{
                    var msg=rsp.msg?rsp.msg:"数据保存失败，请联系管理员或稍后再试！";
                    showTipsDialog("错误信息",msg,true);
                }
            },
            error:function(rsp) {
                if(rsp.status==404||rsp.status=='404'){
                    showTipsDialog("错误信息","数据保存失败，无法访问目标地址！",true);
                }else{
                    showTipsDialog("错误信息","数据保存失败，请联系管理员或稍后再试！",true);
                }
            }
        };
        $sessionAjaxSubmit($("#submitForm"),options);
    });

    // 上传封面
    $('#submitForm').on('change','.a-upload input[name="uploadFile"]',function(){
        //上传图片类型校验
        var upload_file = $("#uploadFile").val();
        if (!/\.(jpg|png|JPG|PNG|jpeg|JPEG)$/.test(upload_file)) {
            showTipsDialog("操作提示", "用户头像类型必须是jpg,png,jpeg中的一种", true);
            $("#uploadFile").val('');
            return false;
        }

        $sessionAjaxSubmit($("#submitForm"),{
            url: $ctx + '/basic/upfile/userImage',
            dataType:'JSON',
            beforeSubmit:function() {
                showTipsDialog("操作提示","服务器处理中，请稍候...");
            },
            success: function (rsp) {
                if (rsp.code == 1000||rsp.code == '1000') {
                    $('input[name="imgurl"]').val(rsp.body);
                    $('#channer_cover_display').attr('src',$ctx+'/basic/viewfile/userImage/'+rsp.body);
                    easyDialog.close();
                } else {
                    showTipsDialog("错误信息", "图片上传出错", true);
                }
                $("#uploadFile").val('');
            },
            error: function (rsp) {
                if(rsp.status==404||rsp.status=='404'){
                    showTipsDialog("错误信息","图片上传出错，无法访问目标地址！",true);
                }else{
                    showTipsDialog("错误信息","图片上传出错，请联系管理员或稍后再试！",true);
                }
                $("#uploadFile").val('');
            }
        });
    });
});

