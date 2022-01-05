package ansan.service;

import ansan.domain.Dto.MemberDto;
import ansan.domain.Entity.Member.MemberEntity;
import ansan.domain.Entity.Member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    MemberRepository memberRepository ;

    // 회원등록 메소드
    public boolean membersignup( MemberDto memberDto ){
        memberRepository.save( memberDto.toentity()  );  // save(entity) : insert / update :  Entity를 DB에 저장
        return true;
    }
    // 회원 로그인 메소드
    public MemberDto login(MemberDto memberDto ){
        List<MemberEntity>  memberEntityList =  memberRepository.findAll();
        for( MemberEntity memberEntity :  memberEntityList ){
            if( memberEntity.getM_id().equals( memberDto.getM_id()) &&
                memberEntity.getM_password().equals(memberDto.getM_password())){
                return MemberDto.builder()
                        .m_id( memberEntity.getM_id() )
                        .m_num( memberEntity.getM_num() ) .build();
            }
        }
        return null;
    }

    // 회원 아이디 찾기
    public String findid( MemberDto memberDto ){
        // 1. 모든 엔티티 호출
        List<MemberEntity> memberEntities = memberRepository.findAll();
        // 2. 반복문 이용한 모든 엔티티를 하나씩 꺼내보기
        for( MemberEntity memberEntity  :  memberEntities){
            // 3. 만약에 해당 엔티티가 이름과 이메일이 동일하면
            if( memberEntity.getM_name().equals(memberDto.getM_name()) &&
                memberEntity.getM_email().equals( memberDto.getM_email() )){
                // 4. 아이디를 반환한다
                return memberEntity.getM_id();
            }
        }
        // 5. 만약에 동일한 정보가 없으면
        return null;
    }

    // 회원 비밀번호 찾기 -> 메일 전송 [ 임시비밀번호 ]
    public boolean findpassword( MemberDto memberDto ){
        List<MemberEntity> memberEntities = memberRepository.findAll();
        for( MemberEntity memberEntity  :  memberEntities){
            if( memberEntity.getM_id().equals(memberDto.getM_id()) &&
                    memberEntity.getM_email().equals( memberDto.getM_email() )){
                // 1. 동일한 아이디와 이메일 찾았으면
                // 2. 찾은 이메일에 이메일 전송

                return true;
            }
        }
        return false;
    }


}
















