package ansan.controller;

import ansan.domain.Dto.BoardDto;
import ansan.domain.Dto.MemberDto;
import ansan.domain.Entity.Board.BoardEntity;
import ansan.domain.Entity.Board.ReplyEntitiy;
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
import java.io.*;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
            String uuidfile = null;
            if( !file.getOriginalFilename().equals("") ) { // 첨부파일이 있을때

                // 파일이름 중복배제 [ UUID : 고유 식별자 ]
                UUID uuid = UUID.randomUUID(); // 고유 식별자 객체 난수생성 메소드 호출
                // 사용자가 만약에 파일명에 _ 존재하면 - 변경
                String OriginalFilename = file.getOriginalFilename();
                uuidfile = uuid.toString() + "_" + OriginalFilename.replaceAll("_" , "-");
                // 고유 식별자 _ 파일명

                // 파일업로드 [  JSP ( COS 라이브러리 ) -> SPRING (MultipartFile 인터페이스 ) ]
                String dir = "D:\\web0928\\springweb\\src\\main\\resources\\static\\upload";
                String filepath = dir + "\\" + uuidfile;  // 저장 경로 +  form에서 첨부한 파일이름 호출
                // file.getOriginalFilename(); : form 첨부파일 호출
                file.transferTo(new File(filepath)); // transferTo : 파일 저장 [ 예외 처리 ]

            }else{ // 첨부파일 없을때
                uuidfile = null;
            }
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

        try{
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(  b_img.split("_")[1]  , "UTF-8") ); // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더
                                                                                                                                        // 영문X 한글[ URLEncoder.encode( 파일명 , "UTF-8")
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
        // 첨부파일 존재하면 uuid가 제거된 파일명 변환해서 b_realimg 담기
        if( boardDto.getB_img() != null ) boardDto.setB_realimg( boardDto.getB_img().split("_")[1] );
        model.addAttribute( "boardDto" , boardDto  );

        // 해당 게시물번호의 댓글 호출
       List<ReplyEntitiy> replyEntitiys =  boardService.getreplylist( b_num);

       // 정렬후 -> 내림차순 [ 댓글번호 ]
        Collections.reverse( replyEntitiys );

        model.addAttribute( "replyEntitiys" , replyEntitiys  );

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

        // 첨부파일 존재하면 uuid가 제거된 파일명 변환해서 b_realimg 담기
        if( boardDto.getB_img() != null ) boardDto.setB_realimg( boardDto.getB_img().split("_")[1] );

        model.addAttribute("boardDto" , boardDto );

        return "board/boardupdate"; // html 열기
    }
    // 수정 처리
    @PostMapping("/board/boardcontroller")
    public String boardcontroller( @RequestParam("b_newimg") MultipartFile file
                                    , @RequestParam("b_num") int b_num
                                    , @RequestParam("b_title") String b_title
                                    , @RequestParam("b_contents") String b_contents
                                    , @RequestParam("b_img") String b_img){

        if( ! file.getOriginalFilename().equals("") ) { // 변경된 첨부파일 존재
            try {
                UUID uuid = UUID.randomUUID();
                String uuidfile = uuid.toString() + "_" + file.getOriginalFilename().replaceAll("_", "-");

                String dir = "D:\\web0928\\springweb\\src\\main\\resources\\static\\upload";
                String filepath = dir + "\\" + uuidfile;  // 저장 경로 +  form에서 첨부한 파일이름 호출
                file.transferTo(new File(filepath)); // transferTo : 파일 저장 [ 예외 처리 ]
               boardService.update(
                        BoardDto.builder().b_num(b_num).b_title(b_title).b_contents(b_contents).b_img( uuidfile ) .build()  );
            }
            catch ( Exception e ) {
                System.out.println( e );
            }

        }else{   // 변경된 첨부파일 없음 -> 기존파일 그대로 사용
            boardService.update(
                    BoardDto.builder().b_num(b_num).b_title(b_title).b_contents(b_contents).b_img( b_img ) .build()  );
        }
        return "redirect:/board/boardview/"+b_num;
    }

    @GetMapping("/board/replywirte")
    @ResponseBody
    public String replywirte( @RequestParam("bnum") int bnum  ,
                              @RequestParam("rcontents") String rcontents  ){

        HttpSession session = request.getSession();
        MemberDto memberDto =
                (MemberDto)session.getAttribute("logindto");

        if( memberDto == null  ){ // 로그인 세션이 없으면
            return "2";
        }

        boardService.replywirte( bnum , rcontents , memberDto.getM_id()  );
                                            // 게시물번호 , 댓글내용 , (로그인된)아이디디
       return "1" ;
    }

}
