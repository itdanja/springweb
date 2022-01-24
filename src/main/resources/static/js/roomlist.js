
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