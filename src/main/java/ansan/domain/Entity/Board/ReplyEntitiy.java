package ansan.domain.Entity.Board;

import ansan.domain.Entity.BaseTimeEntity;
import ansan.domain.Entity.Member.MemberEntity;
import lombok.*;

import javax.persistence.*;

@Entity // db내 테이블과 매핑 설정
@Table( name = "reply") // 테이블속성 / 테이블이름 설정
@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor @Builder
public class ReplyEntitiy extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rnum")
    private  int rnum;

    @Column(name = "rcontents")
    private  String rcontents;

    @Column(name = "rwrite")
    private  String rwrite;

    // 여러개 댓글을 하나의 게시물과 연결
    @ManyToOne // 다:일 연관 관계 매핑
    @JoinColumn( name="bnum" )
    private BoardEntity boardEntity ;
        // 주의 : 댓글 생성시 연결한 게시물 엔티티를 넣어주세요!

}
