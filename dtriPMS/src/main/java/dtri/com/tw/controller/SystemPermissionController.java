package dtri.com.tw.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dtri.com.tw.bean.PackageBean;
import dtri.com.tw.service.PackageService;
import dtri.com.tw.service.SystemPermissionService;

@Controller
public class SystemPermissionController {
	// 功能
	final static String SYS_F = "sys_permission.basil";

	@Autowired
	PackageService packageService;
	@Autowired
	SystemPermissionService permissionService;

	/**
	 * 訪問
	 */
	@ResponseBody
	@RequestMapping(value = { "/ajax/system_permission.basil" }, method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String sysPermissionAccess(@RequestBody String json_object) {
		System.out.println("---controller - sysPermissionAccess Check");
		PackageBean req_object = new PackageBean();
		PackageBean resp_object = new PackageBean();
		String info = null, info_color = null;
		System.out.println(json_object);
		// Step1.包裝解析
		req_object = packageService.jsonToObj(new JSONObject(json_object));
		// Step2.進行查詢
		resp_object = permissionService.getData(req_object.getBody());
		// Step3.包裝回傳
		resp_object = packageService.setObjResp(resp_object, req_object, info, info_color);
		// 回傳-資料
		return packageService.objToJson(resp_object);
	}

	/**
	 * 查詢
	 */
	@ResponseBody
	@RequestMapping(value = { "/ajax/system_permission.basil.AR" }, method = {
			RequestMethod.GET }, produces = "application/json;charset=UTF-8")
	public String sysPermissionSearch(@RequestBody String json_object) {
		System.out.println("---controller - sysPermissionSearch Check");

		return "";
	}
}
