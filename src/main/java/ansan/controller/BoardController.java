package ansan.controller;

import ansan.domain.Dto.BoardDto;
import ansan.domain.Dto.MemberDto;
import ansan.domain.Entity.Board.BoardEntity;
import ansan.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Controller
public class BoardController {

    @Autowired
    BoardService boardService;  // boardService 메소드 호출용 객체

    @Autowired  // 빈 생성
    HttpServletRequest request;     // 요청 객체

    @Autowired
    HttpServletResponse response; // 응답 객체


    // 게시물 전체 목록 페이지 이동
    @GetMapping("/board/boardlist")
    public String boardlist(Model model , @PageableDefault Pageable pageable){

        /* 검색 서비스 */
        String keyword=request.getParameter("keyword");
        String search =request.getParameter("search");

        HttpSession session = request.getSession();

        if( keyword!=null || search!=null ){
            session.setAttribute("keyword" , keyword);
            session.setAttribute("search" , search);
        }else{
            keyword =  (String) session.getAttribute("keyword");
            search =   (String)  session.getAttribute("search");
        }

        Page<BoardEntity> boardDtos = boardService.boardlist( pageable , keyword , search );

        model.addAttribute( "BoardDtos" , boardDtos  );

        System.out.println( "페이지넘버 : " + boardDtos.getNumber() );
        return "board/boardlist" ;  // 타임리프 를 통한 html 반환

    }
    // 게시물 쓰기 페이지 이동
    @GetMapping("/board/boardwrite")
    public String boardwrite(){
        return "board/boardwrite";
    }


    // 게시물 쓰기 처리
    @PostMapping("/board/boardwritecontroller")
    @ResponseBody
    public int boardwritecontroller( @RequestParam("b_img") MultipartFile file  )  {
        try {
            // 파일이름 중복배제 [ UUID : 고유 식별자 ]
            UUID uuid = UUID.randomUUID(); // 고유 식별자 객체 난수생성 메소드 호출

            String uuidfile = uuid.toString() + "_" + file.getOriginalFilename();
            // 고유 식별자 _ 파일명

            // 파일업로드 [  JSP ( COS 라이브러리 ) -> SPRING (MultipartFile 인터페이스 ) ]
            String dir = "D:\\web0928\\springweb\\src\\main\\resources\\static\\upload";
            String filepath = dir + "\\" + uuidfile;  // 저장 경로 +  form에서 첨부한 파일이름 호출
            // file.getOriginalFilename(); : form 첨부파일 호출
            file.transferTo(new File(filepath)); // transferTo : 파일 저장 [ 예외 처리 ]

            // 세션 선언
            HttpSession session = request.getSession();
            // 세션 호출
            MemberDto memberDto = (MemberDto) session.getAttribute("logindto");

            BoardDto boardDto = BoardDto.builder().
                    b_title(request.getParameter("b_title"))
                    .b_contents(request.getParameter("b_contents"))
                    .b_write(memberDto.getM_id())
                    .b_img(uuidfile).build();

            boardService.boardwrite(boardDto);
            /*return "redirect:/board/boardlist"; // 글쓰기 성공시 게시판 목록이동*/
            return 1;
        }
        catch ( Exception e ) {
            return 2;
        }

    }

    // 첨부파일 다운로드 처리
    @GetMapping("/board/filedownload")
    public void filedownload( @RequestParam("b_img") String b_img , HttpServletResponse response ){

        // 첨부파일 경로 + 파일이름
        String path = "D:\\web0928\\springweb\\src\\main\\resources\\static\\upload\\"+b_img;
        // 객체화
    try{
            response.setHeader("Content-Disposition", "attachment;filename=" + b_img.split("_")[1]  ); // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더

            FileInputStream fileInputStream = new FileInputStream(path); // 파일 읽어오기
            OutputStream out = response.getOutputStream(); // 출력 스트림

            int read = 0;
            byte[] buffer = new byte[1024];
            while ((read = fileInputStream.read(buffer)) != -1) { // 1024바이트씩 계속 읽으면서 outputStream에 저장, -1이 나오면 더이상 읽을 파일이 없음
                out.write(buffer, 0, read);
            }
        }
        catch ( Exception e ) {

        }

    }

    // 게시물 보기 페이지 이동
    @GetMapping("/board/boardview/{b_num}") // GET 방식으로 URL 매핑[연결]
    public String boardview( @PathVariable("b_num") int b_num , Model model  ){

        BoardDto boardDto =  boardService.getboard( b_num );

        boardDto.setB_realimg( boardDto.getB_img().split("_")[1] );

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
