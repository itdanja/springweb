function notereply(  nnum ,  ncontents ){
    $("#rcontents").html( '문의내용 : ' + ncontents );
    $("#nnum").val( nnum );
    $("#notereply").modal( "show" );
}
function notereplywrite( ) {
    var nreply = $("#nreply").val();
    var nnum = $("#nnum").val( );

    $.ajax({
        url: "/member/notereplywrite",
        data : {  "nnum" : nnum , "nreply" : nreply },
        success: function(data){
            alert(data);
        }
    });
}

