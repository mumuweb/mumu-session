package com.lovecws.mumu.session.redis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/session")
public class SessionController {

	@ResponseBody
	@RequestMapping(method=RequestMethod.GET)
	public Object info(HttpSession session) {
		return session;
	}
}
