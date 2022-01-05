package ansan.service;


import ansan.domain.Dto.BoardDto;
import ansan.domain.Entity.Board.BoardEntity;
import ansan.domain.Entity.Board.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        List<BoardEntity> boardEntities = boardRepository.findAll(); // 모든 엔티티 호출
        ArrayList<BoardDto> boardDtos = new ArrayList<>(); // 모든 dto 담을 리스트 선언
        for( BoardEntity boardEntity : boardEntities ){ // 모든 엔티티를 반복하면서 하나씩 꺼내오기
            // 엔티티 -> dto 변환
            BoardDto boardDto = new BoardDto(
                    boardEntity.getB_num() ,
                    boardEntity.getB_title() ,
                    boardEntity.getB_contetns(),
                    boardEntity.getB_write() ,
                    boardEntity.getCreatedDate() ,
                    boardEntity.getB_view() );
            boardDtos.add( boardDto ); //  리스트에 저장
        }
        return boardDtos;
    };

}














