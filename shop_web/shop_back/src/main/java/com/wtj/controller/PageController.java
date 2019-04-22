package com.wtj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/toPage")
public class PageController {
	@RequestMapping("/{toPage}")
	public String toPage1(@PathVariable("toPage") String toPage){
		return toPage;
	}
	@RequestMapping("/{toPage}/{page}")
	public String toPage2(@PathVariable("toPage") String toPage,@PathVariable("page") String page){
		return toPage+"/"+page;
	}
}
