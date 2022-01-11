package ansan.domain.Entity.Board;


import ansan.domain.Entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity // db내 테이블과 매핑 설정
@Table( name = "board") // 테이블속성 / 테이블이름 설정
@Getter@Setter @ToString
@AllArgsConstructor @NoArgsConstructor @Builder
public class BoardEntity extends BaseTimeEntity {

    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO KEY
    @Column(name = "b_num")
    private int b_num;

    @Column(name = "b_title")
    private String b_title;

    @Column(name = "b_contents")
    private String b_contents;

    @Column(name = "b_write")
    private String b_write;

    @Column(name = "b_view")
    private int b_view;


}
