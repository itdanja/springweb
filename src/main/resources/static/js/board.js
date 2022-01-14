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

