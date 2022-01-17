function bdelete( b_num ){
            // 1.  기존JS 이름 중복 // 2. 키워드X

    $.ajax({
        url: "/board/boarddelete",
        data : { "b_num" : b_num },
        success: function(result) {
            if( result == 1 ){
                alert("삭제 했습니다.");
                location.href="/board/boardlist";
            }else{
                alert("이미 삭제된 게시물 혹은 오류발생");
            }
        }
    });
}

function boardwrite( ){

    // 1. 폼 태그 가져오기 [ 폼 안에 있는 input 사용 가능 ]
    var formData = new FormData(form);

    // 폼을 컨트롤러에게 비동기 전송
    $.ajax({
        type : "POST" ,
        url : "/board/boardwritecontroller" ,
        data : formData ,
        processData : false ,
        contentType : false ,   // 첨부파일 보낼때 ..
        cache: false ,
        timeout: 600000 ,
        success : function( data ) {
           if( data == 1 ){
                location.href="/board/boardlist";
           }
        }
    });
}

// 썸머노트 실행
$(document).ready(function() {
  $('#summernote').summernote({
    lang: 'ko-KR' ,  // 메뉴 한글 버전 ,
     minHeight : 400 , // 최소 높이
     maxHeight : null ,
     placeholder : "내용 입력"
  } );

});


// 댓글 등록
function replywrite( bnum ){

    var rcontents = $("#rcontents").val();

    // 댓글내용 미 입력시 댓글 저장 막기
    if( rcontents == "" ){ alert("댓글 내용을 입력해주세요~~ "); return; }
    $.ajax({
        url : "/board/replywirte",
        data : { "bnum" : bnum , "rcontents" : rcontents } ,
        success : function( data ){
               if( data == 1 ){
                    // 특정 태그만 새로고침  [ jQuery ]
                    $('#replytable').load( location.href+' #replytable' );
                    // 댓글 입력창 공백
                    $("#rcontents").val("");
               }else if( data == 2 ) {
                    alert("로그인후 사용 가능합니다" ); return;
               }
        }
    });
}









