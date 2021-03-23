package dtri.com.tw.service;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dtri.com.tw.bean.PackageBean;
import dtri.com.tw.db.entity.SystemPermission;
import dtri.com.tw.db.pgsql.dao.SystemPermissionDao;
import dtri.com.tw.tools.Fm_Time;

@Service
public class SystemPermissionService {
	@Autowired
	private SystemPermissionDao permissionDao;

	// 取得當前 資料清單
	public PackageBean getData(JSONArray body) {
		PackageBean bean = new PackageBean();
		ArrayList<SystemPermission> systemPermissions = new ArrayList<SystemPermission>();

		// 全查
		if (body == null || body.length() < 1) {
			systemPermissions = permissionDao.findAllByOrderBySpgidAscSyssortAsc();
		}
		// 放入包裝(header)
		JSONObject object_header = new JSONObject();
		object_header.put("01_h_sp_id", "功能ID");
		object_header.put("02_h_sp_g_id", "功能組ID");
		object_header.put("03_h_sp_g_name", "功能組名稱");
		object_header.put("04_h_sp_name", "功能名稱");
		object_header.put("05_h_sp_control", "功能控制");
		object_header.put("06_h_sp_permission", "權限範圍");
		object_header.put("07_h_sys_c_date", "建立時間");
		object_header.put("08_h_sys_c_user", "建立人");
		object_header.put("09_h_sys_m_date", "修改時間");
		object_header.put("10_h_sys_m_user", "修改人");
		object_header.put("11_h_sys_note", "備註");
		object_header.put("12_h_sys_sort", "排序");
		object_header.put("13_h_sys_ver", "版本");
		object_header.put("14_h_sys_status", "狀態");
		bean.setHeader(object_header);

		// 放入包裝(body)
		JSONArray object_bodys = new JSONArray();
		systemPermissions.forEach(one -> {
			JSONObject object_body = new JSONObject();
			object_body.put("01_b_sp_id", one.getSpid());
			object_body.put("02_b_sp_g_id", one.getSpgid());
			object_body.put("03_b_sp_g_name", one.getSpgname());
			object_body.put("04_b_sp_name", one.getSpname());
			object_body.put("05_b_sp_control", one.getSpcontrol());
			object_body.put("06_b_sp_permission", one.getSppermission());
			object_body.put("07_b_sys_c_date", Fm_Time.to_yMd_Hms(one.getSyscdate()));
			object_body.put("08_b_sys_c_user", one.getSyscuser());
			object_body.put("09_b_sys_m_date", Fm_Time.to_yMd_Hms(one.getSysmdate()));
			object_body.put("10_b_sys_m_user", one.getSysmuser());
			object_body.put("11_b_sys_note", one.getSysnote());
			object_body.put("12_b_sys_sort", one.getSyssort());
			object_body.put("13_b_sys_ver", one.getSysver());
			object_body.put("14_b_sys_status", one.getSysstatus());
			object_bodys.put(object_body);
		});
		bean.setBody(object_bodys);
		return bean;
	}
}
