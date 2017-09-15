package com.lovecws.mumu.session.hazelcast.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/session")
public class SessionController {

	@ResponseBody
	@RequestMapping(method=RequestMethod.GET)
	public Object info(HttpSession session) {
		return session;
	}
}
