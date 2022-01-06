package ansan.service;

import ansan.domain.Dto.MemberDto;
import ansan.domain.Entity.Member.MemberEntity;
import ansan.domain.Entity.Member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

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

    @Autowired
    private JavaMailSender javaMailSender;  //  자바메일 객체

    // 회원 비밀번호 찾기 -> 메일 전송 [ 임시비밀번호 ]
    @Transactional
    public boolean findpassword( MemberDto memberDto ) {
        List<MemberEntity> memberEntities = memberRepository.findAll();
        for( MemberEntity memberEntity  :  memberEntities){
            if( memberEntity.getM_id().equals(memberDto.getM_id()) &&
                    memberEntity.getM_email().equals( memberDto.getM_email() )){
                // 1. 동일한 아이디와 이메일 찾았으면
                // 2. 찾은 이메일에 이메일 전송

                StringBuilder body = new StringBuilder();   // StringBuilder  : 문자열 연결 클래스  [ 문자열1+문자열2 ]
                body.append("<html> <body><h1> Ansan 계정 임시 비밀번호 </h1>");    // 보내는 메시지에 html 추가

                    Random random = new Random();
                    // 임시 비밀번호 만들기
                    StringBuilder temppassword = new StringBuilder();
                    for( int i = 0 ; i<12 ; i++ ){  // 12 자리 만들기
                        // 랜덤 숫자 -> 문자 변환
                        temppassword.append( (char)((int)(random.nextInt(26))+97) );
                    }
                    body.append("<div>"+temppassword+"</div></html>");      // 보내는 메시지에 임시비밀번호를 html에 추가

                    // 엔티티내 패스워드 변경
                    memberEntity.setM_password( temppassword.toString() );     //JPA

                    // Mime : 전자우편 포멧 프로토콜[통신 규약]
                    // SMTP : 전자우편 전송 프로토콜 [ 통신 규약 ]
                    try {       //
                        MimeMessage message = javaMailSender.createMimeMessage();
                        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "utf-8");

                    mimeMessageHelper.setFrom("kgs2072@naver.com", "Ansan");      // 보내는사람  //  이름
                    mimeMessageHelper.setTo( "kgs2072@naver.com");                                                 //  받는사람
                    mimeMessageHelper.setSubject("Ansan 계정 임시 비밀번호 발송 ");                      // 메일 제목
                    mimeMessageHelper.setText(body.toString(), true);                                    // 메일 내용    // html 형식유무
                    javaMailSender.send(message);     // 메일 전송
                }
                catch ( Exception e ){ System.out.println("메일전송 실패 " + e) ; };
                return true;
            }
        }
        return false;
    }

    // 아이디 중복체크
    public boolean idcheck( String m_id ){
        // 1. 모든 엔티티 가져오기
        List<MemberEntity> memberEntities =  memberRepository.findAll();
        // 2. 모든 엔티티 반복문 돌려서 엔티티 하나씩 가쟈오기
        for( MemberEntity memberEntity : memberEntities ) {
            // 3. 해당 엔티티가 입력한 아이디와 동일하면
            if (memberEntity.getM_id().equals(m_id)) {
                return true; // 중복
            }
        }
        return false; // 중복 없음
    }

    // 이메일 중복체크
    public boolean emailcheck( String email ){
        // 1. 모든 엔티티 가져오기
        List<MemberEntity> memberEntities =  memberRepository.findAll();
        // 2. 모든 엔티티 반복문 돌려서 엔티티 하나씩 가쟈오기
        for( MemberEntity memberEntity : memberEntities ) {
            // 3. 해당 엔티티가 입력한 아이디와 동일하면
            if (memberEntity.getM_email().equals(email)) {
                return true; // 중복
            }
        }
        return false; // 중복 없음
    }

}
















