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
import dtri.com.tw.service.ProductionBodyService;


@Controller
public class ProductionBodyController {
	// 功能
	final static String SYS_F = "production_body.basil";

	@Autowired
	PackageService packageService;
	@Autowired
	ProductionBodyService pBodyService;

	/**
	 * 訪問
	 */
	@ResponseBody
	@RequestMapping(value = { "/ajax/production_body.basil" }, method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String sysPermissionAccess(@RequestBody String json_object) {
		System.out.println("---controller - sysPermissionAccess Check");
		PackageBean req = new PackageBean();
		PackageBean resp = new PackageBean();
		String info = null, info_color = null;
		System.out.println(json_object);
		// Step1.包裝解析
		req = packageService.jsonToObj(new JSONObject(json_object));
		// Step2.進行查詢
		resp = pBodyService.getData(req.getBody(), req.getPage_batch(), req.getPage_total());
		// Step3.包裝回傳
		resp = packageService.setObjResp(resp, req, info, info_color);
		// 回傳-資料
		return packageService.objToJson(resp);
	}

	/**
	 * 查詢
	 */
	@ResponseBody
	@RequestMapping(value = { "/ajax/production_body.basil.AR" }, method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String productionBodySearch(@RequestBody String json_object) {
		System.out.println("---controller - productionBodySearch Check");
		PackageBean req = new PackageBean();
		PackageBean resp = new PackageBean();
		String info = null, info_color = null;
		System.out.println(json_object);
		// Step1.包裝解析
		req = packageService.jsonToObj(new JSONObject(json_object));
		// Step2.進行查詢
		resp = pBodyService.getData(req.getBody(), req.getPage_batch(), req.getPage_total());
		// Step3.包裝回傳
		resp = packageService.setObjResp(resp, req, info, info_color);
		// 回傳-資料
		return packageService.objToJson(resp);
	}

	/**
	 * 新增
	 */
	@ResponseBody
	@RequestMapping(value = { "/ajax/production_body.basil.AC" }, method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String productionBodyCreate(@RequestBody String json_object) {
		System.out.println("---controller - productionBodyCreate Check");
		PackageBean req = new PackageBean();
		PackageBean resp = new PackageBean();
		boolean check = false;
		String info = null, info_color = null;
		System.out.println(json_object);
		// 取得-當前用戶資料
		SystemUser user = new SystemUser();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			LoginUserDetails userDetails = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			// Step1.查詢資料
			user = userDetails.getSystemUser();
		}
		// Step1.包裝解析
		req = packageService.jsonToObj(new JSONObject(json_object));
		// Step2.進行新增
		check = pBodyService.createData(req.getBody(), user);
		check = pBodyService.save_asData(req.getBody(), user);
		// Step3.進行判定
		if (check) {
			// Step4.包裝回傳
			resp = packageService.setObjResp(resp, req, info, info_color);
		} else {
			// Step4.包裝回傳
			resp = packageService.setObjResp(resp, req, PackageBean.info_message_warning,
					PackageBean.info_color_warning);
		}
		// 回傳-資料
		return packageService.objToJson(resp);
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping(value = { "/ajax/production_body.basil.AU" }, method = {
			RequestMethod.PUT }, produces = "application/json;charset=UTF-8")
	public String productionBodyModify(@RequestBody String json_object) {
		System.out.println("---controller - productionBodyModify Check");
		PackageBean req = new PackageBean();
		PackageBean resp = new PackageBean();
		boolean check = false;
		String info = null, info_color = null;
		System.out.println(json_object);
		// 取得-當前用戶資料
		SystemUser user = new SystemUser();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			LoginUserDetails userDetails = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			// Step1.查詢資料
			user = userDetails.getSystemUser();
		}
		// Step1.包裝解析
		req = packageService.jsonToObj(new JSONObject(json_object));
		// Step2.進行新增
		check = pBodyService.updateData(req.getBody(), user);
		// Step3.進行判定
		if (check) {
			// Step4.包裝回傳
			resp = packageService.setObjResp(resp, req, info, info_color);
		} else {
			// Step4.包裝回傳
			resp = packageService.setObjResp(resp, req, PackageBean.info_message_warning,
					PackageBean.info_color_warning);
		}
		// 回傳-資料
		return packageService.objToJson(resp);
	}

	/**
	 * 移除
	 */
	@ResponseBody
	@RequestMapping(value = { "/ajax/production_body.basil.AD" }, method = {
			RequestMethod.DELETE }, produces = "application/json;charset=UTF-8")
	public String productionBodyDelete(@RequestBody String json_object) {
		System.out.println("---controller - productionBodyDelete Check");
		PackageBean req = new PackageBean();
		PackageBean resp = new PackageBean();
		boolean check = false;
		String info = null, info_color = null;
		System.out.println(json_object);

		// Step1.包裝解析
		req = packageService.jsonToObj(new JSONObject(json_object));
		// Step2.進行新增
		check = pBodyService.deleteData(req.getBody());
		// Step3.進行判定
		if (check) {
			// Step4.包裝回傳
			resp = packageService.setObjResp(resp, req, info, info_color);
		} else {
			// Step4.包裝回傳
			resp = packageService.setObjResp(resp, req, PackageBean.info_message_warning,
					PackageBean.info_color_warning);
		}
		// 回傳-資料
		return packageService.objToJson(resp);
	}
}
