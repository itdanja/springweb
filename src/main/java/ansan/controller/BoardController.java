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

    @Autowired  // 빈 생성
    HttpServletRequest request;


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


    // 게시물 쓰기 처리
    @PostMapping("/board/boardwritecontroller")
    public String boardwritecontroller( @RequestParam("b_img") MultipartFile file ) throws IOException {

        // 파일업로드 [  JSP ( COS 라이브러리 ) -> SPRING (MultipartFile 인터페이스 ) ]
        String dir = "D:\\web0928\\springweb\\src\\main\\resources\\static\\upload";
        String filepath = dir + "\\" + file.getOriginalFilename();  // 저장 경로 +  form에서 첨부한 파일이름 호출
        // file.getOriginalFilename(); : form 첨부파일 호출
        file.transferTo( new File(filepath) ); // transferTo : 파일 저장 [ 예외 처리 ]

        // 세션 선언
        HttpSession session = request.getSession();
        // 세션 호출
        MemberDto memberDto =  (MemberDto) session.getAttribute( "logindto");

        BoardDto boardDto = BoardDto.builder().
                b_title(  request.getParameter("b_title") )
                        .b_contents( request.getParameter("b_contents") )
                                .b_write( memberDto.getM_id() )
                                        .b_img( file.getOriginalFilename() ).build();

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
                                                    // 자동 주입 사용

        // 자동 주입 미사용
/*        int b_num = Integer.parseInt( request.getParameter("b_num") );
        String b_title = request.getParameter("b_title")  ;
        String b_contents = request.getParameter("b_contents")  ;
        BoardDto boardDto = new BoardDto();
        boardDto.setB_num( b_num );
        boardDto.setB_title( b_title );
        boardDto.setB_contents( b_contents );
        System.out.println( boardDto.toString() );*/

        boolean result =  boardService.update( boardDto );

       /* return "반환타입";       // 일반java :  반환타입이 반환
        return "html파일명"; // 타임리프가 해당 html 반환
        return "객체명"; // @ResponseBody 사용시 객체 반환
        return "redirect : URL "; // 해당 URL 반환*/
        return "redirect:/board/boardview/"+boardDto.getB_num();

    }


}
