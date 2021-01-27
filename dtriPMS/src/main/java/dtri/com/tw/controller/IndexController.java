package dtri.com.tw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
	// 功能
	final static String SYS_F = "index.do";

	/**
	 * 第一次登入
	 */
	@RequestMapping(value = "/index.dtr", method = { RequestMethod.GET })
	public ModelAndView loginCheck() {
		System.out.println("---controller - loginCheck");
		return new ModelAndView("main", "allData", null);
	}
}
