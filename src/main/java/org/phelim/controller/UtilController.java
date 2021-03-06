package org.phelim.controller;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.phelim.model.ContentRange;
import org.phelim.model.MD5CheckSum;
import org.phelim.model.ResponseMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class UtilController {
	
	private static Logger log = LoggerFactory.getLogger("C");
	
	@Value("${file.tmp.path}")
	private String tmpDirectory; // 暫儲的路徑
	
	@Value("${file.finish.path}")
	private String saveDirectory; // 真實儲存的路徑
	
	// 一般傳輸
	@RequestMapping(value = "upload_simple", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String fileUpload(@RequestParam("files") MultipartFile file) throws Exception {
		log.debug("save file " + file.getOriginalFilename());
		createDir(new File(tmpDirectory));
		createDir(new File(saveDirectory));
		file.transferTo(new File(saveDirectory + file.getOriginalFilename()));
		return file.getName();
	}
	
	
	// 有判斷檔案的續傳傳輸
	@RequestMapping(value = "upload_resume", method = RequestMethod.POST)
	@ResponseBody
	public String fileUpload(HttpServletRequest request, HttpServletResponse response, @RequestParam("files") MultipartFile file,
			@RequestParam("md5") String md5 ,@RequestParam("size")long filesize) throws Exception {

		createDir(new File(tmpDirectory));
		createDir(new File(saveDirectory));
		File tmpFile = new File(tmpDirectory + md5);
		File saveFile = new File(saveDirectory + file.getOriginalFilename());
		ResponseMeta meta = new ResponseMeta();
		
		log.debug("*************************");
		log.debug("@ file size: "+filesize);
		log.debug("@ tmp size: "+tmpFile.length());
		// 判斷暫存區內是否有檔案 有則開啟續傳模式 無則使用一般寫入
		if (!tmpFile.exists()) {
			// new file
			log.debug("@ new file");
			file.transferTo(tmpFile);
			response.setHeader("Range", "0-" + (file.getSize() - 1));
			meta.setName(file.getOriginalFilename());
			meta.setSize(tmpFile.length());
		}else if (tmpFile.length() >= filesize) {
			// 檔案已經完整的存在檔案內
			log.debug("@ full file");
			meta.setName(file.getOriginalFilename());
			meta.setSize(tmpFile.length());
		}else {
			// resume file
			log.debug("@ resume file");
			long startPosition = 0;
			long endPosition = 0;
			// 取得ContentRange e.g bytes 0-19999999/209715200
			// 並定義出 起始值 startPosition 和 終值 endPosition
			String range = (String)request.getHeader("Content-Range");
			log.debug("Content-Range: " + range);
			if (range != null) {
				ContentRange contentRange = new ContentRange(range);
				startPosition = contentRange.getStartPosition();
				endPosition = contentRange.getEndPosition();
			}else {
				meta.setError("could not found range");
				response.setStatus(400);
				return meta.output();
			}
			
			// 檔案寫入主體 只有在 startPosition <= localFile < endPosition 時才執行
			long localFileSize = tmpFile.length();
			if(startPosition >= localFileSize && localFileSize < endPosition){
				// 建立續傳寫入
				RandomAccessFile oSavedFile = new RandomAccessFile(
						tmpFile, "rw");

				InputStream fis = file.getInputStream();

				// 作續傳寫入
				oSavedFile.seek(localFileSize);
				byte[] b = new byte[1024];
				int nRead;
				while ((nRead = fis.read(b, 0, 1024)) > 0) {
					oSavedFile.write(b, 0, nRead);
				}
				fis.close();
				oSavedFile.close();
				meta.setName(file.getOriginalFilename());
				meta.setSize(tmpFile.length());
			}
		}
		
		// 作檔案傳輸後的判斷
		if (tmpFile.length() >= filesize) {
			response.setHeader("Range", "0-" + (tmpFile.length()-1));
			boolean check = md5Check(tmpFile, md5);
			if (check) {
				tmpFile.renameTo(saveFile);
				log.debug("@ Upload completed");
			}else {
				meta.setError("checksum fail");
				response.setStatus(417);
			}
		}else if (tmpFile.length() < filesize) {
			meta.setName(file.getOriginalFilename());
			meta.setSize(tmpFile.length());
			response.setHeader("Range", "0-" + (tmpFile.length()-1));
		}
		return meta.output();
	}

	// 建立儲存路徑
	private void createDir(File file) throws Exception {
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
	// MD5 Checksum
	private boolean md5Check(File tmpFile,String md5) throws Exception{
		System.out.print("@ MD5 Check sum: ");
		MD5CheckSum fileMD5 = new MD5CheckSum(tmpFile);
		if (fileMD5.getHex().equalsIgnoreCase(md5)) {
			log.debug("true");
			return true;
		}else {
			log.debug("false");
			tmpFile.delete();
			return false;
		}
	}
	
}
