package ansan.controller;

import ansan.domain.Dto.BoardDto;
import ansan.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

@Controller
public class BoardController {

    @Autowired
    BoardService boardService;  // boardService 메소드 호출용 객체

    // http url 연결
    @GetMapping("/board/boardlist")
    public String boardlist( Model model ){

        ArrayList<BoardDto> boardDtos = boardService.boardlist();
        model.addAttribute( "BoardDtos" , boardDtos  );
        return "board/boardlist" ;  // 타임리프 를 통한 html 반환

    }
    @GetMapping("/board/boardwrite")
    public String boardwrite(){
        return "board/boardwrite";
    }
    @PostMapping("/board/boardwritecontroller")
    public String boardwritecontroller(BoardDto boardDto){
        boardService.boardwrite( boardDto );
        return "redirect:/board/boardlist"; // 글쓰기 성공시 게시판 목록이동
    }

}
