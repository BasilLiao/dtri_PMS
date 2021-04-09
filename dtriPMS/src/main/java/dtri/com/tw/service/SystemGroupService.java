package dtri.com.tw.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.tw.bean.PackageBean;
import dtri.com.tw.db.entity.SystemGroup;
import dtri.com.tw.db.entity.SystemPermission;
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.db.pgsql.dao.SystemGroupDao;
import dtri.com.tw.db.pgsql.dao.SystemPermissionDao;
import dtri.com.tw.tools.Fm_Time;

@Service
public class SystemGroupService {
	@Autowired
	private SystemGroupDao groupDao;
	@Autowired
	private SystemPermissionDao permissionDao;
	@Autowired
	private FrontFormatService f_f;

	// 取得當前 資料清單
	public PackageBean getData(JSONObject body, int page, int p_size) {
		PackageBean bean = new PackageBean();
		List<SystemGroup> systemGroup = new ArrayList<SystemGroup>();
		ArrayList<SystemPermission> permissions = new ArrayList<SystemPermission>();
		// 查詢的頁數，page=從0起算/size=查詢的每頁筆數
		if (p_size < 1) {
			page = 0;
			p_size = 100;
		}
		PageRequest page_r = PageRequest.of(page, p_size, Sort.by("sggid").descending());
		String sg_name = null;
		String status = "0";
		// 初次載入需要標頭 / 之後就不用
		if (body == null || body.isNull("search")) {

			page_r = PageRequest.of(page, 999, Sort.by("spgid").descending());
			permissions = permissionDao.findAllByPermission(null, null, 0, page_r);
			// 放入包裝(header) [01 是排序][_h__ 是分割直][資料庫欄位名稱]
			JSONObject object_header = new JSONObject();
			String inp = "input", tex = "textarea", sel = "select";
			String text = "text", numb = "number";
			String dis = "disabled", sho = "show";

			object_header.put("01_h__sg_id", f_f.h_title("ID", "50px", sho));
			object_header.put("02_h__sg_g_id", f_f.h_title("群組ID", "100px", sho));
			object_header.put("03_h__sg_name", f_f.h_title("群組名稱", "150px", sho));
			object_header.put("04_h__sg_sp_name", f_f.h_title("單元名稱", "150px", sho));
			object_header.put("05_h__sys_header", f_f.h_title("群組代表", "100px", sho));
			object_header.put("06_h__sg_sp_id", f_f.h_title("關聯ID", "100px", sho));

			// (sg_permission[特殊3(512),特殊2(256),特殊1(128),訪問(64),下載(32),上傳(16),新增(8),修改(4),刪除(2),查詢(1)])
			object_header.put("07_h__sg_permission_512", f_f.h_title("S3", "50px", sho));
			object_header.put("08_h__sg_permission_256", f_f.h_title("S2", "50px", sho));
			object_header.put("09_h__sg_permission_128", f_f.h_title("S1", "50px", sho));
			object_header.put("10_h__sg_permission_64", f_f.h_title("訪", "50px", sho));
			object_header.put("11_h__sg_permission_32", f_f.h_title("下", "50px", sho));
			object_header.put("12_h__sg_permission_16", f_f.h_title("上", "50px", sho));
			object_header.put("13_h__sg_permission_8", f_f.h_title("增", "50px", sho));
			object_header.put("14_h__sg_permission_4", f_f.h_title("改", "50px", sho));
			object_header.put("15_h__sg_permission_2", f_f.h_title("刪", "50px", sho));
			object_header.put("16_h__sg_permission_1", f_f.h_title("查", "50px", sho));

			object_header.put("17_h__sys_c_date", f_f.h_title("建立時間", "150px", sho));
			object_header.put("18_h__sys_c_user", f_f.h_title("建立人", "100px", sho));
			object_header.put("19_h__sys_m_date", f_f.h_title("修改時間", "150px", sho));
			object_header.put("20_h__sys_m_user", f_f.h_title("修改人", "100px", sho));

			object_header.put("21_h__sys_note", f_f.h_title("備註", "100px", sho));
			object_header.put("22_h__sys_sort", f_f.h_title("排序", "100px", sho));
			object_header.put("23_h__sys_ver", f_f.h_title("版本", "100px", sho));
			object_header.put("24_h__sys_status", f_f.h_title("狀態", "100px", sho));

			bean.setHeader(object_header);

			// 放入修改 [m__(key)](modify/Create/Delete) 格式 
			JSONArray obj_m = new JSONArray();
			JSONArray values = new JSONArray();

			obj_m.put(f_f.h_modify(inp, text, "", dis, "col-md-2", false, values, "m__sg_id", "群組ID"));
			obj_m.put(f_f.h_modify(inp, text, "", dis, "col-md-2", false, values, "m__sg_g_id", "群組類別ID"));
			obj_m.put(f_f.h_modify(inp, text, "", sho, "col-md-2", true, values, "m__sg_name", "群組名稱"));
			obj_m.put(f_f.h_modify(inp, text, "", dis, "col-md-2", true, values, "m__sg_sp_name", "單元名稱"));

			obj_m.put(f_f.h_modify(inp, numb, "", sho, "col-md-1", true, values, "m__sys_sort", "排序"));
			obj_m.put(f_f.h_modify(inp, numb, "", dis, "col-md-1", false, values, "m__sys_ver", "版本"));

			values = new JSONArray();
			values.put((new JSONObject()).put("value", "正常").put("key", "0"));
			values.put((new JSONObject()).put("value", "異常").put("key", "1"));
			obj_m.put(f_f.h_modify(sel, text, "", sho, "col-md-1", true, values, "m__sys_status", "狀態"));
			bean.setCell_modify(obj_m);

			JSONArray values_select = new JSONArray();
			permissions.forEach(s -> {
				values_select.put((new JSONObject()).put("value", s.getSpname()).put("key", s.getSpid()));
			});
			obj_m.put(f_f.h_modify(sel, text, "", sho, "col-md-2", true, values_select, "m__sg_sp_id", "權限關聯ID"));

			values = new JSONArray();
			values.put((new JSONObject()).put("value", "＞Ｘ＜").put("key", "0"));
			values.put((new JSONObject()).put("value", "＜Ｏ＞").put("key", "1"));

			obj_m.put(f_f.h_modify(sel, text, "", sho, "col-md-1", true, values, "m__sg_permission_512", "S3"));
			obj_m.put(f_f.h_modify(sel, text, "", sho, "col-md-1", true, values, "m__sg_permission_256", "S2"));
			obj_m.put(f_f.h_modify(sel, text, "", sho, "col-md-1", true, values, "m__sg_permission_128", "S1"));
			obj_m.put(f_f.h_modify(sel, text, "", sho, "col-md-1", true, values, "m__sg_permission_64", "訪問"));
			obj_m.put(f_f.h_modify(sel, text, "", sho, "col-md-1", true, values, "m__sg_permission_32", "下載"));
			obj_m.put(f_f.h_modify(sel, text, "", sho, "col-md-1", true, values, "m__sg_permission_16", "上傳"));
			obj_m.put(f_f.h_modify(sel, text, "", sho, "col-md-1", true, values, "m__sg_permission_8", "新增"));
			obj_m.put(f_f.h_modify(sel, text, "", sho, "col-md-1", true, values, "m__sg_permission_4", "修改"));
			obj_m.put(f_f.h_modify(sel, text, "", sho, "col-md-1", true, values, "m__sg_permission_2", "刪除"));
			obj_m.put(f_f.h_modify(sel, text, "", sho, "col-md-1", true, values, "m__sg_permission_1", "查詢"));

			// obj_m.put(f_f.h_modify(inp, text, "", dis, "col-md-2", false, values,
			// "m__sys_c_date", "建立時間"));
			// obj_m.put(f_f.h_modify(inp, text, "", dis, "col-md-2", false, values,
			// "m__sys_c_user", "建立人"));
			// obj_m.put(f_f.h_modify(inp, text, "", dis, "col-md-2", false, values,
			// "m__sys_m_date", "修改時間"));
			// obj_m.put(f_f.h_modify(inp, text, "", dis, "col-md-2", false, values,
			// "m__sys_m_user", "修改人"));

			// obj_m.put(f_f.h_modify(tex, text, "", sho, "col-md-12", false, values,
			// "m__sys_note", "備註"));

			// 放入包裝(search)
			JSONArray object_searchs = new JSONArray();
			values = new JSONArray();
			object_searchs.put(f_f.h_search(inp, text, "col-md-2", "sg_name", "群組名稱", values));

			values = new JSONArray();
			values.put((new JSONObject()).put("value", "正常").put("key", "0"));
			values.put((new JSONObject()).put("value", "異常").put("key", "1"));
			object_searchs.put(f_f.h_search("select", text, "col-md-2", "sys_status", "狀態", values));

			bean.setCell_searchs(object_searchs);
		} else {
			// 進行-特定查詢
			sg_name = body.getJSONObject("search").getString("sg_name");
			sg_name = sg_name.equals("") ? null : sg_name;
			status = body.getJSONObject("search").getString("sys_status");
			status = status.equals("") ? "0" : status;
		}
		
		// 全查
		page_r = PageRequest.of(page, 999, Sort.by("sggid").descending());
		List<Integer> sggid = new ArrayList<Integer>();
		groupDao.findAllBySysheader(true, page_r).forEach(s -> {
			sggid.add(s.getSggid());
		});
		systemGroup = groupDao.findAllBySystemGroup(null, 0, sggid);

		// 放入包裝(body) [01 是排序][_b__ 是分割直][資料庫欄位名稱]
		JSONArray object_bodys = new JSONArray();
		systemGroup.forEach(one -> {
			JSONObject object_body = new JSONObject();
			object_body.put("01_b__sg_id", one.getSgid());
			object_body.put("02_b__sg_g_id", one.getSggid());
			object_body.put("03_b__sg_name", one.getSgname());
			object_body.put("04_b__sg_sp_name", one.getSystemPermission().getSpname());
			object_body.put("05_b__sys_hearder", one.getSysheader());
			object_body.put("06_b__sg_sp_id", one.getSystemPermission().getSpid());

			object_body.put("07_b__sg_permission_512", Character.toString(one.getSgpermission().charAt(0)));
			object_body.put("08_b__sg_permission_256", Character.toString(one.getSgpermission().charAt(1)));
			object_body.put("09_b__sg_permission_128", Character.toString(one.getSgpermission().charAt(2)));
			object_body.put("10_b__sg_permission_64", Character.toString(one.getSgpermission().charAt(3)));
			object_body.put("11_b__sg_permission_32", Character.toString(one.getSgpermission().charAt(4)));
			object_body.put("12_b__sg_permission_16", Character.toString(one.getSgpermission().charAt(5)));
			object_body.put("13_b__sg_permission_8", Character.toString(one.getSgpermission().charAt(6)));
			object_body.put("14_b__sg_permission_4", Character.toString(one.getSgpermission().charAt(7)));
			object_body.put("15_b__sg_permission_2", Character.toString(one.getSgpermission().charAt(8)));
			object_body.put("16_b__sg_permission_1", Character.toString(one.getSgpermission().charAt(9)));

			object_body.put("17_b__sys_c_date", Fm_Time.to_yMd_Hms(one.getSyscdate()));
			object_body.put("18_b__sys_c_user", one.getSyscuser());
			object_body.put("19_b__sys_m_date", Fm_Time.to_yMd_Hms(one.getSysmdate()));
			object_body.put("20_b__sys_m_user", one.getSysmuser());
			object_body.put("21_b__sys_note", one.getSysnote());
			object_body.put("22_b__sys_sort", one.getSyssort());
			object_body.put("23_b__sys_ver", one.getSysver());
			object_body.put("24_b__sys_status", one.getSysstatus());
			object_bodys.put(object_body);
		});
		bean.setBody(new JSONObject().put("search", object_bodys));
		bean.setBody_type("fatherSon");
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
				SystemGroup sys_p = new SystemGroup();
				JSONObject data = (JSONObject) one;
				sys_p.setSgname(data.getString("sg_name"));
				sys_p.setSgname(data.getString("sg_name"));
				SystemPermission p = new SystemPermission();
				p.setSpid(data.getInt("sg_sp_id"));
				sys_p.setSystemPermission(p);
				sys_p.setSgpermission(data.getString("sg_permission_512") + data.getString("sg_permission_256") + data.getString("sg_permission_128")
						+ data.getString("sg_permission_64") + data.getString("sg_permission_32") + data.getString("sg_permission_16")
						+ data.getString("sg_permission_8") + data.getString("sg_permission_4") + data.getString("sg_permission_2")
						+ data.getString("sg_permission_1"));
				// sys_p.setSysnote(data.getString("sys_note"));
				sys_p.setSyssort(data.getInt("sys_sort"));
				sys_p.setSysstatus(data.getInt("sys_status"));
				sys_p.setSysmuser(user.getSuname());
				sys_p.setSyscuser(user.getSuname());

				// 檢查群組名稱重複
				ArrayList<SystemGroup> sys_p_g = groupDao.findAllByGroupTop1(sys_p.getSgname(), PageRequest.of(0, 1));
				if (sys_p_g != null && sys_p_g.size() > 0) {
					// 重複 則取同樣G_ID
					//sys_p.setSggid(sys_p_g.get(0).getSggid());
					//groupDao.save(sys_p);
					return false;
				} else {

					// 新建 群組頭
					sys_p_g = groupDao.findAllByTop1(PageRequest.of(0, 1));
					SystemGroup sys_p_h = new SystemGroup();
					sys_p_h.setSggid((sys_p_g.get(0).getSggid() + 1));
					sys_p_h.setSgname(sys_p.getSgname());
					sys_p_h.setSgpermission("0000000000");
					sys_p_h.setSysheader(true);
					sys_p_h.setSystemPermission(new SystemPermission(0));

					sys_p_h.setSysnote("");
					sys_p_h.setSyssort(0);
					sys_p_h.setSysstatus(0);
					sys_p_h.setSysmuser(user.getSuname());
					sys_p_h.setSyscuser(user.getSuname());
					groupDao.save(sys_p_h);
					// 新建 取得最新G_ID
					sys_p.setSggid((sys_p_g.get(0).getSggid() + 1));
					groupDao.save(sys_p);
					check = true;
				}

			}

			list = body.getJSONArray("save_as");
			for (Object one : list) {
				// 物件轉換
				SystemGroup sys_p = new SystemGroup();
				JSONObject data = (JSONObject) one;
				sys_p.setSgname(data.getString("sg_name"));
				sys_p.setSgname(data.getString("sg_name"));
				SystemPermission p = new SystemPermission();
				p.setSpid(data.getInt("sg_sp_id"));
				sys_p.setSystemPermission(p);
				sys_p.setSgpermission(data.getString("sg_permission_512") + data.getString("sg_permission_256") + data.getString("sg_permission_128")
						+ data.getString("sg_permission_64") + data.getString("sg_permission_32") + data.getString("sg_permission_16")
						+ data.getString("sg_permission_8") + data.getString("sg_permission_4") + data.getString("sg_permission_2")
						+ data.getString("sg_permission_1"));
				// sys_p.setSysnote(data.getString("sys_note"));
				sys_p.setSyssort(data.getInt("sys_sort"));
				sys_p.setSysstatus(data.getInt("sys_status"));
				sys_p.setSysmuser(user.getSuname());
				sys_p.setSyscuser(user.getSuname());

				// 檢查群組名稱重複
				ArrayList<SystemGroup> sys_p_g = groupDao.findAllByGroupTop1(sys_p.getSgname(), PageRequest.of(0, 1));
				if (sys_p_g != null && sys_p_g.size() > 0) {
					// 重複(不存) 則取同樣G_ID
					// sys_p.setSggid(sys_p_g.get(0).getSggid());
					// groupDao.save(sys_p);
					return false;
				} else {
					// 新建 群組頭
					sys_p_g = groupDao.findAllByTop1(PageRequest.of(0, 1));
					SystemGroup sys_p_h = new SystemGroup();
					sys_p_h.setSggid((sys_p_g.get(0).getSggid() + 1));
					sys_p_h.setSgname(sys_p.getSgname());
					sys_p_h.setSgpermission("0000000000");
					sys_p_h.setSysheader(true);
					sys_p_h.setSystemPermission(new SystemPermission(1));

					sys_p_h.setSysnote("");
					sys_p_h.setSyssort(0);
					sys_p_h.setSysstatus(0);
					sys_p_h.setSysmuser(user.getSuname());
					sys_p_h.setSyscuser(user.getSuname());
					groupDao.save(sys_p_h);
					// 新建 取得最新G_ID
					sys_p.setSggid((sys_p_g.get(0).getSggid() + 1));
					groupDao.save(sys_p);
					check = true;
				}
			}

		} catch (Exception e) {
			System.out.println(e);
			return check;
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
				SystemGroup sys_p = new SystemGroup();
				JSONObject data = (JSONObject) one;
				sys_p.setSgid(data.getInt("sg_id"));
				sys_p.setSggid(data.getInt("sg_g_id"));
				sys_p.setSgname(data.getString("sg_name"));
				sys_p.setSgname(data.getString("sg_name"));
				SystemPermission p = new SystemPermission();
				p.setSpid(data.getInt("sg_sp_id"));
				sys_p.setSystemPermission(p);
				sys_p.setSgpermission(data.getString("sg_permission_512") + data.getString("sg_permission_256") + data.getString("sg_permission_128")
						+ data.getString("sg_permission_64") + data.getString("sg_permission_32") + data.getString("sg_permission_16")
						+ data.getString("sg_permission_8") + data.getString("sg_permission_4") + data.getString("sg_permission_2")
						+ data.getString("sg_permission_1"));
				// sys_p.setSysnote(data.getString("sys_note"));
				sys_p.setSyssort(data.getInt("sys_sort"));
				sys_p.setSysstatus(data.getInt("sys_status"));
				sys_p.setSysmdate(new Date());

				// 檢查[群組名稱]重複
				ArrayList<SystemGroup> sys_p_g = groupDao.findAllByGroupTop1(sys_p.getSgname(), PageRequest.of(0, 1));
				if (sys_p_g != null && sys_p_g.size() > 0) {
					// 如果是 父類別(限定修改)+(子類別全數修改)
					if (groupDao.findBySgidOrderBySgidAscSyssortAsc(sys_p.getSgid()).get(0).getSysheader()) {
						List<SystemGroup> sys_p_g_old = groupDao.findBySggidOrderBySggidAscSyssortAsc(sys_p.getSggid());
						sys_p_g_old.forEach(d -> {
							d.setSgname(sys_p.getSgname());
							d.setSysstatus(sys_p.getSysstatus());
							d.setSgpermission(sys_p.getSgpermission());
							groupDao.save(d);
						});
						// 父類別(限定修改) 還原
						sys_p.setSgpermission("0000000000");
						sys_p.setSysheader(true);
						sys_p.setSystemPermission(new SystemPermission(1));
						sys_p.setSyssort(0);
						groupDao.save(sys_p);
					} else {
						// 重複 則取同樣G_ID
						sys_p.setSggid(sys_p_g.get(0).getSggid());
						groupDao.save(sys_p);
					}
					check = true;
				} else {
					// 如果 不是修改類 則
					check = false;
				}

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
				SystemGroup sys_p = new SystemGroup();
				JSONObject data = (JSONObject) one;
				sys_p.setSgid(data.getInt("sg_id"));
				if (data.getInt("sg_sp_id") == 0) {
					// 父類別 關聯全清除
					groupDao.deleteBySggid(data.getInt("sg_g_id"));
				}
				groupDao.delete(sys_p);
				check = true;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return check;
	}
}
