package ansan.service;


import ansan.domain.Dto.BoardDto;
import ansan.domain.Dto.MemberDto;
import ansan.domain.Entity.Board.BoardEntity;
import ansan.domain.Entity.Board.BoardRepository;
import ansan.domain.Entity.Board.ReplyEntitiy;
import ansan.domain.Entity.Board.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service    // 필수!!!!!!!
public class BoardService {

    @Autowired
    BoardRepository boardRepository;

    // 글쓰기 메소드
    public void boardwrite( BoardDto boardDto){
        boardRepository.save( boardDto.toentity() );
    }


    // 모든 글출력 메소드 [ 페이징처리 ]
    public Page<BoardEntity> boardlist( Pageable pageable , String keyword , String search ){


        // 페이지 번호
        int page =  0;
        if( pageable.getPageNumber() == 0) page = 0;        // 0이면 1 페이지
        else page = pageable.getPageNumber()-1 ;                // 1이면-1   1 페이지 2이면-1   2페이지
        // 페이지 속성 [ PageRequest.of( 페이지번호 , 페이당 게시물수 , 정렬기준 )
        pageable = PageRequest.of(  page, 5 , Sort.by( Sort.Direction.DESC , "bnum") );   //  해당 변수 페이지 에 10 개 출력

        // 만약에 검색이 있을경우
        if (  keyword !=null && keyword.equals("b_title") ) return boardRepository.findAlltitle( search , pageable );
        if (  keyword !=null && keyword.equals("b_contents") ) return boardRepository.findAllcontents( search , pageable );
        if (  keyword !=null && keyword.equals("b_write") ) return boardRepository.findAllwrite( search , pageable );

        return boardRepository.findAll( pageable );

    }

    // 모든 글출력 메소드[ 페이징처리 x ]
/*    public ArrayList<BoardDto> boardlist(){

        ArrayList<BoardDto> boardDtos = new ArrayList<>(); // 모든 dto 담을 리스트 선언
        for( BoardEntity boardEntity : boardEntities ){ // 모든 엔티티를 반복하면서 하나씩 꺼내오기
            // 엔티티 -> dto 변환

            BoardDto boardDto = new BoardDto(
                    boardEntity.getB_num() ,
                    boardEntity.getB_title() ,
                    boardEntity.getB_contents(),
                    boardEntity.getB_write() ,
                    date ,
                    boardEntity.getB_view() ,
                    boardEntity.getB_img() , null  );
            boardDtos.add( boardDto ); //  리스트에 저장
        }
        return boardDtos;
    }*/
    @Autowired
    HttpServletRequest request;

    // 게시물 view 출력
    @Transactional
    public BoardDto getboard( int b_num ){

        // findById( "pk값") : 해당 pk의 엔티티를 호출 => entityOptional.get()
        Optional<BoardEntity> entityOptional = boardRepository.findById(b_num);

        String date = entityOptional.get().getCreatedDate().format( DateTimeFormatter.ofPattern("yy-MM-dd") );

        // [ 세션 이용한 ] 조회수 중복 방지
        HttpSession session = request.getSession();
        if( session.getAttribute(b_num+"") == null ) { // 만약에 기존에 조회수 증가을 안했으면
            // 조회수 변경
            entityOptional.get().setB_view(entityOptional.get().getB_view() + 1);
            // 세션 부여
            session.setAttribute(b_num + "", 1);
            // 해당 세션 시간  [ 1초 = 60*60*24 = 24시간 ]
            session.setMaxInactiveInterval( 60*60*24 );
        }


        return BoardDto.builder()
                .b_num( entityOptional.get().getBnum())
                        .b_title( entityOptional.get().getB_title())
                                .b_contents( entityOptional.get().getB_contents())
                                        .b_write( entityOptional.get().getB_write())
                                                .b_view( entityOptional.get().getB_view())
                                                    .b_img( entityOptional.get().getB_img())
                                                        .b_createdDate( date  )
                .build();

    }
    // 게시물 삭제 처리
    public boolean delete( int b_num ){
        Optional<BoardEntity> entityOptional = boardRepository.findById(b_num);
        if( entityOptional.get() != null ) {
            boardRepository.delete(entityOptional.get());
            return true;
        }
        else{
            return false;
        }
    }

    // 게시물 수정 처리
    @Transactional // 수정중 오류 발생시 rollback : 취소
    public boolean update( BoardDto boardDto ){
        try {
            // 1. 수정할 엔티티 찾는다
            Optional<BoardEntity> entityOptional = boardRepository.findById(boardDto.getB_num());
            // 2. 엔티티를 수정한다 [ 엔티티 변화 -> DB 변경처리 ]
            entityOptional.get().setB_title( boardDto.getB_title());
            entityOptional.get().setB_contents( boardDto.getB_contents());
            entityOptional.get().setB_img( boardDto.getB_img());

            return true;
        }
        catch ( Exception e ){

            System.out.println( e );
            return false;
        }
    }

    @Autowired
    private ReplyRepository replyRepository;

    // 댓글 등록
    public boolean replywirte( int bnum , String rcontents , String rwrite ){

        // 게시물번호에 해당하는 게시물 엔티티 출력
        Optional<BoardEntity> entityOptional =   boardRepository.findById( bnum );

        ReplyEntitiy replyEntitiy = ReplyEntitiy.builder()
                .rcontents( rcontents )
                        .rwrite( rwrite )
                                .boardEntity ( entityOptional.get() ) //  게시물->댓글 저장  [ 해당 게시물의 엔티티 넣기 ]
                                    .build();

        replyRepository.save( replyEntitiy ); // 댓글 저장

        // 해당 게시물내 댓글 저장 [ 댓글 -> 게시물 저장 ]
        entityOptional.get().getReplyEntitiys().add( replyEntitiy );

        return false;
    }

    // 모든 댓글 출력
    public List< ReplyEntitiy > getreplylist( int bnum ){

        // 1. 해당 게시물번호의 엔티티 호출
         Optional<BoardEntity> entityOptional = boardRepository.findById(bnum);
         // 2. 해당 엔티티의 댓글 리스트 호출
        List<ReplyEntitiy> replyEntitiys =  entityOptional.get().getReplyEntitiys();

        return replyEntitiys;

    }

}














