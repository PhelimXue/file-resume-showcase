<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Upload resume</title>

<script src="resource/js/jquery/1.9.1/jquery.min.js"></script>
<script src="resource/js/spark-md5.js"></script>
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

<input id="fileupload" type="file" name="files"><br>
MD5: <input id="md5_checksum" type="text" name="md5_checksum"><br>
<div id="option_bt"><input id="stop_btn" type="button" name="stop_btn" value="Cancel"> <input id="up_btn" type="button" name="up_btn" value="Upload"> </div>
<div id="progress">
    <div class="bar" style="width: 0%;"></div>
</div>
<script>
$(function () {
    $('#fileupload').fileupload({
    	url: 'upload_resume',
        dataType: 'json',
        maxChunkSize: 10 * 1024 * 1024,
		limitConcurrentUploads: 1,
        sequentialUploads: true, // 設定必須循序傳送
        singleFileUploads: true, // 限制只能傳送單檔  			            
        replaceFileInput: false,
        maxNumberOfFiles: 1,
        limitMultiFileUploads: 1,			           
        autoUpload: false,
        add: function (e, data) {            
        	data.context = $('#up_btn').remove()
        	.text('Upload')
            .appendTo('#option_bt')
            .click(function () {
            		var jqXHR = data.submit();
            		$('#stop_btn').on('click', function () {
            			jqXHR.abort();
            		});
                });
        },
        done: function (e, data) {
            $.each(data.result.files, function (index, file) {
                $('<p/>').text(file.name).appendTo(document.body);
                alert("Upload completed");
            });
        },
	    progressall: function (e, data) {
	        var progress = parseInt(data.loaded / data.total * 100, 10);
	        $('#progress .bar').css(
	            'width',
	            progress + '%'
	        );
	    },
	});
	});
	
	// 在 submit 時一起將md5的值包在form data中傳上來
	$('#fileupload').bind('fileuploadsubmit', function (e, data) {
	    data.formData = {
	    		md5: $("#md5_checksum").val(),
	    		size: this.files[0].size
	    		};
	});
	
	
	/*-- FileUpload Md5 Checksum --*/
	var blobSlice = File.prototype.slice || File.prototype.mozSlice || File.prototype.webkitSlice,         
	input,
	running = false;
	$('#fileupload').change(function(){
		input=	this;
	 	var goUpload=true;
	 	if(input.files[0]!=null&&input.files[0]!=''){
				doMd5checksum();
	        }
		});
	
	function doMd5checksum() {

        if (running) return;
        
		
        var blobSlice = File.prototype.slice || File.prototype.mozSlice || File.prototype.webkitSlice,
            file = input.files[0]; //讀取file的檔案
            chunkSize = 2097152,// read in chunks of 2MB
            chunks = Math.ceil(file.size / chunkSize),
            currentChunk = 0,
            spark = new SparkMD5.ArrayBuffer(),                       
            chunkId = null,

        frOnload = function(e) {                       

            spark.append(e.target.result);// append array buffer
            currentChunk += 1;

            if (currentChunk < chunks) {
                loadNext();
            }
            else {

                running = false;         
                var md5=spark.end(); //get md5 checksum
                console.log("md5:"+md5);
                $("#md5_checksum").val(md5);                         
            }
        },

       frOnerror = function() {
            running = false;
            alert("Oops, something went wrong");
       },

       loadNext = function() {
            var fileReader = new FileReader();
            fileReader.onload = frOnload;
            fileReader.onerror = frOnerror;

            var start = currentChunk * chunkSize,
                end = ((start + chunkSize) >= file.size) ? file.size : start + chunkSize;

            fileReader.readAsArrayBuffer(blobSlice.call(file, start, end));
        };

        running = true;
       
        loadNext();
    }
</script>
</body>
</html>