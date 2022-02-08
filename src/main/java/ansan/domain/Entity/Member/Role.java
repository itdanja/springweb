package ansan.domain.Entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor@Getter
public enum Role {  // 멤버 등급[권한]
    // enum 열거형 자료형 [ 객체도 배열처럼 사용 ]
    // Class : 클래스 자료형
    // interface : 인터페이스 자료형

    ADMIN( "ROLE_ADMIN","관리자" ) ,  MEMBER("ROLE_MEMBER" , "일반회원");
        // 생성자를 통한 key 와 title 할당
    private String key ;
    private String title ;

}
