package dtri.com.tw.service;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.tw.bean.PackageBean;
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.db.pgsql.dao.SystemGroupDao;
import dtri.com.tw.db.pgsql.dao.SystemUserDao;
import dtri.com.tw.tools.Fm_Time;

@Service
public class SystemUserService {
	@Autowired
	private SystemUserDao userDao;
	@Autowired
	private SystemGroupDao groupDao;

	@Autowired
	private FrontFormatService f_f;

	// 取得當前 資料清單
	public PackageBean getData(JSONObject body, int page, int p_size) {
		PackageBean bean = new PackageBean();
		ArrayList<SystemUser> systemUsers = new ArrayList<SystemUser>();

		// 查詢的頁數，page=從0起算/size=查詢的每頁筆數
		if (p_size < 1) {
			page = 0;
			p_size = 100;
		}
		String su_account = null;
		String su_name = null;
		String status = "0";
		PageRequest page_r = PageRequest.of(page, p_size, Sort.by("suid").descending());
		// 初次載入需要標頭 / 之後就不用
		if (body == null || body.isNull("search")) {

			// 放入包裝(header) [01 是排序][_h__ 是分割直][資料庫欄位名稱]
			JSONObject object_header = new JSONObject();
			String inp = "input", tex = "textarea", sel = "select";
			String text = "text", numb = "number", pass = "password";
			String dis = "disabled", sho = "show";

			object_header.put("01_h__su_id", f_f.h_title("ID", "100px", sho));
			object_header.put("02_h__su_sg_g_id", f_f.h_title("群組ID", "100px", sho));
			object_header.put("03_h__su_name", f_f.h_title("姓名", "100px", sho));
			object_header.put("04_h__su_e_name", f_f.h_title("英文姓名", "100px", sho));
			object_header.put("05_h__su_position", f_f.h_title("職位", "100px", sho));
			object_header.put("06_h__su_account", f_f.h_title("帳號", "100px", sho));
			object_header.put("07_h__su_email", f_f.h_title("Email", "100px", sho));

			object_header.put("08_h__sys_c_date", f_f.h_title("建立時間", "150px", sho));
			object_header.put("09_h__sys_c_user", f_f.h_title("建立人", "100px", sho));
			object_header.put("10_h__sys_m_date", f_f.h_title("修改時間", "150px", sho));
			object_header.put("11_h__sys_m_user", f_f.h_title("修改人", "100px", sho));

			object_header.put("12_h__sys_note", f_f.h_title("備註", "100px", dis));
			object_header.put("13_h__sys_sort", f_f.h_title("排序", "100px", dis));
			object_header.put("14_h__sys_ver", f_f.h_title("版本", "100px", dis));
			object_header.put("15_h__sys_status", f_f.h_title("狀態", "100px", sho));
			bean.setHeader(object_header);

			// 放入修改 [m__(key)](modify/Create/Delete) 格式
			JSONArray obj_m = new JSONArray();
			JSONArray values = new JSONArray();

			obj_m.put(f_f.h_modify(inp, text, "", dis, "col-md-1", false, new JSONArray(), "m__su_id", "ID"));

			JSONArray groups = new JSONArray();
			groupDao.findAllBySysheader(true, PageRequest.of(0, 999)).forEach(s -> {
				groups.put((new JSONObject()).put("value", s.getSgname()).put("key", s.getSggid()));
			});

			obj_m.put(f_f.h_modify(sel, text, "", sho, "col-md-2", true, groups, "m__su_sg_g_id", "群組ID"));
			obj_m.put(f_f.h_modify(inp, text, "", sho, "col-md-1", true, new JSONArray(), "m__su_position", "職位"));
			obj_m.put(f_f.h_modify(inp, text, "", sho, "col-md-2", true, new JSONArray(), "m__su_name", "姓名"));
			obj_m.put(f_f.h_modify(inp, text, "", sho, "col-md-2", true, new JSONArray(), "m__su_e_name", "英文姓名"));
			obj_m.put(f_f.h_modify(inp, text, "", sho, "col-md-2", true, new JSONArray(), "m__su_account", "帳號"));
			obj_m.put(f_f.h_modify(inp, pass, "", sho, "col-md-2", true, new JSONArray(), "m__su_password", "密碼"));

			obj_m.put(f_f.h_modify(inp, text, "", sho, "col-md-4", true, new JSONArray(), "m__su_email", "Email"));
			obj_m.put(f_f.h_modify(inp, text, "", dis, "col-md-2", false, new JSONArray(), "m__sys_c_date", "建立時間"));
			obj_m.put(f_f.h_modify(inp, text, "", dis, "col-md-2", false, new JSONArray(), "m__sys_c_user", "建立人"));
			obj_m.put(f_f.h_modify(inp, text, "", dis, "col-md-2", false, new JSONArray(), "m__sys_m_date", "修改時間"));
			obj_m.put(f_f.h_modify(inp, text, "", dis, "col-md-2", false, new JSONArray(), "m__sys_m_user", "修改人"));

			obj_m.put(f_f.h_modify(tex, text, "", sho, "col-md-12", false, new JSONArray(), "m__sys_note", "備註"));
			obj_m.put(f_f.h_modify(inp, numb, "", sho, "col-md-2", true, new JSONArray(), "m__sys_sort", "排序"));
			obj_m.put(f_f.h_modify(inp, numb, "", dis, "col-md-2", false, new JSONArray(), "m__sys_ver", "版本"));

			values = new JSONArray();
			values.put((new JSONObject()).put("value", "正常").put("key", "0"));
			values.put((new JSONObject()).put("value", "異常").put("key", "1"));
			obj_m.put(f_f.h_modify(sel, text, "", sho, "col-md-2", true, values, "m__sys_status", "狀態"));
			bean.setCell_modify(obj_m);

			// 放入包裝(search)
			JSONArray object_searchs = new JSONArray();
			object_searchs.put(f_f.h_search(inp, text, "col-md-2", "su_account", "帳號", new JSONArray()));
			object_searchs.put(f_f.h_search(inp, text, "col-md-2", "su_name", "姓名", new JSONArray()));

			values = new JSONArray();
			values.put((new JSONObject()).put("value", "正常").put("key", "0"));
			values.put((new JSONObject()).put("value", "異常").put("key", "1"));
			object_searchs.put(f_f.h_search(sel, text, "col-md-2", "sys_status", "狀態", values));
			bean.setCell_searchs(object_searchs);
		} else {

			// 進行-特定查詢
			su_account = body.getJSONObject("search").getString("su_account");
			su_account = su_account.equals("") ? null : su_account;
			su_name = body.getJSONObject("search").getString("su_name");
			su_name = su_name.equals("") ? null : su_name;
			status = body.getJSONObject("search").getString("sys_status");
			status = status.equals("") ? "0" : status;
		}
		// 全查
		systemUsers = userDao.findAllBySystemUser(su_name, su_account, Integer.parseInt(status), page_r);

		// 放入包裝(body) [01 是排序][_b__ 是分割直][資料庫欄位名稱]
		JSONArray object_bodys = new JSONArray();
		systemUsers.forEach(one -> {
			JSONObject object_body = new JSONObject();
			object_body.put("01_b__su_id", one.getSuid());
			object_body.put("02_b__su_sg_g_id", one.getSusggid());
			object_body.put("03_b__su_name", one.getSuname());
			object_body.put("04_b__su_e_name", one.getSuename());
			object_body.put("05_b__su_position", one.getSuposition());
			object_body.put("06_b__su_account", one.getSuaccount());
			object_body.put("07_b__su_email", one.getSuemail());

			object_body.put("08_b__sys_c_date", Fm_Time.to_yMd_Hms(one.getSyscdate()));
			object_body.put("09_b__sys_c_user", one.getSyscuser());
			object_body.put("10_b__sys_m_date", Fm_Time.to_yMd_Hms(one.getSysmdate()));
			object_body.put("11_b__sys_m_user", one.getSysmuser());
			object_body.put("12_b__sys_note", one.getSysnote());
			object_body.put("13_b__sys_sort", one.getSyssort());
			object_body.put("14_b__sys_ver", one.getSysver());
			object_body.put("15_b__sys_status", one.getSysstatus());
			object_bodys.put(object_body);
		});
		bean.setBody(new JSONObject().put("search", object_bodys));
		return bean;
	}

	// 存檔 資料清單
	@Transactional
	public boolean createData(JSONObject body, SystemUser user) {
		boolean check = false;
		PasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		try {
			JSONArray list = body.getJSONArray("create");
			for (Object one : list) {
				// 物件轉換
				SystemUser sys_c = new SystemUser();
				JSONObject data = (JSONObject) one;
				sys_c.setSusggid(data.getInt("su_sg_g_id"));
				sys_c.setSuname(data.getString("su_name"));
				sys_c.setSuename(data.getString("su_e_name"));
				sys_c.setSuposition(data.getString("su_position"));
				sys_c.setSuaccount(data.getString("su_account"));
				sys_c.setSupassword(pwdEncoder.encode((data.getString("su_password"))));
				sys_c.setSuemail(data.getString("su_email"));

				sys_c.setSysnote(data.getString("sys_note"));
				sys_c.setSyssort(data.getInt("sys_sort"));
				sys_c.setSysstatus(data.getInt("sys_status"));
				sys_c.setSysmuser(user.getSuname());
				sys_c.setSyscuser(user.getSuname());

				// 帳號重複
				if (userDao.findBySuaccount(sys_c.getSuaccount()) != null) {
					check = false;
				} else {
					userDao.save(sys_c);
					check = true;
				}
			}

			list = body.getJSONArray("save_as");
			for (Object one : list) {
				// 物件轉換
				SystemUser sys_c = new SystemUser();
				JSONObject data = (JSONObject) one;
				sys_c.setSusggid(data.getInt("su_sg_g_id"));
				sys_c.setSuname(data.getString("su_name"));
				sys_c.setSuename(data.getString("su_e_name"));
				sys_c.setSuposition(data.getString("su_position"));
				sys_c.setSuaccount(data.getString("su_account"));
				sys_c.setSupassword(pwdEncoder.encode(data.getString("su_password")));
				sys_c.setSuemail(data.getString("su_email"));

				sys_c.setSysnote(data.getString("sys_note"));
				sys_c.setSyssort(data.getInt("sys_sort"));
				sys_c.setSysstatus(data.getInt("sys_status"));
				sys_c.setSysmuser(user.getSuname());
				sys_c.setSyscuser(user.getSuname());

				// 帳號重複
				if (userDao.findBySuaccount(sys_c.getSuaccount()) != null) {
					check = false;
				} else {
					userDao.save(sys_c);
					check = true;
				}
			}

		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return check;
	}

	// 更新 資料清單
	@Transactional
	public boolean updateData(JSONObject body, SystemUser user) {
		boolean check = false;
		try {
			PasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
			JSONArray list = body.getJSONArray("modify");
			for (Object one : list) {
				// 物件轉換
				SystemUser sys_c = new SystemUser();
				JSONObject data = (JSONObject) one;
				sys_c.setSuid(data.getInt("su_id"));
				sys_c.setSusggid(data.getInt("su_sg_g_id"));
				sys_c.setSuname(data.getString("su_name"));
				sys_c.setSuename(data.getString("su_e_name"));
				sys_c.setSuposition(data.getString("su_position"));
				sys_c.setSuaccount(data.getString("su_account"));
				sys_c.setSupassword(pwdEncoder.encode(data.getString("su_password")));
				sys_c.setSuemail(data.getString("su_email"));

				sys_c.setSysnote(data.getString("sys_note"));
				sys_c.setSyssort(data.getInt("sys_sort"));
				sys_c.setSysstatus(data.getInt("sys_status"));
				sys_c.setSysmuser(user.getSuname());
				sys_c.setSyscuser(user.getSuname());
				sys_c.setSysmdate(new Date());

				userDao.save(sys_c);
				check = true;

			}
		} catch (Exception e) {
			System.out.println(e);
			return false;
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
				SystemUser sys_p = new SystemUser();
				JSONObject data = (JSONObject) one;
				sys_p.setSuid(data.getInt("su_id"));

				if (userDao.deleteBySuid(sys_p.getSuid()) > 1) {
					check = true;
				}

			}
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return check;
	}
}
