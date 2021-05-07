package dtri.com.tw.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.tw.bean.PackageBean;
import dtri.com.tw.db.entity.ProductionBody;
import dtri.com.tw.db.entity.SystemGroup;
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.db.entity.Workstation;
import dtri.com.tw.db.entity.WorkstationItem;
import dtri.com.tw.db.pgsql.dao.ProductionBodyDao;
import dtri.com.tw.db.pgsql.dao.SystemGroupDao;
import dtri.com.tw.db.pgsql.dao.WorkstationDao;
import dtri.com.tw.db.pgsql.dao.WorkstationItemDao;
import dtri.com.tw.db.pgsql.dao.WorkstationProgramDao;
import dtri.com.tw.tools.Fm_Time;

@Service
public class WorkstationService {
	@Autowired
	private WorkstationDao workstationDao;
	@Autowired
	private WorkstationItemDao itemDao;
	@Autowired
	private ProductionBodyDao bodyDao;
	@Autowired
	private SystemGroupDao groupDao;
	@Autowired
	private WorkstationProgramDao workpDao;

	// 取得當前 資料清單
	public PackageBean getData(JSONObject body, int page, int p_size) {
		PackageBean bean = new PackageBean();
		ArrayList<Workstation> workstations = new ArrayList<Workstation>();
		ArrayList<WorkstationItem> workstationItems = new ArrayList<WorkstationItem>();
		List<SystemGroup> systemGroup = new ArrayList<SystemGroup>();
		// 查詢的頁數，page=從0起算/size=查詢的每頁筆數
		if (p_size < 1) {
			page = 0;
			p_size = 100;
		}
		PageRequest page_r = PageRequest.of(page, p_size, Sort.by("wid").descending());
		String w_sg_name = null;
		String w_pb_name = null;
		String status = "0";
		// 初次載入需要標頭 / 之後就不用
		if (body == null || body.isNull("search")) {
			// 放入包裝(header) [01 是排序][_h__ 是分割直][資料庫欄位名稱]
			JSONObject object_header = new JSONObject();
			int ord = 0;
			object_header.put(FFS.ord((ord += 1), FFS.H) + "w_id", FFS.h_t("ID", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "w_g_id", FFS.h_t("群組ID", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "w_i_id", FFS.h_t("項目ID", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "w_codename", FFS.h_t("工作站條碼", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "w_pb_name", FFS.h_t("工作站名稱", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "w_pb_cell", FFS.h_t("工作站欄位", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "w_sg_id", FFS.h_t("可使用[群組]ID", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "w_sg_name", FFS.h_t("可使用[群組]名稱", "180px", FFS.SHO));

			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_t_date", FFS.h_t("建立時間", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_t_user", FFS.h_t("建立人", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_m_date", FFS.h_t("修改時間", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_m_user", FFS.h_t("修改人", "100px", FFS.SHO));

			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_note", FFS.h_t("備註", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_sort", FFS.h_t("排序", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_ver", FFS.h_t("版本", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_status", FFS.h_t("狀態", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_header", FFS.h_t("群組?", "100px", FFS.SHO));
			bean.setHeader(object_header);

			// 放入修改 [(key)](modify/Create/Delete) 格式
			JSONArray obj_m = new JSONArray();
			JSONArray n_val = new JSONArray();
			JSONArray a_val = new JSONArray();

			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "0", "0", FFS.DIS, "col-md-1", false, n_val, "w_id", "W_ID"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "0", "0", FFS.DIS, "col-md-1", false, n_val, "w_g_id", "W_群組ID"));

			workstationItems = itemDao.findAll();
			JSONArray a_vals1 = new JSONArray();
			workstationItems.forEach(s -> {
				a_vals1.put((new JSONObject()).put("value", s.getWipbvalue()).put("key", s.getWiid()));
			});
			obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, a_vals1, "w_i_id", "WI_項目ID"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "五碼[AA000]", "", FFS.SHO, "col-md-2", true, n_val, "w_c_name", "W_工作站碼"));

			// sn關聯表-工作站
			a_val = new JSONArray();
			int j = 0;
			Method method;
			ProductionBody body_one = bodyDao.findAllByPbid(0).get(0);
			for (j = 0; j < 20; j++) {
				String m_name = "getPbwname" + String.format("%02d", j + 1);
				try {
					method = body_one.getClass().getMethod(m_name);
					String value = (String) method.invoke(body_one);
					String name = "pb_w_name" + String.format("%02d", j + 1);
					if (value != null && !value.equals("")) {
						a_val.put((new JSONObject()).put("value", value).put("key", name));
					}
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, a_val, "w_pb_cell", "W_工作站[欄]"));
			// obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false,
			// n_val, "w_pb_name", "W_工作站[名]"));

			systemGroup = groupDao.findAllBySysheader(true, PageRequest.of(0, 999));

			JSONArray a_vals2 = new JSONArray();
			systemGroup.forEach(e -> {
				a_vals2.put((new JSONObject()).put("value", e.getSgname()).put("key", e.getSggid()));
			});
			obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, a_vals2, "w_sg_id", "SU_[群組]ID"));
			// obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false,
			// n_val, "w_sg_name", "SU_[群組]名"));

			// obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false,
			// n_val, "sys_t_date", "建立時間"));
			// obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false,
			// n_val, "sys_t_user", "建立人"));
			// obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false,
			// n_val, "sys_m_date", "修改時間"));
			// obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false,
			// n_val, "sys_m_user", "修改人"));

			// obj_m.put(FFS.h_m(FFS.TTA, FFS.TEXT, "", "", FFS.SHO, "col-md-12", false,
			// n_val, "sys_note", "備註"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, "0", "0", FFS.SHO, "col-md-1", true, n_val, "sys_sort", "排序"));
			// obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, "", "", FFS.DIS, "col-md-2", false,
			// n_val, "sys_ver", "版本"));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			// obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "", "0", FFS.SHO, "col-md-1", true,
			// a_val, "sys_status", "狀態"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-1", false, n_val, "sys_header", "群組?"));
			bean.setCell_modify(obj_m);

			// 放入包裝(search)
			JSONArray object_searchs = new JSONArray();
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "w_pb_name", "工作站[名稱]", n_val));
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "w_sg_name", "可使用者[群組]Name", n_val));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			object_searchs.put(FFS.h_s(FFS.SEL, FFS.TEXT, "0", "col-md-2", "sys_status", "狀態", a_val));
			bean.setCell_searchs(object_searchs);
		} else {

			// 進行-特定查詢
			w_sg_name = body.getJSONObject("search").getString("w_sg_name");
			w_sg_name = w_sg_name.equals("") ? null : w_sg_name;
			w_pb_name = body.getJSONObject("search").getString("w_pb_name");
			w_pb_name = w_pb_name.equals("") ? null : w_pb_name;
			status = body.getJSONObject("search").getString("sys_status");
			status = status.equals("") ? "0" : status;
		}
		workstations = workstationDao.findAllByWorkstation(w_sg_name, w_pb_name, Integer.parseInt(status), page_r);

		// 放入包裝(body) [01 是排序][_b__ 是分割直][資料庫欄位名稱]
		JSONArray object_bodys = new JSONArray();
		workstations.forEach(one -> {
			JSONObject object_body = new JSONObject();
			int ord = 0;

			object_body.put(FFS.ord((ord += 1), FFS.B) + "w_id", one.getWid());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "w_g_id", one.getWgid());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "w_i_id", one.getWorkstationItem().getWiid());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "w_c_name", one.getWcname());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "w_pb_name", one.getWpbname());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "w_pb_cell", one.getWpbcell());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "w_sg_id", one.getWsgid());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "w_sg_name", one.getWsgname());

			object_body.put(FFS.ord((ord += 1), FFS.B) + "sys_t_date", Fm_Time.to_yMd_Hms(one.getSyscdate()));
			object_body.put(FFS.ord((ord += 1), FFS.B) + "sys_t_user", one.getSyscuser());
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
			String wc_name = "";
			Integer wc_g_id = 0;
			String w_pb_value = "";
			String w_pb_cell = "";
			Method method;
			List<SystemGroup> systemGroup = new ArrayList<SystemGroup>();
			for (Object one : list) {
				JSONObject data = (JSONObject) one;
				if (wc_g_id == 0) {
					// 使用者群組
					systemGroup = groupDao.findBySggidOrderBySggidAscSyssortAsc(data.getInt("w_sg_id"));
					ProductionBody body_one = bodyDao.findAllByPbid(0).get(0);
					// 取得工作欄位 位置
					String w_pb_name = data.getString("w_pb_cell").replace("pb_w_name", "getPbwname");
					try {
						method = body_one.getClass().getMethod(w_pb_name);
						w_pb_value = (String) method.invoke(body_one);
						w_pb_cell = data.getString("w_pb_cell");
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
				// 物件轉換
				Workstation sys_t = new Workstation();
				WorkstationItem sys_ti = new WorkstationItem();
				sys_ti.setWiid(data.getInt("w_i_id"));
				sys_t.setWorkstationItem(sys_ti);
				sys_t.setWcname(data.getString("w_c_name"));
				sys_t.setWpbcell(w_pb_cell);
				sys_t.setWpbname(w_pb_value);
				sys_t.setWsgid(systemGroup.get(0).getSggid());
				sys_t.setWsgname(systemGroup.get(0).getSgname());
				sys_t.setSysnote("");
				sys_t.setSyssort(data.getInt("sys_sort"));
				sys_t.setSysstatus(0);
				sys_t.setSysheader(false);
				sys_t.setSysmuser(user.getSuname());
				sys_t.setSyscuser(user.getSuname());

				// 檢查_工作站碼重複
				ArrayList<Workstation> sys_t_g = workstationDao.findAllByWcname(sys_t.getWcname(), PageRequest.of(0, 1));
				// 檢查工作站 w_pb_cell 欄位重複
				if (sys_t_g.size() == 0) {
					sys_t_g = workstationDao.findAllByWpbcell(sys_t.getWpbcell(), PageRequest.of(0, 1));
				}

				if (sys_t_g != null && sys_t_g.size() > 0) {
					// 重複 則取同樣G_ID && 登入為子類別
					sys_t.setWcname(sys_t_g.get(0).getWcname());
					sys_t.setWgid(sys_t_g.get(0).getWgid());
					sys_t.setSysheader(false);
				} else {
					// 取得最新G_ID && 登記為父類別
					wc_g_id = workstationDao.getProduction_workstation_g_seq();
					wc_name = sys_t.getWcname();
					WorkstationItem sys_ti_f = new WorkstationItem();
					sys_ti_f.setWiid(0);
					Workstation sys_t_f = new Workstation();
					sys_t_f.setWgid(wc_g_id);
					sys_t_f.setWcname(wc_name);
					sys_t_f.setSysheader(true);
					sys_t_f.setWpbcell(w_pb_cell);
					sys_t_f.setWpbname(w_pb_value);
					sys_t_f.setWorkstationItem(sys_ti_f);
					sys_t_f.setSysnote("");
					sys_t_f.setSyssort(data.getInt("sys_sort"));
					sys_t_f.setSysstatus(0);
					sys_t_f.setWsgid(systemGroup.get(0).getSggid());
					sys_t_f.setWsgname(systemGroup.get(0).getSgname());
					sys_t_f.setSysmuser(user.getSuname());
					sys_t_f.setSyscuser(user.getSuname());
					workstationDao.save(sys_t_f);

					sys_t.setWgid(wc_g_id);
					sys_t.setWcname(wc_name);
				}

				workstationDao.save(sys_t);
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
			String wc_name = "";
			Integer wc_g_id = 0;
			String w_pb_value = "";
			String w_pb_cell = "";
			Method method;
			List<SystemGroup> systemGroup = new ArrayList<SystemGroup>();
			for (Object one : list) {
				JSONObject data = (JSONObject) one;
				if (wc_g_id == 0) {
					// 使用者群組
					systemGroup = groupDao.findBySggidOrderBySggidAscSyssortAsc(data.getInt("w_sg_id"));
					ProductionBody body_one = bodyDao.findAllByPbid(0).get(0);
					// 取得工作欄位 位置
					String w_pb_name = data.getString("w_pb_cell").replace("pb_w_name", "getPbwname");
					try {
						method = body_one.getClass().getMethod(w_pb_name);
						w_pb_value = (String) method.invoke(body_one);
						w_pb_cell = data.getString("w_pb_cell");
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
				// 物件轉換
				Workstation sys_t = new Workstation();
				WorkstationItem sys_ti = new WorkstationItem();
				sys_ti.setWiid(data.getInt("w_i_id"));
				sys_t.setWorkstationItem(sys_ti);
				sys_t.setWcname(data.getString("w_c_name"));
				sys_t.setWpbcell(w_pb_cell);
				sys_t.setWpbname(w_pb_value);
				sys_t.setWsgid(systemGroup.get(0).getSggid());
				sys_t.setWsgname(systemGroup.get(0).getSgname());
				sys_t.setSysnote("");
				sys_t.setSyssort(data.getInt("sys_sort"));
				sys_t.setSysstatus(0);
				sys_t.setSysheader(false);
				sys_t.setSysmuser(user.getSuname());
				sys_t.setSyscuser(user.getSuname());

				// 檢查_工作站碼重複
				ArrayList<Workstation> sys_t_g = workstationDao.findAllByWcname(sys_t.getWcname(), PageRequest.of(0, 1));
				// 檢查工作站 w_pb_cell 欄位重複
				if (sys_t_g.size() == 0) {
					sys_t_g = workstationDao.findAllByWpbcell(sys_t.getWpbcell(), PageRequest.of(0, 1));
				}

				if (sys_t_g != null && sys_t_g.size() > 0) {
					// 重複 則取同樣G_ID && 登入為子類別
					sys_t.setWcname(sys_t_g.get(0).getWcname());
					sys_t.setWgid(sys_t_g.get(0).getWgid());
					sys_t.setSysheader(false);
				} else {
					// 取得最新G_ID && 登記為父類別
					wc_g_id = workstationDao.getProduction_workstation_g_seq();
					wc_name = sys_t.getWcname();
					WorkstationItem sys_ti_f = new WorkstationItem();
					sys_ti_f.setWiid(0);
					Workstation sys_t_f = new Workstation();
					sys_t_f.setWgid(wc_g_id);
					sys_t_f.setWcname(wc_name);
					sys_t_f.setSysheader(true);
					sys_t_f.setWpbcell(w_pb_cell);
					sys_t_f.setWpbname(w_pb_value);
					sys_t_f.setWorkstationItem(sys_ti_f);
					sys_t_f.setSysnote("");
					sys_t_f.setSyssort(data.getInt("sys_sort"));
					sys_t_f.setSysstatus(0);
					sys_t_f.setWsgid(systemGroup.get(0).getSggid());
					sys_t_f.setWsgname(systemGroup.get(0).getSgname());
					sys_t_f.setSysmuser(user.getSuname());
					sys_t_f.setSyscuser(user.getSuname());
					workstationDao.save(sys_t_f);

					sys_t.setWgid(wc_g_id);
					sys_t.setWcname(wc_name);
				}
				workstationDao.save(sys_t);
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
			Workstation sys_t = new Workstation();
			String w_pb_value = "";
			String w_pb_cell = "";
			String wc_name = "";
			Method method;
			List<SystemGroup> systemGroup = new ArrayList<SystemGroup>();
			// 物件轉換
			for (Object one : list) {
				JSONObject data = (JSONObject) one;
				sys_t.setWid(data.getInt("w_id"));

				// 父類別
				if (data.getBoolean("sys_header")) {
					// 使用者群組
					systemGroup = groupDao.findBySggidOrderBySggidAscSyssortAsc(data.getInt("w_sg_id"));
					ProductionBody body_one = bodyDao.findAllByPbid(0).get(0);
					// 取得工作欄位 位置
					String w_pb_name = data.getString("w_pb_cell").replace("pb_w_name", "getPbwname");
					try {
						method = body_one.getClass().getMethod(w_pb_name);
						w_pb_value = (String) method.invoke(body_one);
						w_pb_cell = data.getString("w_pb_cell");
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}

					// 檢查_工作站碼重複
					ArrayList<Workstation> sys_t_g = workstationDao.findAllByWcnameAndWcnameNot(data.getString("w_c_name"),
							data.getString("w_c_name"), PageRequest.of(0, 1));
					// 檢查工作站 w_pb_cell 欄位重複
					if (sys_t_g.size() == 0) {
						sys_t_g = workstationDao.findAllByWpbcellAndWpbcellNot(data.getString("w_pb_cell"), data.getString("w_pb_cell"),
								PageRequest.of(0, 1));
					}
					// 如果重複則 退回
					if (sys_t_g.size() > 0) {
						return false;
					}
					WorkstationItem sys_ti_f = new WorkstationItem();
					sys_ti_f.setWiid(0);
					sys_t.setWorkstationItem(sys_ti_f);
					sys_t.setWgid(data.getInt("w_g_id"));
					sys_t.setWcname(data.getString("w_c_name"));
					sys_t.setWpbcell(data.getString("w_pb_cell"));
					sys_t.setWpbname(w_pb_value);
					sys_t.setWpbcell(w_pb_cell);
					sys_t.setWsgid(systemGroup.get(0).getSggid());
					sys_t.setWsgname(systemGroup.get(0).getSgname());
					sys_t.setSysmuser(user.getSuname());
					sys_t.setSyscuser(user.getSuname());
					sys_t.setSysheader(true);
					sys_t.setSyssort(0);
					workstationDao.save(sys_t);

					// 更新每一筆資料
					ArrayList<Workstation> workstations = workstationDao.findAllByWgid(data.getInt("w_g_id"));
					for (Workstation w : workstations) {
						w.setWcname(data.getString("w_c_name"));
						w.setWpbname(w_pb_value);
						w.setWpbcell(w_pb_cell);
						w.setWsgid(systemGroup.get(0).getSggid());
						w.setWsgname(systemGroup.get(0).getSgname());
						w.setSysmuser(user.getSuname());
						w.setSyscuser(user.getSuname());
					}
					workstationDao.saveAll(workstations);
					wc_name = data.getString("w_c_name");
				} else {
					// 子類別
					WorkstationItem sys_ti = new WorkstationItem();
					sys_ti.setWiid(data.getInt("w_i_id"));
					sys_t.setWcname(wc_name);
					sys_t.setWorkstationItem(sys_ti);
					sys_t.setSysheader(false);
					sys_t.setWpbname(w_pb_value);
					sys_t.setWpbcell(w_pb_cell);
					sys_t.setWsgid(systemGroup.get(0).getSggid());
					sys_t.setWsgname(systemGroup.get(0).getSgname());
					sys_t.setSysmuser(user.getSuname());
					sys_t.setSyscuser(user.getSuname());
					sys_t.setSyssort(data.getInt("sys_sort"));
					workstationDao.save(sys_t);
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
				Workstation sys_t = new Workstation();
				JSONObject data = (JSONObject) one;
				// 群組?
				// 如果程序正在使用-不能移除
				if (data.getBoolean("sys_header")) {
					if (workpDao.findAllByWpwgid(data.getInt("w_g_id")).size() > 0) {
						return false;
					}
					workstationDao.deleteByWgid(sys_t.getWgid());
					continue;
				}
				sys_t.setWid(data.getInt("w_id"));
				sys_t.setWgid(data.getInt("w_g_id"));
				workstationDao.deleteByWidAndSysheader(sys_t.getWid(), false);
				check = true;
			}
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return check;
	}
}
