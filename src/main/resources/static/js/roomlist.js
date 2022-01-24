
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