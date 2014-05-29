package org.phelim.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NormalController {

	@RequestMapping(value = "upload_simple", method = RequestMethod.GET)
	public String uploadSimple() {
		return "upload_simple";
	}
	
	@RequestMapping("upload2")
	public @ResponseBody String uploadPage2(){
		return "upload2";
	}
}
