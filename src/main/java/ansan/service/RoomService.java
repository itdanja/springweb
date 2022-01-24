package ansan.service;

import ansan.domain.Dto.MemberDto;
import ansan.domain.Entity.Member.MemberEntity;
import ansan.domain.Entity.Member.MemberRepository;
import ansan.domain.Entity.Room.RoomEntity;
import ansan.domain.Entity.Room.RoomRepository;
import ansan.domain.Entity.Room.RoomimgEntity;
import ansan.domain.Entity.Room.RoomimgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private MemberService memberService;
    @Autowired
    private RoomimgRepository roomimgRepository;

    // 저장
    public boolean write(RoomEntity roomEntity , List<MultipartFile> files){

        // 등록한 회원번호 찾기 [ 세션에 로그인정보 ]
        HttpSession session = request.getSession();
        MemberDto memberDto =  (MemberDto) session.getAttribute("logindto");
        // 회원번호 -> 회원 엔티티 가져오기
        MemberEntity memberEntity
                = memberService.getmentitiy( memberDto.getM_num() );
        // 룸엔티티에 회원 엔티티 넣기
        roomEntity.setMemberEntity( memberEntity );

            // 방상태 : 첫등록시 검토중으로 설정
            roomEntity.setRactive("검토중");

        // 룸엔티티 저장후에 룸엔티티 번호 가져온다. [ 왜?? 회원엔티티에 룸리스트에 저장 ]
        int rnum = roomRepository.save(roomEntity).getRnum();
        // 회원 엔티티 룸리스트에 룸엔티티 추가
        RoomEntity roomEntitysaved =  roomRepository.findById(rnum).get();
        memberEntity.getRoomEntities().add( roomEntitysaved );

        // 파일 처리
        String uuidfile = null;
        if( files.size() !=0 ) {
            for( MultipartFile file : files ){
                UUID uuid = UUID.randomUUID();
                // 식별난수값 + _ + 파일명 ( 만약에 파일명에 _가 있으면 - 변환 )
                uuidfile = uuid.toString()+"_"+file.getOriginalFilename().replaceAll("_","-");
                String dir = "D:\\web0928\\springweb\\src\\main\\resources\\static\\roomimg";
                String filepath = dir +"\\" + uuidfile; // 경로 + 파일명
                try {
                    file.transferTo(new File(filepath)); // 해당 파일 -> 해당 경로로 파일 이동[복사]
                }
                catch ( Exception e ) { System.out.println("파일 저장 실패 : "+ e );}
                // roomimg 엔티티 생성 [  해당 room엔티티 주입 ]
                RoomimgEntity roomimgEntity = RoomimgEntity.builder().rimg( uuidfile ).roomEntity( roomEntitysaved ).build();
                roomimgRepository.save( roomimgEntity ).getRimgnum();
                // room 엔티티내 roomimg리스트에 roomimg엔티티 주입
                roomRepository.findById( rnum ).get().getRoomimgEntities().add( roomimgEntity  );
            }
        }
        return true;
    }

    // 모든 룸 가져오기
    public List<RoomEntity> getroomlist(){
        return roomRepository.findAll();
    }

    // 특정 룸 가져오기
    public RoomEntity getroom( int rnum ){
        return roomRepository.findById( rnum ).get();
    }
    // 특정 룸 삭제
    public boolean delete( int rnum ){
        roomRepository.delete(  roomRepository.findById( rnum ).get() );
        return true;
    }
}



















