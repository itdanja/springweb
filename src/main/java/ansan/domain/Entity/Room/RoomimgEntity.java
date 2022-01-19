package ansan.domain.Entity.Room;

import ansan.domain.Entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity @Table(name = "Roomimg")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @ToString( exclude="roomEntity" )@Builder
public class RoomimgEntity extends BaseTimeEntity {
        //번호
        @Id // pk [ 기본키 : 테이블 1개당 기본키 1개 권장 ]
        @GeneratedValue( strategy = GenerationType.IDENTITY ) // 오토키
        @Column( name = "rimgnum") // 필드 속성 ( name ="필드명" )
        private int rimgnum;
        //이미지경로
        @Column( name = "rimg")
        private String rimg;
        // 룸 관계
        @ManyToOne
        @JoinColumn(name="rnum")
        private RoomEntity roomEntity;
}
