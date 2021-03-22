package dtri.com.tw.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dtri.com.tw.bean.PackageBean;
import dtri.com.tw.db.entity.SystemGroup;
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.login.LoginUserDetails;
import dtri.com.tw.service.IndexService;
import dtri.com.tw.service.PackageService;

@Controller
public class IndexController {
	// 功能
	final static String SYS_F = "index.basil";

	@Autowired
	PackageService packageService;
	@Autowired
	IndexService indexService;

	/**
	 * 登入 and 登出-畫面
	 */
	@RequestMapping(value = { "/", "/login.basil", "/index.basil", "/logout.basil" }, method = { RequestMethod.GET })
	public ModelAndView loginCheck(HttpServletRequest request) {
		System.out.println("---controller - login(logout) Check");
		// 可能有錯誤碼
		String error = request.getParameter("status");
		// 回傳-模板
		return new ModelAndView("/html/login", "status", error);
	}

	/**
	 * (初始化)主頁
	 * 
	 */
	@RequestMapping(value = { "/index.basil" }, method = { RequestMethod.POST })
	public ModelAndView indexCheck() {
		System.out.println("---controller - index(init) Check");
		PackageBean req_object = new PackageBean();
		PackageBean resp_object = new PackageBean();
		String info = null, info_color = null;
		// 取得-當前用戶資料
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			LoginUserDetails userDetails = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			// Step1.查詢資料
			SystemUser user = userDetails.getSystemUser();
			ArrayList<SystemGroup> nav = userDetails.getSystemGroup();
			resp_object = indexService.getNav(nav);
			resp_object.setInfo_user(indexService.getUserInfo(user));
		} else {
			// 取得用戶失敗
			info = PackageBean.info_message_warning + PackageBean.info_administrator;
			info_color = PackageBean.info_color_warning;
		}
		// Step2.包裝回傳
		resp_object = packageService.setObjResp(resp_object, req_object, info, info_color);

		// 回傳-模板
		return new ModelAndView("/html/main", "initMain", packageService.objToJson(resp_object));
	}

	/**
	 * (再次讀取)主頁
	 */
	@ResponseBody
	@RequestMapping(value = { "ajax/index.basil" }, method = { RequestMethod.POST })
	public String index(@RequestBody String json_object) {
		System.out.println("---controller - index(again) Check");
		PackageBean req_object = new PackageBean();
		PackageBean resp_object = new PackageBean();
		// Step1.包裝解析
		req_object = packageService.jsonToObj(new JSONObject(json_object));
		String info = null, info_color = null;
		// 取得-當前用戶資料
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			LoginUserDetails userDetails = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			// Step2.查詢資料
			SystemUser user = userDetails.getSystemUser();
			ArrayList<SystemGroup> nav = userDetails.getSystemGroup();
			resp_object = indexService.getNav(nav);
			resp_object.setInfo_user(indexService.getUserInfo(user));
		} else {
			// 取得用戶失敗
			info = PackageBean.info_message_warning + PackageBean.info_administrator;
			info_color = PackageBean.info_color_warning;
		}
		// Step2.包裝回傳
		resp_object = packageService.setObjResp(resp_object, req_object, info, info_color);

		// 回傳-模板
		return packageService.objToJson(resp_object);
	}
}
