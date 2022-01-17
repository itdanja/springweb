package ansan.domain.Entity.Board;


import ansan.domain.Entity.BaseTimeEntity;
import ansan.domain.Entity.Member.MemberEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity // db내 테이블과 매핑 설정
@Table( name = "board") // 테이블속성 / 테이블이름 설정
@Getter@Setter @ToString
@AllArgsConstructor @NoArgsConstructor @Builder
public class BoardEntity extends BaseTimeEntity {

    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO KEY
    @Column(name = "bnum")
    private int bnum;

    @Column(name = "b_title")
    private String b_title;

    @Column(name = "b_contents" , columnDefinition = "LONGTEXT") // 썸머노트 내용에 사진이 들어갈경우에 바이트 커야함
    private String b_contents;

    @Column(name = "b_write")
    private String b_write;

    @Column(name = "b_view")
    private int b_view;

    @Column(name = "b_img" )
    private String b_img;

    // 여러개 댓글 저장할 리스트
    @OneToMany( mappedBy = "boardEntity")
    private List<ReplyEntitiy> replyEntitiys = new ArrayList<>();


}
