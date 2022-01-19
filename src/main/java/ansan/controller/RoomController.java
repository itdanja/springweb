package ansan.controller;

import ansan.domain.Entity.Room.RoomEntity;
import ansan.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller // view <--- ---> Controller [ 매핑 ]
@RequestMapping("/room")    // 중복 url
public class RoomController {
    @Autowired
    private RoomService roomService;
    @GetMapping("/write") // 이동
    public String write(){
        return "room/roomwrite"; // 타임리프 반환 [ 앞에 / 제거 ]
    }
    @PostMapping("/writecontroller") // 처리
    public String writecontroller(RoomEntity roomEntity ,
                                  @RequestParam("file") List<MultipartFile> files){
        roomService.write( roomEntity , files );
        return  "main";
    }

}
