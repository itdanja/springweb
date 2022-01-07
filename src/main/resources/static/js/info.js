function mdelete(){
    var passwordconfirm = $("#passwordconfirm").val();
    $.ajax({
        url: "/member/mdelete" ,
        data : { "passwordconfirm" : passwordconfirm } ,
        success : function(data){
                if( data == 1 ){
                    location.href="/member/logout";
                }else{
                    $("#deletemsg").html("[회원탈퇴실패] 비밀번호가 다릅니다.");
                }
        }
    });
}