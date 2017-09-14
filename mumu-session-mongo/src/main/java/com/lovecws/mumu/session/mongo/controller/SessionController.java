package com.lovecws.mumu.session.mongo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/session")
public class SessionController {

	@ResponseBody
	@RequestMapping(method=RequestMethod.GET)
	public Object info(HttpSession session) {
		return session;
	}
}
