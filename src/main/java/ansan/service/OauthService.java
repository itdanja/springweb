package ansan.service;

import ansan.domain.Dto.Oauth2Dto;
import ansan.domain.Entity.Member.MemberEntity;
import ansan.domain.Entity.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class OauthService implements OAuth2UserService<OAuth2UserRequest , OAuth2User> {

    @Override // 소셜 로그인후 회원정보 가져오기 메소드
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser( userRequest );  // perperties 에 요청 uri로부터  인증,토큰,회원정보 등등

        // 회원정보 속성 가져오기
        String nameattributekey = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // 클라이언트 아이디 가져오기
        String registrationid = userRequest.getClientRegistration().getRegistrationId();

        // DTO
        Oauth2Dto oauth2Dto = Oauth2Dto.of( registrationid , nameattributekey  , oAuth2User.getAttributes() );
        // DB 저장
        MemberEntity memberEntity= saveorupdate(oauth2Dto);
        // 세션 할당

        // 리턴 ( 회원정보와 권한[키] )
        return new DefaultOAuth2User(   Collections.singleton( new SimpleGrantedAuthority(  memberEntity.getRoleKey()  )) ,   oauth2Dto.getAttribute() , oauth2Dto.getNameattributekey()  );

    }
    @Autowired
    private MemberRepository memberRepository;
    // 동일한 이메일이 있을경우 업데이트 동일한 이메일 없으면 저장
    public MemberEntity saveorupdate(  Oauth2Dto oauth2Dto ){
        //MemberEntity memberEntity = memberRepository.findByemail( oauth2Dto.getEmail() ).get();
        return memberRepository.save( oauth2Dto.toEntity() );
    }


}
