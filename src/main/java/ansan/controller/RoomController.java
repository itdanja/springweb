package ansan.controller;

import ansan.domain.Entity.Room.RoomEntity;
import ansan.service.RoomService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
                                  @RequestParam("file") List<MultipartFile> files ,
                                  @RequestParam("addressy") Double addressy ,
                                  @RequestParam("addressx") Double addressx){

        roomEntity.setRaddress( roomEntity.getRaddress()+"," + addressy + "," + addressx  );

        roomService.write( roomEntity , files );
        return  "main";
    }

    // 룸 보기 페이지 이동
    @GetMapping("/roomview") // 이동
    public String roomview(){
        return "room/roomview"; // 타임리프 반환 [ 앞에 / 제거 ]
    }


    // json 반환 [ 지도에 띄우고자 하는 방 응답하기 ]
    @GetMapping("/chicken.json")
    @ResponseBody
    public JSONObject chicken( ){

        // Map <---> Json [  키 : 값 ] => 엔트리
            // 중첩이 가능하다 .

        // {"positions": [ {"lat": 37.27943075229118,lng": 127.01763998406159} }
        // { "키" : 리스트{ "키": 값1 , "키":값2 , "키": 값3 } }
        // jsonObject = { "positions" : jsonArray{ "키": 값1 , "키":값2 , "키": 값3 } }
            // map = { 키 : 값 }
        //   map객체 = { "키" : List[ map객체 , map객체 , map객체  ]  }

        JSONObject jsonObject = new JSONObject(); // json 전체 [ 응답 용 ]
        JSONArray jsonArray = new JSONArray(); // json 안에 들어가는 리스트

        List<RoomEntity>roomlist = roomService.getroomlist();   // 모든 방 [ 위도 , 경도 포함 ]
        for( RoomEntity roomEntity : roomlist ){ // 모든 방에서 하나씩 반복문 돌리기

            JSONObject data = new JSONObject(); // 리스트 안에 들어가는 키:값
            data.put("lat" , roomEntity.getRaddress().split(",")[1] );      //  주소[0],위도[1],경도[2]
            data.put("lng" , roomEntity.getRaddress().split(",")[2] );      //  주소[0],위도[1],경도[2]
            data.put("rnum" , roomEntity.getRnum() );
            jsonArray.add( data ); // 리스트에 저장

        }

        jsonObject.put("positions" ,  jsonArray );  // json 전체에 리스트 넣기

        return jsonObject;
    }

    // 방번호를 이용한 방정보 html 반환
    @GetMapping("/getroom")
    public String getroom(@RequestParam("rnum") int rnum , Model model){
        model.addAttribute( "room" ,  roomService.getroom(rnum) ) ;
        return "room/room"; // room html 반환
    }

    // 문의 등록
    @GetMapping("/notewrite")
    @ResponseBody
    public String notewrite ( @RequestParam("rnum") int rnum ,
                              @RequestParam("ncontents") String ncontents ) {

        boolean result = roomService.notewrite( rnum , ncontents );
        if( result ){ return "1"; }
        else{ return "2"; }

    }






















}
