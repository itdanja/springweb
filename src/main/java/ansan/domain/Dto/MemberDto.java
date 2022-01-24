package ansan.domain.Dto;

import ansan.domain.Entity.Member.MemberEntity;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor // 빈생성자
@AllArgsConstructor // 풀생성자
@Getter@Setter  // get / set 메소드
@ToString // 객체주소 정보 -> 객체내 필드 값 정보 로 변경 오버라이딩
@Builder
public class MemberDto {
    // 필드
    private int m_num;   // 회원번호
    private String m_id;    // 회원아이디
    private String m_password; // 회원비밀번호
    private String m_name; // 회원이름
    private String m_sex; // 회원성별
    private String m_phone; // 회원연락처
    private String m_email; // 회원이메일
    private String m_address; // 회원주소
    private int m_point; // 회원포인트
    private String m_grade; // 회원등급
    private LocalDateTime m_createdDate; // 회원 가입일


    // Dto -> entity
    public MemberEntity toentity(){
        return MemberEntity.builder()
                        .m_id( this.m_id )
                        .m_password(this.m_password)
                        .m_name( this.m_name)
                        .m_sex( this.m_sex )
                        .m_phone( this.m_phone)
                        .m_email( this.m_email)
                        .m_point( this.m_point)
                        .m_address( this.m_address)
                        .m_grade( this.getM_grade()).build();
    }
}