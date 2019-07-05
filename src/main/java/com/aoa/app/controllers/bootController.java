package com.aoa.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class bootController {

		@RequestMapping(value = "/" ,produces = "text/html;charset=UTF-8")
		public String home() {

			return "index.html";
		}
}