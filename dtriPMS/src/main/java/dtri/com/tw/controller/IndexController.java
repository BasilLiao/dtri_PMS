package dtri.com.tw.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
	// 功能
	final static String SYS_F = "index.basil";

	/**
	 * 登入-畫面
	 */
	@RequestMapping(value = { "/login.basil","/index.basil"}, method = { RequestMethod.GET })
	public ModelAndView loginCheck(HttpServletRequest request) {
		System.out.println("---controller - login Check");
		String error = request.getParameter("status");
		//return "/html/login";
		return new ModelAndView("/html/login", "status", error);
	}

	/**
	 * 主頁
	 */
	@RequestMapping(value = "/index.basil", method = { RequestMethod.POST })
	public ModelAndView indexCheck() {
		System.out.println("---controller - index Check");
		return new ModelAndView("/html/main", "resp_content", 123);
	}

	/**
	 * 主頁
	 */
//	@RequestMapping(value = "/ajax/index2.basil", method = { RequestMethod.POST })
//	public ModelAndView indexCheckd2() {
//		System.out.println("---controller - index Check2");
//		return new ModelAndView("/html/main", "resp_content", 456);
//	}
}
