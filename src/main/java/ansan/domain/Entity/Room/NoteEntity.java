package ansan.domain.Entity.Room;

import ansan.domain.Entity.BaseTimeEntity;
import ansan.domain.Entity.Member.MemberEntity;
import lombok.*;

import javax.persistence.*;

@Entity @Table(name = "Note")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @ToString @Builder
public class NoteEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "nnum")
    private int nnum;       // 문의 번호

    @Column( name = "ncontents")
    private String ncontents; // 문의 내용

    @Column( name = "nreply")
    private String nreply; // 문의 답변

    @Column( name = "nread")
    private int nread; // 0.안읽음 1. 읽음

    @ManyToOne
    @JoinColumn( name = "mnum")
    private MemberEntity memberEntity; // 보낸 사람

    @ManyToOne
    @JoinColumn( name = "rnum")
    private RoomEntity roomEntity; // 문의 방 // 받는 사람

}
