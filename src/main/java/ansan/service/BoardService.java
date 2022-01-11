package ansan.service;


import ansan.domain.Dto.BoardDto;
import ansan.domain.Dto.MemberDto;
import ansan.domain.Entity.Board.BoardEntity;
import ansan.domain.Entity.Board.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    // 모든 글출력 메소드
    public ArrayList<BoardDto> boardlist(){

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
                    boardEntity.getB_contetns(),
                    boardEntity.getB_write() ,
                    date ,
                    boardEntity.getB_view() );
            boardDtos.add( boardDto ); //  리스트에 저장
        }
        return boardDtos;
    }

    // 게시물 view 출력
    public BoardDto getboard( int b_num ){

        // findById( "pk값") : 해당 pk의 엔티티를 호출 => entityOptional.get()
        Optional<BoardEntity> entityOptional = boardRepository.findById(b_num);

        String date = entityOptional.get().getCreatedDate().format( DateTimeFormatter.ofPattern("yy-MM-dd") );

        return BoardDto.builder()
                .b_num( entityOptional.get().getB_num())
                        .b_title( entityOptional.get().getB_title())
                                .b_contetns( entityOptional.get().getB_contetns())
                                        .b_write( entityOptional.get().getB_write())
                                                .b_view( entityOptional.get().getB_view())
                                                        .b_createdDate( date  )
                .build();

    }

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


}














