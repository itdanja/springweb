function notereply(  nnum ,  ncontents ){ // 답변하기 버튼 클릭
    $("#rcontents").html( '문의내용 : ' + ncontents );
    $("#nnum").val( nnum );
    $("#notereply").modal( "show" );
    // 읽음처리 업데이트
        $.ajax({
            url: "/room/nreadupdate",
            data : { "nnum"  : nnum } ,
            success : function(data){}
        });
}
function notereplywrite( ) {
    var nreply = $("#nreply").val();
    var nnum = $("#nnum").val( );

    $.ajax({
        url: "/member/notereplywrite",
        data : {  "nnum" : nnum , "nreply" : nreply },
        success: function(data){
            alert(data);
        }
    });
}

