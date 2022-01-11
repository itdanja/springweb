package ansan.controller;

import ansan.domain.Dto.BoardDto;
import ansan.domain.Dto.MemberDto;
import ansan.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Controller
public class BoardController {

    @Autowired
    BoardService boardService;  // boardService 메소드 호출용 객체

    // 게시물 전체 목록 페이지 이동
    @GetMapping("/board/boardlist")
    public String boardlist( Model model ){
        ArrayList<BoardDto> boardDtos = boardService.boardlist();
        model.addAttribute( "BoardDtos" , boardDtos  );
        return "board/boardlist" ;  // 타임리프 를 통한 html 반환

    }
    // 게시물 쓰기 페이지 이동
    @GetMapping("/board/boardwrite")
    public String boardwrite(){
        return "board/boardwrite";
    }

    @Autowired  // 빈 생성
    HttpServletRequest request;

    // 게시물 쓰기 처리
    @PostMapping("/board/boardwritecontroller")
    public String boardwritecontroller(  BoardDto boardDto) {

        // 세션 선언
        HttpSession session = request.getSession();
        // 세션 호출
        MemberDto memberDto =  (MemberDto) session.getAttribute( "logindto");
        boardDto.setB_write( memberDto.getM_id() );

        boardService.boardwrite( boardDto );
        return "redirect:/board/boardlist"; // 글쓰기 성공시 게시판 목록이동
    }

    // 게시물 보기 페이지 이동
    @GetMapping("/board/boardview/{b_num}") // GET 방식으로 URL 매핑[연결]
    public String boardview( @PathVariable("b_num") int b_num , Model model  ){

        BoardDto boardDto =  boardService.getboard( b_num );

        model.addAttribute( "boardDto" , boardDto  );

        return "board/boardview";
                // 타임리프를 이용한 html 반환
    }
    // 게시물 삭제 처리
    @GetMapping("/board/boarddelete")
    @ResponseBody
    public int boarddelete( @RequestParam("b_num") int b_num ){

        boolean result = boardService.delete( b_num );
        if( result ){ return 1; }
        else{ return 2; }

    }

    // @GetMapping( "/board/boardupdate/{변수}/{변수}/{변수}" )
    // 수정페이지 이동
    @GetMapping( "/board/boardupdate/{b_num}")
    public String boardupdate( @PathVariable("b_num") int b_num , Model model ){

        BoardDto boardDto = boardService.getboard( b_num );
        model.addAttribute("boardDto" , boardDto );

        return "board/boardupdate"; // html 열기
    }
    // 수정 처리
    @PostMapping("/board/boardcontroller")
    public String boardcontroller( BoardDto boardDto ){

        System.out.println( boardDto.getB_num() );

        boolean result =  boardService.update( boardDto );

        return "main";
    }


}
