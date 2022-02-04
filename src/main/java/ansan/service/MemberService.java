package ansan.service;

import ansan.domain.Dto.IntergratedDto;
import ansan.domain.Dto.MemberDto;
import ansan.domain.Entity.Member.MemberEntity;
import ansan.domain.Entity.Member.MemberRepository;
import ansan.domain.Entity.Member.Role;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class MemberService implements UserDetailsService {
                                                            // 로그인 [ 인증 ] 절차 지원 인터페이스
    @Autowired
    MemberRepository memberRepository ;

    @Autowired
    private JavaMailSender javaMailSender;  //  자바메일 객체

    // 회원등록 메소드
    public boolean membersignup( MemberDto memberDto ){
        // 패스워드 암호화 [ BCryptPasswordEncoder ]
        // 1. 암호화 클래스 객체 생성
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 2. 입력받은 memberDto내 패스워드 재설정 [ 암호화객체명.encode( 입력받은 패스워드 )   ]
        memberDto.setM_password( passwordEncoder.encode( memberDto.getM_password() ) );

        memberRepository.save( memberDto.toentity()  );  // save(entity) : insert / update :  Entity를 DB에 저장
        return true;
    }

//    // 회원 로그인 메소드 [ 스프링시큐리티 사용시 로그인처리 메소드 제공 받기 때문에 사용X ]
//    public MemberDto login(MemberDto memberDto ){
//        List<MemberEntity>  memberEntityList =  memberRepository.findAll();
//        for( MemberEntity memberEntity :  memberEntityList ){
//            if( memberEntity.getMid().equals( memberDto.getM_id()) &&
//                memberEntity.getM_password().equals(memberDto.getM_password())){
//                return MemberDto.builder()
//                        .m_id( memberEntity.getMid() )
//                        .m_num( memberEntity.getM_num() ) .build();
//            }
//        }
//        return null;
//    }

    // 회원 아이디 찾기
    public String findid( MemberDto memberDto ){
        // 1. 모든 엔티티 호출
        List<MemberEntity> memberEntities = memberRepository.findAll();
        // 2. 반복문 이용한 모든 엔티티를 하나씩 꺼내보기
        for( MemberEntity memberEntity  :  memberEntities){
            // 3. 만약에 해당 엔티티가 이름과 이메일이 동일하면
            if( memberEntity.getM_name().equals(memberDto.getM_name()) &&
                memberEntity.getMemail().equals( memberDto.getM_email() )){
                // 4. 아이디를 반환한다
                return memberEntity.getMid();
            }
        }
        // 5. 만약에 동일한 정보가 없으면
        return null;
    }


    // 회원 비밀번호 찾기 -> 메일 전송 [ 임시비밀번호 ]
    @Transactional
    public boolean findpassword( MemberDto memberDto ) {

        List<MemberEntity> memberEntities = memberRepository.findAll();

        for( MemberEntity memberEntity  :  memberEntities) {

            StringBuilder body = new StringBuilder();   // StringBuilder  : 문자열 연결 클래스  [ 문자열1+문자열2 ]
            body.append("<html> <body><h1> Ansan 계정 임시 비밀번호 </h1>");    // 보내는 메시지에 html 추가

            Random random = new Random();
            // 임시 비밀번호 만들기
            StringBuilder temppassword = new StringBuilder();
            for (int i = 0; i < 12; i++) {  // 12 자리 만들기
                // 랜덤 숫자 -> 문자 변환
                temppassword.append((char) ((int) (random.nextInt(26)) + 97));
            }
            body.append("<div>" + temppassword + "</div></html>");      // 보내는 메시지에 임시비밀번호를 html에 추가

            // !!!엔티티내 패스워드 변경
            memberEntity.setM_password(temppassword.toString());     //JPA

                try {
                    // Mime : 전자우편 포멧 프로토콜[통신 규약]
                    // SMTP : 전자우편 전송 프로토콜 [ 통신 규약 ]
                    MimeMessage message = javaMailSender.createMimeMessage();
                    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "utf-8");
                    mimeMessageHelper.setFrom("본인아이디", "Ansan");      // 보내는사람  //  이름
                    mimeMessageHelper.setTo( memberEntity.getMemail() );                                                 //  받는사람
                    mimeMessageHelper.setSubject("Ansan 계정 임시 비밀번호 발송 ");                      // 메일 제목
                    mimeMessageHelper.setText(body.toString(), true);                                    // 메일 내용    // html 형식유무
                    javaMailSender.send(message);     // 메일 전송

                    return true;
                } catch (Exception e) {
                    System.out.println("메일전송 실패 " + e);
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
            if (memberEntity.getMid().equals(m_id)) {
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
            if (memberEntity.getMemail().equals(email)) {
                return true; // 중복
            }
        }
        return false; // 중복 없음
    }

    // 회원번호 -> 회원정보 반환
    public MemberDto getmemberDto( int m_num ){
            // memberRepository.findAll(); : 모든 엔티티 호출
            // memberRepository.findById( pk값 ) : 해당 pk값의 엔티티 호출
            // 1. 해당 회원번호[pk] 만 엔티티 호출
            Optional<MemberEntity> entityOptional = memberRepository.findById(m_num);
            // 2. 찾은 entity를 dto 변경후 반환 [ 패스워드 , 수정날짜 제외 ]
            return MemberDto.builder()
                    .m_id( entityOptional.get().getMid() )
                    .m_name( entityOptional.get().getM_name() )
                    .m_address( entityOptional.get().getM_address() )
                    .m_email( entityOptional.get().getMemail() )
                    .m_grade( entityOptional.get().getM_grade() )
                    .m_phone( entityOptional.get().getM_phone() )
                    .m_point( entityOptional.get().getM_point() )
                    .m_sex( entityOptional.get().getM_sex() )
                .m_createdDate( entityOptional.get().getCreatedDate() )
                .build();
    }

    @Transactional
    public boolean delete( int m_num , String passwordconfirm ){
        // 1. 로그인된 회원번호의 엔티티[레코드] 호출
        Optional<MemberEntity> entityOptional = memberRepository.findById(m_num);
            // Optional 클래스 :  null 포함 객체 저장
        // 2. 해당 엔티티내 패스워드가 확인패스워드와 동일하면
        if( entityOptional.get().getM_password().equals( passwordconfirm) ){
            // Optional 클래스 -> memberEntity.get()  :  Optional 내 객체 호출
            memberRepository.delete( entityOptional.get() );
            return true;    // 회원탈퇴
        }
        return false;  // 회원탈퇴 X
    }
    // 회원번호 -> 회원엔티티 반환
    public MemberEntity getmentitiy( int mnum){
        Optional<MemberEntity> entityOptional
                = memberRepository.findById(mnum);
        return  entityOptional.get();
    }

    @Autowired
    private HttpServletRequest request;

    @Override   // /member/logincontroller URL 호출시 실행되는 메소드 [ 로그인처리(인증처리) 메소드 ]
    public UserDetails loadUserByUsername(String mid ) throws UsernameNotFoundException {

        // 회원 아이디로 회원엔티티 찾기
        Optional<MemberEntity> entityOptional = memberRepository.findBymid( mid );
        MemberEntity memberEntity = entityOptional.orElse(null);
                                                        //   .orElse( null ) : 만약에 엔티티가 없으면 null

        // 찾은 회원엔티티의 권한[키] 을 리스트에 담기
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add( new SimpleGrantedAuthority( memberEntity.getRoleKey() ) ) ;
                                // GrantedAuthority : 권한 [ 키 저장 가능한 클래스 ]

        // 세션 부여
        MemberDto loginDto =   MemberDto.builder().m_id(memberEntity.getMid()).m_num( memberEntity.getM_num() ).build();
              HttpSession session = request.getSession();   // 서버내 세션 가져오기
              session.setAttribute( "logindto" , loginDto );    // 세션 설정

        // 회원정보와 권한을 갖는 UserDetails 반환
        return new IntergratedDto( memberEntity , authorities );
    }

}
















