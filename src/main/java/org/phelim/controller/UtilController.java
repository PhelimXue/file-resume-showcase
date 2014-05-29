package org.phelim.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UtilController {

	private String savePath = "/tmp/";
	
	@RequestMapping(value = "upload_simple", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String fileUpload(@RequestParam("files") MultipartFile file) throws IllegalStateException, IOException {
		System.out.println("save file " + file.getOriginalFilename());
		file.transferTo(new File(savePath + file.getOriginalFilename()));
		return file.getName();
	}
}
