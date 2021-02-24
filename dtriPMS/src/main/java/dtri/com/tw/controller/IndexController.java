package dtri.com.tw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
	// 功能
	final static String SYS_F = "index.basil";

	/**
	 * 登入
	 */
	@RequestMapping(value = "/login.basil", method = { RequestMethod.GET })
	public String loginCheck() {
		System.out.println("---controller - login Check");
		return "/html/login";
	}
	/**
	 * 主頁
	 */
	@RequestMapping(value = "/index.basil", method = { RequestMethod.POST })
	public ModelAndView indexCheck() {
		System.out.println("---controller - index Check");
		return new ModelAndView("/html/main", "resp_content", 123);
	}
}
