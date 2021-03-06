package ansan.domain.Dto;

import ansan.domain.Entity.Member.MemberEntity;
import ansan.domain.Entity.Member.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter@Setter
public class Oauth2Dto {

    String name;   // 이름
    String email;     // 이메일
    private Map<String , Object> attribute;     // 회원정보
    private String nameattributekey;   // 요청 토큰[키]

    // 풀생성자
    @Builder
    public Oauth2Dto(String name, String email, Map<String, Object> attribute, String nameattributekey) {
        this.name = name;
        this.email = email;
        this.attribute = attribute;
        this.nameattributekey = nameattributekey;
    }
    // 클라이언트 구분용 메소드 [ 카카오 or 네이버 or 구글 ]
    public static Oauth2Dto of( String registrationid , String nameattributekey , Map<String, Object> attribute){
        if( registrationid.equals("kakao")){   return ofkakao(    nameattributekey , attribute  ); }   // 카카오
        else if( registrationid.equals("naver") ){ return ofnaver(    nameattributekey , attribute  );} // 네이버
        else{ return ofgoogle(    nameattributekey , attribute  );} // 구글
    }

    // 구글 정보 dto 변환 메소드
    private static Oauth2Dto ofgoogle( String nameattributekey ,Map<String, Object> attribute  ){
                                                                        // 인증키              //    회원정보
        return Oauth2Dto.builder()
                .name( (String) attribute.get("name") )        // 구글 회원 이름
                .email((String) attribute.get("email"))          // 구글 회원 이메일
                .attribute( attribute )                                  // 구글 회원정보
                .nameattributekey( nameattributekey )        // 구글 인증 키
                .build();
    }

    // 네이버 정보 dto 변환 메소드
    private static Oauth2Dto ofnaver( String nameattributekey ,Map<String, Object> attribute  ){

        Map<String, Object> response = (Map<String, Object>) attribute.get("response"); // response 키 호출

        return Oauth2Dto.builder()
                .name( (String) response.get("name") )
                .email((String) response.get("email"))
                .attribute( attribute )
                .nameattributekey( nameattributekey )
                .build();
    }

    // 카카오 정보 dto 변환 메소드
    private static Oauth2Dto ofkakao( String nameattributekey ,Map<String, Object> attribute  ){

        Map<String, Object> kakao_account = (Map<String, Object>) attribute.get("kakao_account"); // kakao_account 키 호출
        Map<String,Object> profile = (Map<String, Object>) kakao_account.get("profile"); // kakao_account 키 호출

        return Oauth2Dto.builder()
                .name( (String) profile.get("nickname") )
                .email((String) kakao_account.get("email"))
                .attribute( attribute )
                .nameattributekey( nameattributekey )
                .build();
    }
    // 첫 로그인했을때 회원가입 dto -> entitiy
    public MemberEntity toEntity(){
        return MemberEntity.builder().m_name(name).memail(email).m_grade(Role.ADMIN).mid( email.split("@")[0] ).build();
    }
}















