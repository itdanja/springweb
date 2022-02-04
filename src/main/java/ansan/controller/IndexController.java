package ansan.controller;

import ansan.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    // 메인페이지 매핑[ 연결 ]
    @GetMapping("/")
    public String main(){
        return "main";
    }

//    @GetMapping("/error")
//    public String error(){
//        return "error";
//    }

    @Autowired
    private RoomService roomService;

    // 안읽은 쪽지의 개수 세기
    @GetMapping("/nreadcount")
    @ResponseBody
    public void nreadcount(){
        roomService.nreadcount();
    }



}
