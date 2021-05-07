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
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.db.entity.Workstation;
import dtri.com.tw.db.entity.WorkstationProgram;
import dtri.com.tw.db.pgsql.dao.WorkstationDao;
import dtri.com.tw.db.pgsql.dao.WorkstationProgramDao;
import dtri.com.tw.tools.Fm_Time;

@Service
public class WorkstationProgramService {
	@Autowired
	private WorkstationProgramDao programDao;
	@Autowired
	private WorkstationDao workstationDao;

	// 取得當前 資料清單
	public PackageBean getData(JSONObject body, int page, int p_size) {
		PackageBean bean = new PackageBean();
		ArrayList<WorkstationProgram> workstationPrograms = new ArrayList<WorkstationProgram>();
		ArrayList<Workstation> workstations = new ArrayList<Workstation>();
		// 查詢的頁數，page=從0起算/size=查詢的每頁筆數
		if (p_size < 1) {
			page = 0;
			p_size = 100;
		}
		PageRequest page_r = PageRequest.of(page, p_size, Sort.by("wpid").descending());
		String wp_name = null;
		String wp_c_name = null;
		String status = "0";
		// 初次載入需要標頭 / 之後就不用
		if (body == null || body.isNull("search")) {
			// 放入包裝(header) [01 是排序][_h__ 是分割直][資料庫欄位名稱]
			JSONObject object_header = new JSONObject();
			int ord = 0;
			object_header.put(FFS.ord((ord += 1), FFS.H) + "wp_id", FFS.h_t("ID", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "wp_g_id", FFS.h_t("群組ID", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "wp_name", FFS.h_t("工作程序名稱", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "wp_c_name", FFS.h_t("工作程序代號", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "wp_w_g_id", FFS.h_t("工作站ID", "100px", FFS.SHO));

			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_c_date", FFS.h_t("建立時間", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_c_user", FFS.h_t("建立人", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_m_date", FFS.h_t("修改時間", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_m_user", FFS.h_t("修改人", "100px", FFS.SHO));

			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_note", FFS.h_t("備註", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_sort", FFS.h_t("排序", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_ver", FFS.h_t("版本", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_status", FFS.h_t("狀態", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_header", FFS.h_t("群組代表", "100px", FFS.SHO));

			bean.setHeader(object_header);

			// 放入修改 [(key)](modify/Create/Delete) 格式
			JSONArray obj_m = new JSONArray();
			JSONArray n_val = new JSONArray();
			JSONArray a_val = new JSONArray();

			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-1", false, n_val, "sys_header", "工作程序代表"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-1", false, n_val, "wp_id", "ID"));

			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, n_val, "wp_g_id", "群組ID"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, n_val, "wp_name", "工作程序名稱"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-1", true, n_val, "wp_c_name", "工作程序代號"));

			JSONArray a_vals = new JSONArray();
			workstations = workstationDao.findAllBySysheader(true, PageRequest.of(0, 999));
			workstations.forEach(w -> {
				a_vals.put((new JSONObject()).put("value", w.getWpbname()).put("key", w.getWgid()));
			});

			obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, a_vals, "wp_w_g_id", "工作站ID"));

			// obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false,
			// n_val, "sys_c_date", "建立時間"));
			// obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false,
			// n_val, "sys_c_user", "建立人"));
			// obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false,
			// n_val, "sys_m_date", "修改時間"));
			// obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2",
			// false,n_val, "sys_m_user", "修改人"));

			// obj_m.put(FFS.h_m(FFS.TTA, FFS.TEXT, "", "", FFS.SHO, "col-md-12", false,
			// n_val, "sys_note", "備註"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, "0", "0", FFS.SHO, "col-md-1", true, n_val, "sys_sort", "排序"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, "0", "0", FFS.DIS, "col-md-1", false, n_val, "sys_ver", "版本"));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "", "0", FFS.DIS, "col-md-1", true, a_val, "sys_status", "狀態"));
			bean.setCell_modify(obj_m);

			// 放入包裝(search)
			JSONArray object_searchs = new JSONArray();
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "wp_c_name", "工作程序代號", n_val));
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "wp_name", "工作程序名稱", n_val));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			object_searchs.put(FFS.h_s(FFS.SEL, FFS.TEXT, "0", "col-md-2", "sys_status", "狀態", a_val));
			bean.setCell_searchs(object_searchs);
		} else {

			// 進行-特定查詢
			wp_name = body.getJSONObject("search").getString("wp_name");
			wp_name = wp_name.equals("") ? null : wp_name;
			wp_c_name = body.getJSONObject("search").getString("wp_c_name");
			wp_c_name = wp_c_name.equals("") ? null : wp_c_name;
			status = body.getJSONObject("search").getString("sys_status");
			status = status.equals("") ? "0" : status;
		}
		workstationPrograms = programDao.findAllByProgram(wp_name, wp_c_name, Integer.parseInt(status), page_r);

		// 放入包裝(body) [01 是排序][_b__ 是分割直][資料庫欄位名稱]
		JSONArray object_bodys = new JSONArray();
		workstationPrograms.forEach(one -> {
			JSONObject object_body = new JSONObject();
			int ord = 0;
			object_body.put(FFS.ord((ord += 1), FFS.B) + "wp_id", one.getWpid());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "wp_g_id", one.getWpgid());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "wp_c_name", one.getWpcname());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "wp_name", one.getWpname());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "wp_w_g_id", one.getWpwgid());

			object_body.put(FFS.ord((ord += 1), FFS.B) + "sys_c_date", Fm_Time.to_yMd_Hms(one.getSyscdate()));
			object_body.put(FFS.ord((ord += 1), FFS.B) + "sys_c_user", one.getSyscuser());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "sys_m_date", Fm_Time.to_yMd_Hms(one.getSysmdate()));
			object_body.put(FFS.ord((ord += 1), FFS.B) + "sys_m_user", one.getSysmuser());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "sys_note", one.getSysnote());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "sys_sort", one.getSyssort());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "sys_ver", one.getSysver());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "sys_status", one.getSysstatus());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "sys_header", one.getSysheader());
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
			ArrayList<WorkstationProgram> workstationPrograms = new ArrayList<WorkstationProgram>();
			int wpgid = 0;
			String wp_c_name = "";
			String wp_name = "";
			for (Object one : list) {
				// 物件轉換
				WorkstationProgram sys_c = new WorkstationProgram();
				JSONObject data = (JSONObject) one;
				// 檢查名稱重複?
				workstationPrograms = programDao.findAllByWpcnameOrWpname(data.getString("wp_c_name"), data.getString("wp_name"));
				if (workstationPrograms.size() > 0) {
					wp_c_name = workstationPrograms.get(0).getWpcname();
					wp_name = workstationPrograms.get(0).getWpname();
					wpgid = workstationPrograms.get(0).getWpgid();
				}
				if (wpgid == 0 && workstationPrograms.size() == 0) {
					if (workstationPrograms.size() > 0) {
						return false;
					}
					// 添加父類別
					WorkstationProgram wpf = new WorkstationProgram();
					wpgid = programDao.getWorkstation_program_g_seq();
					wpf.setWpgid(wpgid);
					wpf.setWpcname(data.getString("wp_c_name"));
					wpf.setWpname(data.getString("wp_name"));
					wpf.setWpwgid(0);
					wpf.setSyssort(0);
					wpf.setSysnote("");
					wpf.setSysstatus(0);
					wpf.setSysheader(true);
					wpf.setSysmuser(user.getSuname());
					wpf.setSyscuser(user.getSuname());
					programDao.save(wpf);
					wp_c_name = data.getString("wp_c_name");
					wp_name = data.getString("wp_name");

					// 子類別
					sys_c.setWpgid(wpgid);
					sys_c.setWpcname(data.getString("wp_c_name"));
					sys_c.setWpname(data.getString("wp_name"));
					sys_c.setWpwgid(data.getInt("wp_w_g_id"));
					sys_c.setSyssort(data.getInt("sys_sort"));
					sys_c.setSysstatus(0);
					sys_c.setSysnote("");
					sys_c.setSysheader(false);
					sys_c.setSysmuser(user.getSuname());
					sys_c.setSyscuser(user.getSuname());
					programDao.save(sys_c);

				} else {
					// 添加到該類子類別
					sys_c.setWpcname(wp_c_name);
					sys_c.setWpname(wp_name);
					sys_c.setWpgid(wpgid);

					sys_c.setSysnote("");
					sys_c.setSyssort(data.getInt("sys_sort"));
					sys_c.setSysstatus(data.getInt("sys_status"));
					sys_c.setWpwgid(data.getInt("wp_w_g_id"));
					sys_c.setSysheader(false);
					sys_c.setSysmuser(user.getSuname());
					sys_c.setSyscuser(user.getSuname());
					programDao.save(sys_c);
				}

			}
			check = true;
		} catch (Exception e) {
			System.out.println(e);
		}
		return check;
	}

	// 存檔 資料清單
	@Transactional
	public boolean save_asData(JSONObject body, SystemUser user) {
		boolean check = false;
		try {
			JSONArray list = body.getJSONArray("save_as");
			ArrayList<WorkstationProgram> workstationPrograms = new ArrayList<WorkstationProgram>();
			int wpgid = 0;
			String wp_c_name = "";
			String wp_name = "";
			for (Object one : list) {
				// 物件轉換
				WorkstationProgram sys_c = new WorkstationProgram();
				JSONObject data = (JSONObject) one;
				// 檢查名稱重複?
				if (wpgid == 0) {
					workstationPrograms = programDao.findAllByWpcnameOrWpname(data.getString("wp_c_name"), data.getString("wp_name"));
					if (workstationPrograms.size() > 0) {
						return false;
					}
					wp_c_name = data.getString("wp_c_name");
					wp_name = data.getString("wp_name");
					// 添加父類別
					WorkstationProgram wpf = new WorkstationProgram();
					wpgid = programDao.getWorkstation_program_g_seq();
					wpf.setWpgid(wpgid);
					wpf.setWpcname(data.getString("wp_c_name"));
					wpf.setWpname(data.getString("wp_name"));
					wpf.setWpwgid(0);
					wpf.setSyssort(0);
					wpf.setSysnote("");
					wpf.setSysstatus(0);
					wpf.setSysheader(true);
					wpf.setSysmuser(user.getSuname());
					wpf.setSyscuser(user.getSuname());
					programDao.save(wpf);

					if (list.length() == 1) {
						// 子類別
						sys_c.setWpgid(wpgid);
						sys_c.setWpcname(data.getString("wp_c_name"));
						sys_c.setWpname(data.getString("wp_name"));
						sys_c.setWpwgid(data.getInt("wp_w_g_id"));
						sys_c.setSyssort(data.getInt("sys_sort"));
						sys_c.setSysstatus(0);
						sys_c.setSysnote("");
						sys_c.setSysheader(false);
						sys_c.setSysmuser(user.getSuname());
						sys_c.setSyscuser(user.getSuname());
						programDao.save(sys_c);
					}

				} else {
					// 添加到該類子類別
					sys_c.setWpcname(wp_c_name);
					sys_c.setWpname(wp_name);
					sys_c.setWpgid(wpgid);

					sys_c.setSysnote("");
					sys_c.setSyssort(data.getInt("sys_sort"));
					sys_c.setSysstatus(data.getInt("sys_status"));
					sys_c.setWpwgid(data.getInt("wp_w_g_id"));
					sys_c.setSysheader(false);
					sys_c.setSysmuser(user.getSuname());
					sys_c.setSyscuser(user.getSuname());
					programDao.save(sys_c);
				}

			}
			check = true;
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
			ArrayList<WorkstationProgram> workstationPrograms = new ArrayList<WorkstationProgram>();
			String wp_name = "", wp_c_name = "";
			for (Object one : list) {
				// 物件轉換
				WorkstationProgram sys_p = new WorkstationProgram();
				JSONObject data = (JSONObject) one;
				// 父類別
				if (data.getBoolean("sys_header")) {
					// 檢查名稱重複?
					if (programDao.findAllByWpcnameAndWpcnameNot(data.getString("wp_c_name"), data.getString("wp_c_name")).size() > 0 || //
							programDao.findAllByWpnameAndWpnameNot(data.getString("wp_name"), data.getString("wp_name")).size() > 0) {
						return false;
					}
					sys_p.setWpid(data.getInt("wp_id"));
					sys_p.setWpname(data.getString("wp_name"));
					sys_p.setWpgid(data.getInt("wp_g_id"));
					sys_p.setWpcname(data.getString("wp_c_name"));
					sys_p.setWpwgid(0);
					sys_p.setSysnote("");
					sys_p.setSyssort(0);
					sys_p.setSysstatus(data.getInt("sys_status"));
					sys_p.setSysmuser(user.getSuname());
					sys_p.setSysmdate(new Date());
					sys_p.setSysheader(true);
					programDao.save(sys_p);
					// 更新子類別
					workstationPrograms = programDao.findAllByWpgidOrderBySyssortAsc(data.getInt("wp_g_id"));
					workstationPrograms.forEach(wp -> {
						wp.setWpname(data.getString("wp_name"));
						wp.setWpcname(data.getString("wp_c_name"));
					});
					programDao.saveAll(workstationPrograms);
					wp_name = data.getString("wp_name");
					wp_c_name = data.getString("wp_c_name");

				} else {
					sys_p.setWpid(data.getInt("wp_id"));
					sys_p.setWpgid(data.getInt("wp_g_id"));
					sys_p.setWpname(wp_name);
					sys_p.setWpcname(wp_c_name);
					sys_p.setWpwgid(data.getInt("wp_w_g_id"));
					sys_p.setSysnote("");
					sys_p.setSyssort(data.getInt("sys_sort"));
					sys_p.setSysstatus(data.getInt("sys_status"));
					sys_p.setSysmuser(user.getSuname());
					sys_p.setSysmdate(new Date());
					programDao.save(sys_p);
				}
			}
			// 有更新才正確
			if (list.length() > 0) {
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
				WorkstationProgram sys_p = new WorkstationProgram();
				JSONObject data = (JSONObject) one;
				// 群組移除
				if (data.getBoolean("sys_header")) {
					programDao.deleteByWpgid(data.getInt("wp_g_id"));
					continue;
				}
				sys_p.setWpid(data.getInt("wp_id"));
				programDao.deleteByWpidAndSysheader(sys_p.getWpid(), false);
				check = true;
			}
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return check;
	}
}
