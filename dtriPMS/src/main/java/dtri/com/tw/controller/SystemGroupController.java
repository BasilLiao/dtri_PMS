package dtri.com.tw.controller;

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

import dtri.com.tw.bean.PackageBean;
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.login.LoginUserDetails;
import dtri.com.tw.service.PackageService;
import dtri.com.tw.service.SystemGroupService;

@Controller
public class SystemGroupController {
	// 功能
	final static String SYS_F = "sys_group.basil";

	@Autowired
	PackageService packageService;
	@Autowired
	SystemGroupService groupService;

	/**
	 * 訪問
	 */
	@ResponseBody
	@RequestMapping(value = { "/ajax/system_group.basil" }, method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String sysGroupAccess(@RequestBody String json_object) {
		System.out.println("---controller - sysGroupAccess Check");
		// 取得-當前用戶資料
		SystemUser user = new SystemUser();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			LoginUserDetails userDetails = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// Step1.查詢資料
			user = userDetails.getSystemUser();
		}
		PackageBean req = new PackageBean();
		PackageBean resp = new PackageBean();
		String info = null, info_color = null;
		System.out.println(json_object);
		// Step1.包裝解析
		req = packageService.jsonToObj(new JSONObject(json_object));
		// Step2.進行查詢
		resp = groupService.getData(req.getBody(), req.getPage_batch(), req.getPage_total(), user.getSuaccount());
		// Step3.包裝回傳
		resp = packageService.setObjResp(resp, req, info, info_color);
		// 回傳-資料
		return packageService.objToJson(resp);
	}

	/**
	 * 查詢
	 */
	@ResponseBody
	@RequestMapping(value = { "/ajax/system_group.basil.AR" }, method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String sysGroupSearch(@RequestBody String json_object) {
		System.out.println("---controller - sysGroupSearch Check");
		// 取得-當前用戶資料
		SystemUser user = new SystemUser();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			LoginUserDetails userDetails = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// Step1.查詢資料
			user = userDetails.getSystemUser();
		}
		PackageBean req = new PackageBean();
		PackageBean resp = new PackageBean();
		String info = null, info_color = null;
		System.out.println(json_object);
		// Step1.包裝解析
		req = packageService.jsonToObj(new JSONObject(json_object));
		// Step2.進行查詢
		resp = groupService.getData(req.getBody(), req.getPage_batch(), req.getPage_total(), user.getSuaccount());
		// Step3.包裝回傳
		resp = packageService.setObjResp(resp, req, info, info_color);
		// 回傳-資料
		return packageService.objToJson(resp);
	}

	/**
	 * 新增
	 */
	@ResponseBody
	@RequestMapping(value = { "/ajax/system_group.basil.AC" }, method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String sysGroupCreate(@RequestBody String json_object) {
		System.out.println("---controller - sysGroupCreate Check");
		PackageBean req = new PackageBean();
		PackageBean resp = new PackageBean();
		boolean check = false;
		String info = null, info_color = null;
		System.out.println(json_object);
		// 取得-當前用戶資料
		SystemUser user = new SystemUser();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			LoginUserDetails userDetails = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// Step1.查詢資料
			user = userDetails.getSystemUser();
		}
		// Step1.包裝解析
		req = packageService.jsonToObj(new JSONObject(json_object));
		// Step2.進行新增
		check = groupService.createData(req.getBody(), user);
		check = groupService.save_asData(req.getBody(), user);
		// Step3.進行判定
		if (check) {
			// Step4.包裝回傳
			resp = packageService.setObjResp(resp, req, info, info_color);
		} else {
			// Step4.包裝回傳
			resp = packageService.setObjResp(resp, req, PackageBean.info_message_warning, PackageBean.info_color_warning);
		}
		// 回傳-資料
		return packageService.objToJson(resp);
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping(value = { "/ajax/system_group.basil.AU" }, method = { RequestMethod.PUT }, produces = "application/json;charset=UTF-8")
	public String sysGroupModify(@RequestBody String json_object) {
		System.out.println("---controller - sysGroupModify Check");
		PackageBean req = new PackageBean();
		PackageBean resp = new PackageBean();
		boolean check = false;
		String info = null, info_color = null;
		System.out.println(json_object);
		// 取得-當前用戶資料
		SystemUser user = new SystemUser();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			LoginUserDetails userDetails = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// Step1.查詢資料
			user = userDetails.getSystemUser();
		}
		// Step1.包裝解析
		req = packageService.jsonToObj(new JSONObject(json_object));
		// Step2.進行新增
		check = groupService.updateData(req.getBody(), user);
		// Step3.進行判定
		if (check) {
			// Step4.包裝回傳
			resp = packageService.setObjResp(resp, req, info, info_color);
		} else {
			// Step4.包裝回傳
			resp = packageService.setObjResp(resp, req, PackageBean.info_message_warning, PackageBean.info_color_warning);
		}
		// 回傳-資料
		return packageService.objToJson(resp);
	}

	/**
	 * 移除
	 */
	@ResponseBody
	@RequestMapping(value = { "/ajax/system_group.basil.AD" }, method = { RequestMethod.DELETE }, produces = "application/json;charset=UTF-8")
	public String sysGroupDelete(@RequestBody String json_object) {
		System.out.println("---controller - sysGroupDelete Check");
		PackageBean req = new PackageBean();
		PackageBean resp = new PackageBean();
		boolean check = false;
		String info = null, info_color = null;
		System.out.println(json_object);

		// Step1.包裝解析
		req = packageService.jsonToObj(new JSONObject(json_object));
		// Step2.進行新增
		check = groupService.deleteData(req.getBody());
		// Step3.進行判定
		if (check) {
			// Step4.包裝回傳
			resp = packageService.setObjResp(resp, req, info, info_color);
		} else {
			// Step4.包裝回傳
			resp = packageService.setObjResp(resp, req, PackageBean.info_message_warning, PackageBean.info_color_warning);
		}
		// 回傳-資料
		return packageService.objToJson(resp);
	}
}
