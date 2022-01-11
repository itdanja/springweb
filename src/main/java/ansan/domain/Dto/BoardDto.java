package ansan.domain.Dto;


import ansan.domain.Entity.Board.BoardEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter@Setter@ToString
@AllArgsConstructor@NoArgsConstructor@Builder
public class BoardDto {
    private int b_num;
    private String b_title;
    private String b_contents;
    private String b_write;
    private String b_createdDate;
    private int b_view;
    private String b_img;


    // dto -> entity 메소드
    public BoardEntity toentity(){
        return BoardEntity.builder()
                .b_title( this.b_title )
                .b_contents(this.b_contents )
                .b_write( this.b_write)
                .b_img( this.b_img ).
                build();
    }

}
