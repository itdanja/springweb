package ansan.domain.Entity.Member;

import ansan.domain.Entity.BaseTimeEntity;
import ansan.domain.Entity.Room.RoomEntity;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity // DB내 테이블과 연결
@Table( name = "member") // 테이블속성 // db에서 사용할 테이블명
@AllArgsConstructor@NoArgsConstructor
@Getter@Setter@ToString@Builder
public class MemberEntity extends BaseTimeEntity {

    @Id // 기본키 pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto key
    private int m_num;   // 회원번호
    @Column
    private String m_id;    // 회원아이디
    @Column
    private String m_password; // 회원비밀번호
    @Column
    private String m_name; // 회원이름
    @Column
    private String m_sex; // 회원성별
    @Column
    private String m_phone; // 회원연락처
    @Column
    private String m_email; // 회원이메일
    @Column
    private String m_address; // 회원주소
    @Column
    private int m_point; // 회원포인트
    @Column
    private String m_grade; // 회원등급

    // 룸 리스트
    @OneToMany( mappedBy ="memberEntity" )
    private List<RoomEntity> roomEntities = new ArrayList<>();

}
