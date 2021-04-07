package dtri.com.tw.service;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.tw.bean.PackageBean;
import dtri.com.tw.db.entity.SystemPermission;
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.db.pgsql.dao.SystemPermissionDao;
import dtri.com.tw.tools.Fm_Time;

@Service
public class SystemPermissionService {
	@Autowired
	private SystemPermissionDao permissionDao;
	@Autowired
	private FrontFormatService f_f;

	// 取得當前 資料清單
	public PackageBean getData(JSONObject body, int page, int p_size) {
		PackageBean bean = new PackageBean();
		ArrayList<SystemPermission> systemPermissions = new ArrayList<SystemPermission>();

		// 查詢的頁數，page=從0起算/size=查詢的每頁筆數
		if (p_size < 1) {
			page = 0;
			p_size = 100;
		}
		PageRequest page_r = PageRequest.of(page, p_size, Sort.by("spid").descending());
		// 初次載入需要標頭 / 之後就不用
		if (body == null || body.isNull("search")) {
			// 全查
			systemPermissions = permissionDao.findAllByOrderBySpgidAscSpidAsc(page_r);
			// 放入包裝(header) [01 是排序][_h__ 是分割直][資料庫欄位名稱]
			JSONObject object_header = new JSONObject();
			object_header.put("01_h__sp_id", f_f.h_title("功能ID", "100px"));
			object_header.put("02_h__sp_g_id", f_f.h_title("功能組ID", "100px"));
			object_header.put("03_h__sp_g_name", f_f.h_title("功能組名稱", "150px"));
			object_header.put("04_h__sp_name", f_f.h_title("功能名稱", "150px"));
			object_header.put("05_h__sp_control", f_f.h_title("功能控制", "150px"));
			object_header.put("06_h__sp_permission", f_f.h_title("權限範圍", "150px"));
			
			object_header.put("07_h__sys_c_date", f_f.h_title("建立時間", "150px"));
			object_header.put("08_h__sys_c_user", f_f.h_title("建立人", "100px"));
			object_header.put("09_h__sys_m_date", f_f.h_title("修改時間", "150px"));
			object_header.put("10_h__sys_m_user", f_f.h_title("修改人", "100px"));

			object_header.put("11_h__sys_note", f_f.h_title("備註", "100px"));
			object_header.put("12_h__sys_sort", f_f.h_title("排序", "100px"));
			object_header.put("13_h__sys_ver", f_f.h_title("版本", "100px"));
			object_header.put("14_h__sys_status", f_f.h_title("狀態", "100px"));
			bean.setHeader(object_header);

			// 放入修改 [m__(key)](modify/Create/Delete) 格式
			JSONArray obj_m = new JSONArray();
			JSONArray values = new JSONArray();
			String inp = "input", tex = "textarea", sel = "select";
			String text = "text", numb = "number";
			String dis = "disabled", sho = "show";

			obj_m.put(f_f.h_modify(inp, text, "", dis, "col-md-2", false, values, "m__sp_id", "功能ID"));
			obj_m.put(f_f.h_modify(inp, text, "", dis, "col-md-2", false, values, "m__sp_g_id", "功能組ID"));
			obj_m.put(f_f.h_modify(inp, text, "", sho, "col-md-2", true, values, "m__sp_g_name", "功能組名稱"));
			obj_m.put(f_f.h_modify(inp, text, "", sho, "col-md-2", true, values, "m__sp_name", "功能名稱"));
			obj_m.put(f_f.h_modify(inp, text, "", sho, "col-md-2", true, values, "m__sp_control", "功能控制"));

			obj_m.put(f_f.h_modify(inp, text, "", sho, "col-md-2", true, values, "m__sp_permission", "權限範圍"));
			obj_m.put(f_f.h_modify(inp, text, "", dis, "col-md-2", false, values, "m__sys_c_date", "建立時間"));
			obj_m.put(f_f.h_modify(inp, text, "", dis, "col-md-2", false, values, "m__sys_c_user", "建立人"));
			obj_m.put(f_f.h_modify(inp, text, "", dis, "col-md-2", false, values, "m__sys_m_date", "修改時間"));
			obj_m.put(f_f.h_modify(inp, text, "", dis, "col-md-2", false, values, "m__sys_m_user", "修改人"));

			obj_m.put(f_f.h_modify(tex, text, "", sho, "col-md-12", false, values, "m__sys_note", "備註"));
			obj_m.put(f_f.h_modify(inp, numb, "", sho, "col-md-2", true, values, "m__sys_sort", "排序"));
			obj_m.put(f_f.h_modify(inp, numb, "", dis, "col-md-2", false, values, "m__sys_ver", "版本"));

			values = new JSONArray();
			values.put((new JSONObject()).put("value", "正常").put("key", "0"));
			values.put((new JSONObject()).put("value", "異常").put("key", "1"));
			obj_m.put(f_f.h_modify(sel, text, "", sho, "col-md-2", true, values, "m__sys_status", "狀態"));
			bean.setCell_modify(obj_m);

			// 放入包裝(search)
			JSONArray object_searchs = new JSONArray();
			object_searchs.put(f_f.h_search("input", "text", "col-md-2", "sp_g_name", "功能組名稱", new JSONArray()));
			object_searchs.put(f_f.h_search("input", "text", "col-md-2", "sp_name", "功能名稱", new JSONArray()));
			values = new JSONArray();
			values.put((new JSONObject()).put("value", "正常").put("key", "0"));
			values.put((new JSONObject()).put("value", "異常").put("key", "1"));
			object_searchs.put(f_f.h_search("select", "text", "col-md-2", "sys_status", "狀態", values));
			bean.setCell_searchs(object_searchs);
		} else {
			// 進行-特定查詢
			String sp_name = body.getJSONObject("search").getString("sp_name");
			sp_name = sp_name.equals("") ? null : sp_name;
			String sp_g_name = body.getJSONObject("search").getString("sp_g_name");
			sp_g_name = sp_g_name.equals("") ? null : sp_g_name;
			String status = body.getJSONObject("search").getString("sys_status");
			status = status.equals("") ? "0" : status;

			systemPermissions = permissionDao.findAllByPermission(sp_name, sp_g_name, Integer.parseInt(status), page_r);
		}

		// 放入包裝(body) [01 是排序][_b__ 是分割直][資料庫欄位名稱]
		JSONArray object_bodys = new JSONArray();
		systemPermissions.forEach(one -> {
			JSONObject object_body = new JSONObject();
			object_body.put("01_b__sp_id", one.getSpid());
			object_body.put("02_b__sp_g_id", one.getSpgid());
			object_body.put("03_b__sp_g_name", one.getSpgname());
			object_body.put("04_b__sp_name", one.getSpname());
			object_body.put("05_b__sp_control", one.getSpcontrol());
			object_body.put("06_b__sp_permission", one.getSppermission());
			object_body.put("07_b__sys_c_date", Fm_Time.to_yMd_Hms(one.getSyscdate()));
			object_body.put("08_b__sys_c_user", one.getSyscuser());
			object_body.put("09_b__sys_m_date", Fm_Time.to_yMd_Hms(one.getSysmdate()));
			object_body.put("10_b__sys_m_user", one.getSysmuser());
			object_body.put("11_b__sys_note", one.getSysnote());
			object_body.put("12_b__sys_sort", one.getSyssort());
			object_body.put("13_b__sys_ver", one.getSysver());
			object_body.put("14_b__sys_status", one.getSysstatus());
			object_bodys.put(object_body);
		});
		bean.setBody(new JSONObject().put("search", object_bodys));
		return bean;
	}

	// 存檔 資料清單
	@Transactional
	public boolean createData(JSONObject body, SystemUser user) {
		boolean check = false;
		try {
			JSONArray list = body.getJSONArray("create");
			for (Object one : list) {
				// 物件轉換
				SystemPermission sys_p = new SystemPermission();
				JSONObject data = (JSONObject) one;
				sys_p.setSpname(data.getString("sp_name"));
				sys_p.setSpgname(data.getString("sp_g_name"));
				sys_p.setSpcontrol(data.getString("sp_control"));
				sys_p.setSppermission(data.getString("sp_permission"));
				sys_p.setSysnote(data.getString("sys_note"));
				sys_p.setSyssort(data.getInt("sys_sort"));
				sys_p.setSysstatus(data.getInt("sys_status"));
				sys_p.setSysmuser(user.getSuname());
				sys_p.setSyscuser(user.getSuname());

				// 檢查群組名稱重複
				ArrayList<SystemPermission> sys_p_g = permissionDao.findAllByPermissionGroupTop1(sys_p.getSpgname(), PageRequest.of(0, 1));
				if (sys_p_g != null && sys_p_g.size() > 0) {
					// 重複 則取同樣G_ID
					sys_p.setSpgid(sys_p_g.get(0).getSpgid());
				} else {
					// 取得最新G_ID
					sys_p_g = permissionDao.findAllByTop1(PageRequest.of(0, 1));
					sys_p.setSpgid((sys_p_g.get(0).getSpgid() + 1));
				}
				permissionDao.save(sys_p);
				check = true;
			}

			list = body.getJSONArray("save_as");
			for (Object one : list) {
				// 物件轉換
				SystemPermission sys_p = new SystemPermission();
				JSONObject data = (JSONObject) one;
				sys_p.setSpname(data.getString("sp_name"));
				sys_p.setSpgname(data.getString("sp_g_name"));
				sys_p.setSpcontrol(data.getString("sp_control"));
				sys_p.setSppermission(data.getString("sp_permission"));
				sys_p.setSysnote(data.getString("sys_note"));
				sys_p.setSyssort(data.getInt("sys_sort"));
				sys_p.setSysstatus(data.getInt("sys_status"));
				sys_p.setSysmuser(user.getSuname());
				sys_p.setSyscuser(user.getSuname());

				// 檢查群組名稱重複
				ArrayList<SystemPermission> sys_p_g = permissionDao.findAllByPermissionGroupTop1(sys_p.getSpgname(), PageRequest.of(0, 1));
				if (sys_p_g != null && sys_p_g.size() > 0) {
					// 重複 則取同樣G_ID
					sys_p.setSpgid(sys_p_g.get(0).getSpgid());
					sys_p.setSysheader(false);
				} else {
					// 取得最新G_ID
					sys_p_g = permissionDao.findAllByTop1(PageRequest.of(0, 1));
					sys_p.setSpgid((sys_p_g.get(0).getSpgid() + 1));
					sys_p.setSysheader(true);
				}
				permissionDao.save(sys_p);
				check = true;
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return check;
	}

	// 更新 資料清單
	@Transactional
	public boolean updateData(JSONObject body, SystemUser user) {
		boolean check = false;
		try {
			JSONArray list = body.getJSONArray("modify");
			for (Object one : list) {
				// 物件轉換
				SystemPermission sys_p = new SystemPermission();
				JSONObject data = (JSONObject) one;
				sys_p.setSpid(data.getInt("sp_id"));
				sys_p.setSpname(data.getString("sp_name"));
				sys_p.setSpgid(data.getInt("sp_g_id"));
				sys_p.setSpgname(data.getString("sp_g_name"));
				sys_p.setSpcontrol(data.getString("sp_control"));
				sys_p.setSppermission(data.getString("sp_permission"));
				sys_p.setSysnote(data.getString("sys_note"));
				sys_p.setSyssort(data.getInt("sys_sort"));
				sys_p.setSysstatus(data.getInt("sys_status"));
				sys_p.setSysmdate(new Date());

				// 檢查群組名稱重複
				ArrayList<SystemPermission> sys_p_g = permissionDao.findAllByPermissionGroupTop1(sys_p.getSpgname(), PageRequest.of(0, 1));
				if (sys_p_g != null && sys_p_g.size() > 0) {
					// 重複 則取同樣G_ID
					sys_p.setSpgid(sys_p_g.get(0).getSpgid());
					sys_p.setSysheader(false);
				} else {
					// 取得最新G_ID
					sys_p_g = permissionDao.findAllByTop1(PageRequest.of(0, 1));
					sys_p.setSpgid((sys_p_g.get(0).getSpgid() + 1));
					sys_p.setSysheader(true);
				}
				permissionDao.save(sys_p);
				check = true;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return check;
	}

	// 移除 資料清單
	@Transactional
	public boolean deleteData(JSONObject body) {

		boolean check = false;
		try {
			JSONArray list = body.getJSONArray("delete");
			for (Object one : list) {
				// 物件轉換
				SystemPermission sys_p = new SystemPermission();
				JSONObject data = (JSONObject) one;
				sys_p.setSpid(data.getInt("sp_id"));

				permissionDao.deleteBySpidAndSysheader(sys_p.getSpid(),false);
				check = true;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return check;
	}
}
