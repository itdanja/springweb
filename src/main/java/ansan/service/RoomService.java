package ansan.service;

import ansan.domain.Dto.MemberDto;
import ansan.domain.Entity.Member.MemberEntity;
import ansan.domain.Entity.Member.MemberRepository;
import ansan.domain.Entity.Room.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
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
    @Autowired
    private NoteRepository noteRepository;


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

    // 특정 룸 상태변경
    @Transactional // 업데이트시 필수!!!
    public boolean activeupdate( int rnum , String upactive ){
        RoomEntity roomEntity =
                roomRepository.findById( rnum ).get(); // 엔티티 호출
        if( roomEntity.getRactive().equals( upactive ) ){
            // 선택 버튼의 상태와 기존 룸 상태가 동일하면 업데이트X
            return false;
        }else{
            roomEntity.setRactive( upactive ); return true;
        }
    }

    // 방정보 수정
    @Transactional
    public boolean update( int rnum , String field , String newcontents  ){
        RoomEntity roomEntity =  roomRepository.findById( rnum ).get();
        if( field.equals("rname") ){   roomEntity.setRname( newcontents );     }
        else if( field.equals("rprice") ){ roomEntity.setRprice( newcontents ); }
        return true;
    }

    // 문의 등록
    public boolean notewrite( int rnum , String ncontents ) {

        // 로그인된 회원정보를 가져온다. [ 작성자 ]
        HttpSession session = request.getSession();
        MemberDto memberDto =
                (MemberDto)session.getAttribute("logindto");
        // 만약에 로그인이 안되어 있으면
        if( memberDto == null ){ return  false; } // 등록 실패
        // 문의 엔티티 생성
        NoteEntity noteEntity = new NoteEntity();
            noteEntity.setNcontents( ncontents ); // 작성내용
            noteEntity.setMemberEntity(  memberService.getmentitiy( memberDto.getM_num()) ); // 작성자 엔티티
            noteEntity.setRoomEntity( roomRepository.findById( rnum ).get() );  // 방 엔티티
        // 문의 엔티티 저장
        int nnum =  noteRepository.save( noteEntity ).getNnum(); //
        // 해당 룸엔티티의 문의리스트에 문의엔티티 저장
        roomRepository.findById( rnum ).get().getNoteEntities().add( noteRepository.findById(nnum).get()  );
        // 해당 회원엔티티의 문의리스트에 문의엔티티 저장
        memberService.getmentitiy( memberDto.getM_num() ).getNoteEntities().add(  noteRepository.findById(nnum).get()  );
        return true; // 등록 성공
    }


    // 로그인 된 회원이 등록한 방 출력
    public List<RoomEntity> getmyroomlist(  ){
        HttpSession session = request.getSession();
        MemberDto memberDto =
                (MemberDto)session.getAttribute("logindto");
        MemberEntity memberEntity =
                memberService.getmentitiy( memberDto.getM_num() );
        return memberEntity.getRoomEntities();
    }
    // 로그인 된 회원이 등록한 문의 출력
    public List<NoteEntity> getmynotelist( ){
        HttpSession session = request.getSession();
        MemberDto memberDto =
                (MemberDto)session.getAttribute("logindto");
        MemberEntity memberEntity =
                memberService.getmentitiy( memberDto.getM_num() );
        return memberEntity.getNoteEntities();
    }
}



















