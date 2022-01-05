package ansan.controller;


import ansan.domain.Dto.MemberDto;
import ansan.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MemberController {

    @Autowired
    MemberService memberService ;

    @GetMapping("/member/login")    // 로그인페이지 연결
    public String login(){
        return "member/login";
    }

    @GetMapping("/member/signup") // 회원가입페이지 연결
    public String signup(){
        return "member/signup";
    }
    @PostMapping("/member/signupcontroller") // 회원가입 처리 연결
    public String signupcontroller(MemberDto memberDto){
     // 자동주입 : form 입력한 name 과 dto의 필드명 동일하면 자동주입 // 입력이 없는 필드는 초기값[ 문자=null , 숫자 = 0 ]
        memberService.membersignup(memberDto);
        return "redirect:/";  // 회원가입 성공시 메인페이지 연결
    }
    @PostMapping("/member/logincontroller")
    @ResponseBody
    public String logincontroller( @RequestBody MemberDto memberDto){
                                             // 폼 사용시에는 자동주입 O
                                             // AJAX 사용시에는 자동주입 X -> @RequestBody
          MemberDto loginDto =   memberService.login( memberDto );
          if( loginDto !=null ){
              System.out.println("Login success");
              return "1";
          }else{
              System.out.println("Login fail");
              return "2";
          }
            // 타임리프를 설치했을경우  RETRUN URL , HTML
            // html 혹은 url 아닌 값 반환할때  @ResponseBody
    }

}
