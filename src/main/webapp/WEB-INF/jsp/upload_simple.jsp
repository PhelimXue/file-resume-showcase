<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Upload test</title>

<script src="resource/js/jquery/1.9.1/jquery.min.js"></script>
<script src="resource/js/jquery-fileupload/vendor/jquery.ui.widget.js"></script>
<script src="resource/js/jquery-fileupload/jquery.iframe-transport.js"></script>
<script src="resource/js/jquery-fileupload/jquery.fileupload.js"></script>

<style>
.bar {
    height: 18px;
    background: green;
}
</style>
</head>
<body>

<input id="fileupload" type="file" name="files" multiple><br>
<input id="up_btn" type="button" name="up_btn" value="Upload"> <input id="stop_btn" type="button" name="stop_btn" value="Cancel">
<div id="progress">
    <div class="bar" style="width: 0%;"></div>
</div>
<script>
$(function () {
    $('#fileupload').fileupload({
    	url: '/file-resume-showcase/upload_simple',
        dataType: 'json',
        add: function (e, data) {            
            $("#up_btn").on('click', function () {
            	var jqXHR = data.submit();
            	$('#stop_btn').on('click', function () {
        			jqXHR.abort();
        		});
            });
        },
        done: function (e, data) {
            $.each(data.result.files, function (index, file) {
                $('<p/>').text(file.name).appendTo(document.body);
            });
        },
	    progressall: function (e, data) {
	        var progress = parseInt(data.loaded / data.total * 100, 10);
	        $('#progress .bar').css(
	            'width',
	            progress + '%'
	        );
	    }
	});
	});
</script>
</body>
</html>