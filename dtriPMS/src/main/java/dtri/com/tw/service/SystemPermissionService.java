package dtri.com.tw.service;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dtri.com.tw.bean.PackageBean;
import dtri.com.tw.db.entity.SystemPermission;
import dtri.com.tw.db.pgsql.dao.SystemPermissionDao;

@Service
public class SystemPermissionService {
	@Autowired
	private SystemPermissionDao permissionDao;

	// 取得當前 資料清單
	public PackageBean getData(JSONArray body) {
		PackageBean bean = new PackageBean();
		ArrayList<SystemPermission> systemPermissions = new ArrayList<SystemPermission>();
		
		// 全查
		if (body == null ||body.length()<1) {
			systemPermissions = permissionDao.findAllByOrderBySpgidAscSyssortAsc();
		}
		// 放入包裝(header)
		JSONObject object_header = new JSONObject();
		object_header.put("sp_id", "功能ID");
		object_header.put("sp_g_id", "功能組ID");
		object_header.put("sp_g_name", "功能組名稱");
		object_header.put("sp_name", "功能名稱");
		object_header.put("sp_control", "功能控制");
		object_header.put("sp_permission", "權限範圍");
		object_header.put("sys_c_date", "建立時間");
		object_header.put("sys_c_user", "建立人");
		object_header.put("sys_m_date", "修改時間");
		object_header.put("sys_m_user", "修改人");
		object_header.put("sys_note", "備註");
		object_header.put("sys_sort", "排序");
		object_header.put("sys_ver", "版本");
		object_header.put("sys_status", "狀態");
		bean.setHeader(object_header);

		// 放入包裝(body)
		JSONArray object_bodys = new JSONArray();
		systemPermissions.forEach(one -> {
			JSONObject object_body = new JSONObject();
			object_body.put("sp_id", one.getSpid());
			object_body.put("sp_g_id", one.getSpgid());
			object_body.put("sp_g_name", one.getSpgname());
			object_body.put("sp_name", one.getSpname());
			object_body.put("sp_control", one.getSpcontrol());
			object_body.put("sp_permission", one.getSppermission());
			object_body.put("sys_c_date", one.getSyscdate());
			object_body.put("sys_c_user", one.getSyscuser());
			object_body.put("sys_m_date", one.getSysmdate());
			object_body.put("sys_m_user", one.getSysmuser());
			object_body.put("sys_note", one.getSysnote());
			object_body.put("sys_sort", one.getSyssort());
			object_body.put("sys_ver", one.getSysver());
			object_body.put("sys_status", one.getSysstatus());
			object_bodys.put(object_body);
		});
		bean.setBody(object_bodys);
		return bean;
	}
}
