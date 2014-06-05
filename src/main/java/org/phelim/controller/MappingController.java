package org.phelim.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MappingController {

	// 簡易上傳
	@RequestMapping(value = "upload_simple", method = RequestMethod.GET)
	public String uploadSimple() {
		return "upload_simple";
	}
	
	// 上傳續傳
	@RequestMapping(value = "upload_resume", method = RequestMethod.GET)
	public String uploadResume() {
		return "upload_resume";
	}
}
