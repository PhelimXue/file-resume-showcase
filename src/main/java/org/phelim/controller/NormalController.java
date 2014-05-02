package org.phelim.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NormalController {

	@RequestMapping("upload")
	public String uploadPage(){
		return "upload";
	}
	
	@RequestMapping("upload2")
	public @ResponseBody String uploadPage2(){
		return "upload2";
	}
}
