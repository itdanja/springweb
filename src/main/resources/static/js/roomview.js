    var map = new kakao.maps.Map(document.getElementById('map'), { // 지도를 표시할 div
        center : new kakao.maps.LatLng(36.2683, 127.6358), // 지도의 중심좌표
        level : 12 // 지도의 확대 레벨
    });

    var clusterer = new kakao.maps.MarkerClusterer({
        map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
        averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
        minLevel: 10, // 클러스터 할 최소 지도 레벨
        disableClickZoom: true // 클러스터 마커를 클릭했을 때 지도가 확대되지 않도록 설정한다
    });


            //   // 통신  json 받을 url 매핑[요청]          //  응답
    $.get("/room/chicken.json", function(data) {
        var markers = $(data.positions).map(function(i, position) {
        var marker = new kakao.maps.Marker({
                position : new kakao.maps.LatLng(position.lat, position.lng)
            });

                  kakao.maps.event.addListener(marker, 'click', function() {   // 마커에 클릭 이벤트를 등록한다 (우클릭 : rightclick)

                        // 사이드바 열기 < js에서  자동으로 클릭 이벤트 실행 하기 >
                        $("#sidebartoggle").trigger('click');

                            // 사이드바 내용물에 html 넣기
                            $.ajax({
                                url: '/room/getroom' ,
                                data : { "rnum" : position.rnum } ,
                                success: function(data) {
                                    $("#contents").html(data);
                                }
                            });
                                /*     		var customOverlay = new kakao.maps.CustomOverlay({    // 커스텀 오버레이를 생성하고 지도에 표시한다 [
                                                map: map,
                                                content: "<div style='padding:0 5px;background:#fff;'> 방 등록번호 : "+position.rnum+"</div>" ,
                                                position: new kakao.maps.LatLng(position.lat, position.lng), // 커스텀 오버레이를 표시할 좌표
                                                xAnchor: 0.5, // 컨텐츠의 x 위치
                                                yAnchor: 0 // 컨텐츠의 y 위치
                                            });*/

                  });
            return marker;
        });
        clusterer.addMarkers(markers);   // 클러스터러에 마커들을 추가합니다
    });

    kakao.maps.event.addListener(clusterer, 'clusterclick', function(cluster) { // 클러스터 클릭 이벤트

        // 현재 지도 레벨에서 1레벨 확대한 레벨
        var level = map.getLevel()-1;

        // 지도를 클릭된 클러스터의 마커의 위치를 기준으로 확대합니다
        map.setLevel(level, {anchor: cluster.getCenter()});
    });


// 문의 버튼 클릭 이벤트
function notewrite( rnum ){

    alert( rnum );
    alert( $("#ncontents").val() );

}













