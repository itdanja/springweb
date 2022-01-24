
function rdelete( rnum ){
    $.ajax({
        url: "/admin/delete" ,
        data : { "rnum" : rnum } ,
        success: function(data) {
            if( data = 1 ){
                location.href="/admin/roomlist";
            }
        }
    });
}

function activeupdate( rnum , upactive  ){
    $.ajax({
        url: "/admin/activeupdate" ,
        data : { "rnum" : rnum , "upactive" : upactive } ,
        success: function(data) {
            if( data = 1 ){
                location.href="/admin/roomlist";
            }else{
                $("#activemsg").html("현재 동일한 상태 입니다. ");
            }
        }
    });
}

function roomupdate(field , filecontents , rnum){

    if( field == 'raddress'  ){ // 주소 변경
        alert("주소 버튼 클릭");
        $("#updatemsg").html(
         '<input type="text" id="sample5_address" placeholder="주소" name="raddress" class="form-control">' +
         '<input type="button" onclick="sample5_execDaumPostcode()" value="주소 검색" class="form-control"><br>' +
         '<div id="map" style="width:300px;height:300px;margin-top:10px;display:none"></div>' +
         '<input type="hidden" name="addressy"  id="addressy">' +
         '<input type="hidden" name="addressx"  id="addressx">' +
         '<button onclick=update("+rnum+")>수정</button> </div>'
         );

    }else if( field == 'rimg' ){ // 이미지 변경
        alert("이미지버튼 클릭 ");
    }
    else{
         $("#updatemsg").html(
               "<p> 내용수정후 수정버튼 눌러주세요! </p> "+
               "<div> <input type='hidden' value="+field+" id='field' >  </div>" +
               "<div> <input type='text' value="+filecontents+" id='newcontents' > " +
               "<button onclick=update("+rnum+")>수정</button> </div>"
         );
    }
}
function update( rnum ) {
    var newcontents = $("#newcontents").val();
     var field = $("#field").val();
     $.ajax({
          url: "/admin/update" ,
          data : { "rnum" : rnum , "field" : field , "newcontents" : newcontents  } ,
          success : function(data){
              if( data == 1 ){
                  location.href="/admin/roomlist";
              }else{
                  $("#deletemsg").html("[방 수정 실패].");
              }
          }
      });
}


// 주소
  function sample5_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                var addr = data.address; // 최종 주소 변수

                // 주소 정보를 해당 필드에 넣는다.
                document.getElementById("sample5_address").value = addr;
                // 주소로 상세 정보를 검색
                geocoder.addressSearch(data.address, function(results, status) {
                    // 정상적으로 검색이 완료됐으면
                    if (status === daum.maps.services.Status.OK) {

                        var result = results[0]; //첫번째 결과의 값을 활용

                        // 해당 주소에 대한 좌표를 받아서
                        var coords = new daum.maps.LatLng(result.y, result.x);

                            // html에 좌표 저장한다 [ db에 저장하기 위해서 ]
                            $("#addressy").val( result.y);
                            $("#addressx").val( result.x);

                        // 지도를 보여준다.
                        mapContainer.style.display = "block";
                        map.relayout();
                        // 지도 중심을 변경한다.
                        map.setCenter(coords);
                        // 마커를 결과값으로 받은 위치로 옮긴다.
                        marker.setPosition(coords)
                    }
                });
            }
        }).open();
    }








