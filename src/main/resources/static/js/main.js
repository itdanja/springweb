function login(){

    var m_id = $("#m_id").val();
    var m_password = $("#m_password").val();
    var memberdto = { "m_id" : m_id  , "m_password" : m_password  };
    // json형식 = { "키" : 값  ,  "키2" : 값2 }

     $.ajax({
        url: "/member/logincontroller",             // 보내는곳
        data : JSON.stringify(memberdto)  ,   //  전송 데이터 값
           //  JSON.stringify( JSON 자료형 -> 문자열 )
        method: "post",        //  Get , Post 방식중 선택
        contentType: "application/json" ,  //  ajax 타입
        success: function(result ){     // 성공시 반환값
            if( result == 1 ){
                location.href="/"   // 로그인 성공시 메인페이지로 이동
            }else{
                $("#loginfailmsg").html("아이디 혹은 비밀번호가 다릅니다.");
                //  $("#태그ID").html( ) ;   // 태그 안에 html 추가
                // js : document.getElementById("loginfailmsg").innerHTML = "아이디 혹은 비밀번호가 다릅니다";
            }
        }
     });


}