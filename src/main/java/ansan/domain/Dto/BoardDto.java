package ansan.domain.Dto;


import ansan.domain.Entity.Board.BoardEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter@Setter@ToString
@AllArgsConstructor@NoArgsConstructor@Builder
public class BoardDto {
    private int b_num;
    private String b_title;
    private String b_contetns;
    private String b_write;
    private LocalDateTime b_createdDate;
    private int b_view;

    // dto -> entity 메소드
    public BoardEntity toentity(){
        return BoardEntity.builder()
                .b_title( this.b_title )
                .b_contetns(this.b_contetns )
                .b_write( this.b_write).
                build();
    }
}
