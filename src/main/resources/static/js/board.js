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