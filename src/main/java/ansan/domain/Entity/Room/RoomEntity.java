package ansan.domain.Entity.Room;

import ansan.domain.Entity.BaseTimeEntity;
import ansan.domain.Entity.Member.MemberEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity //해당 클래스 엔티티로 사용 [ DB내 테이블과 매핑 ]
@Table(name = "Room") // 테이블속성 [ name="테이블이름" ]
@AllArgsConstructor // 풀생성자 [ 롬복 ]
@NoArgsConstructor // 빈생성자 [ 롬복 ]
@Getter @Setter // 필드 get , set 메소드 [ 롬복 ]
@ToString( exclude="memberEntity" ) // ToString-> Object[ 객체의 주소값 ] : @ToString -> [ 모든 필드의 내용물 ]
@Builder // 객체 생성시 안정성 보장 [   new 생성자()  <---> Builder ] : 1.필드 주입순서X
public class RoomEntity extends BaseTimeEntity {

    //번호
    @Id // pk [ 기본키 : 테이블 1개당 기본키 1개 권장 ]
    @GeneratedValue( strategy = GenerationType.IDENTITY ) // 오토키
    @Column( name = "rnum") // 필드 속성 ( name ="필드명" )
    private int rnum;
    // 이름
    @Column( name = "rname")
    private String rname;
   //가격
   @Column( name = "rprice")
   private String rprice;
    //면적
    @Column( name = "rarea")
    private int rarea;
    //관리비
    @Column( name = "rmanagementfee")
    private int rmanagementfee;
     //준공날짜
     @Column( name = "rcompletiondate")
     private String rcompletiondate;
    //입주가능일
    @Column( name = "rindate")
    private String rindate;
     //구조
     @Column( name = "rstructure")
     private String rstructure;
    //층/건물층수
    @Column( name = "rfloor")
    private String rfloor;
     //건물종류
     @Column( name = "rkind")
     private String rkind;
    //주소
    @Column( name = "raddress")
    private String raddress;
     // 내용
     @Column( name = "rcontents")
     private String rcontents;
    //상태
    @Column( name = "ractive")
    private String ractive;
     //거래방식 [ 전세 , 월세 , 매매 ]
     @Column( name = "rtrans")
     private String rtrans;

    //회원번호 관계
    @ManyToOne
    @JoinColumn(name ="mnum" ) // 해당 필드 의 이름 [ 컬럼 = 열 = 필드 ]
    private MemberEntity memberEntity;

    //이미지 관계 // 룸 삭제/변경시 이미지도 같이 삭제/변경 [ 제약조건 : cascade = ALL ]
    @OneToMany( mappedBy = "roomEntity" , cascade = CascadeType.ALL)
    private List<RoomimgEntity> roomimgEntities = new ArrayList<>();

}













