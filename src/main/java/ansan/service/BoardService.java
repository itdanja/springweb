package ansan.service;


import ansan.domain.Dto.BoardDto;
import ansan.domain.Dto.MemberDto;
import ansan.domain.Entity.Board.BoardEntity;
import ansan.domain.Entity.Board.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

        return boardRepository.findAll( pageable );
    }

    // 모든 글출력 메소드[ 페이징처리 x ]
/*    public ArrayList<BoardDto> boardlist(){

        // 게시물 번호를 정렬해서 엔티티 호출하기
        // SQL : Select * from board order by 필드명 DESC
        // JPA : boardRepository.findAll( Sort.by( Sort.Direction.DESC , "entity 필드명" ) );

        List<BoardEntity> boardEntities
                = boardRepository.findAll( Sort.by( Sort.Direction.DESC , "createdDate" ) ); // 모든 엔티티 호출

        ArrayList<BoardDto> boardDtos = new ArrayList<>(); // 모든 dto 담을 리스트 선언
        for( BoardEntity boardEntity : boardEntities ){ // 모든 엔티티를 반복하면서 하나씩 꺼내오기
            // 엔티티 -> dto 변환

            // 게시물 작성일 날짜형 변환 [ LocalDateTime -> String ]
                // LocalDateTime.format( DateTimeFormatter.ofPattern("yy-MM-dd") ) ;
            String date = boardEntity.getCreatedDate().format( DateTimeFormatter.ofPattern("yy-MM-dd") );
            // 오늘날짜 [ LocalDateTime -> String ]
            String nowdate = LocalDateTime.now().format ( DateTimeFormatter.ofPattern("yy-MM-dd") );
            // 만약에 게시물 작성일이 오늘이면 시간출력 오늘이 아니면 날짜를 출력
            if( date.equals( nowdate ) ){
                date = boardEntity.getCreatedDate().format( DateTimeFormatter.ofPattern("hh:mm:ss") );
            }

            System.out.println(  nowdate );
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

    // 게시물 view 출력
    public BoardDto getboard( int b_num ){

        // findById( "pk값") : 해당 pk의 엔티티를 호출 => entityOptional.get()
        Optional<BoardEntity> entityOptional = boardRepository.findById(b_num);

        String date = entityOptional.get().getCreatedDate().format( DateTimeFormatter.ofPattern("yy-MM-dd") );

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

            return true;
        }
        catch ( Exception e ){

            System.out.println( e );
            return false;
        }
    }

}














