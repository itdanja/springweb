$( function(){

     // 아이디 유효성검사
    $("#m_id").keyup( function(){// 해당 태그에 키보드가 눌렸을때 이벤트
        var m_id = $("#m_id").val();
        var idj = /^[a-z0-9]{5,15}$/        // 정규 표현식  영소문자 5~15 글자만 허용

        if( !idj.test( m_id ) ){    // 정규 표현식이 다를경우
            $("#idcheck").html("영소문자 5~15 글자만 가능합니다.");

        }else{
            // 아이디중복체크 비동기 통신
            $.ajax({
               url : "/member/idcheck" ,
               data :{ "m_id" : m_id } ,
               success : function( result ){
                     if( result == 1 ){
                        $("#idcheck").html("현재 사용중인 아이디 입니다.");

                     }else{
                         $("#idcheck").html("사용 가능한 아이디 입니다.");
                     }
                 } // success send
           }); // ajax 함수  end
       } // else end
    }); // 키보드이벤트 함수 end

    // 패스워드 유효성검사
    $("#m_password").keyup( function(){
           var pwj = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\d~!@#$%^&*()+|=]{8,15}$/;
           // 영대소문자+숫자+특수문자[ !@#$%^&*()+|= ] 8~15포함
           var m_password = $("#m_password").val();
            if( !pwj.test(m_password ) ){
                 $("#passwordcheck").html("영대소문자+숫자+특수문자[ !@#$%^&*()+|= ] 8~15포함");

            }else{
                 $("#passwordcheck").html("사용 가능한 비밀번호 입니다.");
            }
    });

    // 패스워드 확인 유효성검사
    $("#m_passwordconfirm").keyup( function(){
               var pwj = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\d~!@#$%^&*()+|=]{8,16}$/;
               // 숫자', '문자', '특수문자' 무조건 1개 이상, 비밀번호 '최소 8자에서 최대 16자'까지 허용
               var m_password = $("#m_password").val();
               var m_passwordconfirm = $("#m_passwordconfirm").val();
                if( !pwj.test(m_passwordconfirm ) ){
                     $("#passwordcheck").html("숫자', '문자', '특수문자' 포함 , '최소 8문자~16글자 허용.");

                }else if(  m_password != m_passwordconfirm   ){
                     $("#passwordcheck").html("서로 패스워드가 다릅니다.");

                }else{
                     $("#passwordcheck").html("사용 가능한 비밀번호 입니다.");
                }
        });

        // 이름 유효성검사
        $("#m_name").keyup( function(){
            var namej = /^[A-Za-z가-힣]{1,15}$/;	// 이름 정규표현식
             var m_name = $("#m_name").val();
            if( !namej.test(m_name ) ){
                 $("#namecheck").html("영대문자/한글 1~15 허용");

            }else{
                 $("#namecheck").html("사용 가능한 이름 입니다.");
            }
        });

       // 연락처 유효성검사
        $("#m_phone").keyup( function(){
            var phonej = /^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/; // 연락처
              var m_phone = $("#m_phone").val();
              if( !phonej.test(m_phone ) ){
                   $("#phonecheck").html("01X-XXXX-XXXX 형식으로 입력해주세요");

              }else{
                   $("#phonecheck").html("사용 가능한 연락처 입니다.");
              }
        });
         // 이메일 유효성검사
        $("#m_email").keyup( function(){
                var emailj = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
                var m_email = $("#m_email").val();
                if( !emailj.test(m_email ) ){
                  $("#emailcheck").html("이메일 형식으로 입력해주세요");

                  }else{
                       $("#emailcheck").html("사용 가능한 이메일 입니다.");
                  }
        });


}); // 함수 end




















