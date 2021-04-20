package dtri.com.tw.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.tw.bean.PackageBean;
import dtri.com.tw.db.entity.ProductionBody;
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.db.pgsql.dao.ProductionBodyDao;
import dtri.com.tw.tools.Fm_Time;

@Service
public class ProductionBodyService {
	@Autowired
	private ProductionBodyDao bodyDao;
	@Autowired
	EntityManager em;

	// 取得當前 資料清單
	public PackageBean getData(JSONObject body, int page, int p_size) {
		PackageBean bean = new PackageBean();
		List<ProductionBody> productionBodies = new ArrayList<ProductionBody>();
		ProductionBody body_one = bodyDao.findAllByPbid(0).get(0);
		// 查詢的頁數，page=從0起算/size=查詢的每頁筆數
		if (p_size < 1) {
			page = 0;
			p_size = 100;
		}
		PageRequest page_r = PageRequest.of(page, p_size, Sort.by("pbid").descending());
		String phmodel = "";
		String phprid = "";
		String sysstatus = "0";
		String pb_sn_value = "";
		String pb_sn_name = "";
		String pb_sn = "";
		List<Integer> pbphid = null;
		// 初次載入需要標頭 / 之後就不用
		if (body == null || body.isNull("search")) {

			// 放入包裝(header) [01 是排序][_h__ 是分割直][資料庫欄位名稱]
			JSONObject object_header = new JSONObject();
			int ord = 1;
			String.format("%02d", (ord += 1));

			object_header.put("01_h__pb_id", FFS.h_t("SN_ID", "100px", FFS.SHO));
			object_header.put("02_h__pb_ph_id", FFS.h_t("TL_ID", "100px", FFS.SHO));
			object_header.put("03_h__ph_pr_id", FFS.h_t("TL_工單號", "160px", FFS.SHO));
			object_header.put("04_h__ph_model", FFS.h_t("TL_產品型號", "160px", FFS.SHO));
			object_header.put("05_h__pb_sn", FFS.h_t("SN_(出貨/產品)", "250px", FFS.SHO));

			object_header.put("06_h__pb_f_value", FFS.h_t("SN_維修項目", "150px", FFS.SHO));
			object_header.put("07_h__pb_f_note", FFS.h_t("SN_維修說明", "150px", FFS.SHO));
			object_header.put("08_h__pb_check", FFS.h_t("SN_檢驗完成", "150px", FFS.SHO));
			object_header.put("09_h__pb_l_path", FFS.h_t("SN_檢測Log位置", "150px", FFS.SHO));
			object_header.put("10_h__pb_l_text", FFS.h_t("SN_檢測Log內容", "150px", FFS.SHO));

			int j = 0;
			int j_now = 10;
			Method method;
			for (j = 0; j < 50; j++) {
				String m_name = "getPbvalue" + String.format("%02d", j + 1);
				try {
					method = body_one.getClass().getMethod(m_name);
					String value = (String) method.invoke(body_one);
					String name = String.format("%02d", j + 11) + "_h__pb_value" + String.format("%02d", j + 1);
					if (value != null && !value.equals("")) {
						object_header.put(name, FFS.h_t("SN_" + value, "180px", FFS.SHO));
						j_now += 1;
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			object_header.put((j_now + 1) + "_h__sys_c_date", FFS.h_t("建立時間", "150px", FFS.SHO));
			object_header.put((j_now + 2) + "_h__sys_c_user", FFS.h_t("建立人", "100px", FFS.SHO));
			object_header.put((j_now + 3) + "_h__sys_m_date", FFS.h_t("修改時間", "150px", FFS.SHO));
			object_header.put((j_now + 4) + "_h__sys_m_user", FFS.h_t("修改人", "100px", FFS.SHO));

			object_header.put((j_now + 5) + "_h__sys_note", FFS.h_t("備註", "100px", FFS.SHO));
			object_header.put((j_now + 6) + "_h__sys_sort", FFS.h_t("排序", "100px", FFS.SHO));
			object_header.put((j_now + 7) + "_h__sys_ver", FFS.h_t("版本", "100px", FFS.SHO));
			object_header.put((j_now + 8) + "_h__sys_status", FFS.h_t("狀態", "100px", FFS.SHO));
			object_header.put((j_now + 9) + "_h__sys_header", FFS.h_t("群組", "100px", FFS.SHO));
			bean.setHeader(object_header);

			// 放入修改 [m__(key)](modify/Create/Delete) 格式
			JSONArray obj_m = new JSONArray();
			JSONArray values = new JSONArray();

			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, new JSONArray(), "m__pb_id", "SN_ID"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, new JSONArray(), "m__pb_ph_id", "TL_ID"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, new JSONArray(), "m__ph_pr_id", "TL_工單號"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, new JSONArray(), "m__ph_model", "TL_產品型號"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, new JSONArray(), "m__pb_sn", "SN_(出貨/產品)"));

			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, new JSONArray(), "m__pb_f_value", "SN_維修項目"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, new JSONArray(), "m__pb_f_note", "SN_維修說明"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, new JSONArray(), "m__pb_check", "SN_流程完成"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, new JSONArray(), "m__pb_l_path", "SN_檢測Log位置"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, new JSONArray(), "m__pb_l_text", "SN_檢測Log內容"));

			// sn關聯表
			for (j = 0; j < 50; j++) {
				String m_name = "getPbvalue" + String.format("%02d", j + 1);
				try {
					method = body_one.getClass().getMethod(m_name);
					String value = (String) method.invoke(body_one);
					String name = "m__pb_value" + String.format("%02d", j + 1);
					if (value != null && !value.equals("")) {
						obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", false, values, name, value));
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

			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, new JSONArray(), "m__sys_c_date", "建立時間"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, new JSONArray(), "m__sys_c_user", "建立人"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, new JSONArray(), "m__sys_m_date", "修改時間"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, new JSONArray(), "m__sys_m_user", "修改人"));

			obj_m.put(FFS.h_m(FFS.TTA, FFS.TEXT, "", "", FFS.SHO, "col-md-12", false, new JSONArray(), "m__sys_note", "備註"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, "0", "0", FFS.SHO, "col-md-2", true, new JSONArray(), "m__sys_sort", "排序"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, "", "", FFS.DIS, "col-md-2", false, new JSONArray(), "m__sys_ver", "版本"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, "", "", FFS.DIS, "col-md-1", false, new JSONArray(), "m__sys_header", "群組"));

			values = new JSONArray();
			values.put((new JSONObject()).put("value", "正常").put("key", "0"));
			values.put((new JSONObject()).put("value", "異常").put("key", "1"));
			obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "", "0", FFS.SHO, "col-md-2", true, values, "m__sys_status", "狀態"));
			bean.setCell_modify(obj_m);

			// 放入包裝(search)
			JSONArray object_searchs = new JSONArray();
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "ph_model", "TL_產品型號", new JSONArray()));
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "ph_pr_id", "TL_製令單號", new JSONArray()));

			values = new JSONArray();
			values.put((new JSONObject()).put("value", "正常").put("key", "0"));
			values.put((new JSONObject()).put("value", "異常").put("key", "1"));
			object_searchs.put(FFS.h_s(FFS.SEL, FFS.TEXT, "0", "col-md-1", "sys_status", "TL_狀態", values));

			// 項目查詢(選單)
			values = new JSONArray();
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "pb_sn", "SN_出貨序號", new JSONArray()));

			for (j = 0; j < 50; j++) {
				String m_name = "getPbvalue" + String.format("%02d", j + 1);
				try {
					method = body_one.getClass().getMethod(m_name);
					String value = (String) method.invoke(body_one);
					String name = String.format("pb_value" + String.format("%02d", j + 1));
					if (value != null && !value.equals("")) {
						values.put((new JSONObject()).put("value", value).put("key", name));
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
			object_searchs.put(FFS.h_s(FFS.SEL, FFS.TEXT, "0", "col-md-2", "pb_sn_name", "SN_類型", values));
			values = new JSONArray();
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "0", "col-md-2", "pb_sn_value", "SN_值", values));
			bean.setCell_searchs(object_searchs);
		} else {

			// 進行-特定查詢
			phmodel = body.getJSONObject("search").getString("ph_model");
			phmodel = (phmodel.equals("")) ? null : phmodel;

			phprid = body.getJSONObject("search").getString("ph_pr_id");
			phprid = (phprid.equals("")) ? null : phprid;

			sysstatus = body.getJSONObject("search").getString("sys_status");
			sysstatus = (sysstatus.equals("")) ? "0" : sysstatus;

			pb_sn = body.getJSONObject("search").getString("pb_sn");
			pb_sn = (pb_sn == null) ? "" : pb_sn;

			pb_sn_name = body.getJSONObject("search").getString("pb_sn_name");
			pb_sn_name = pb_sn_name == null ? "" : pb_sn_name;

			pb_sn_value = body.getJSONObject("search").getString("pb_sn_value");
			pb_sn_value = pb_sn_value == null ? "" : pb_sn_value;

			if (pb_sn_name.equals("") || pb_sn_value.equals("")) {
				pb_sn_value = "";
				pb_sn_name = "";
			}
		}

		// 查詢SN欄位
		if (!pb_sn.equals("") || !pb_sn_value.equals("")) {
			String nativeQuery = "SELECT pb_ph_id FROM production_body WHERE ";
			if (!pb_sn_value.equals("")) {
				nativeQuery += " (:pb_sn_value='' or " + pb_sn_name + " LIKE :pb_sn_value) and ";
			}
			nativeQuery += " (:pb_sn='' or pb_sn LIKE :pb_sn) and ";
			nativeQuery += " (pb_ph_id != 0) group by pb_ph_id ";

			Query query = em.createNativeQuery(nativeQuery);
			if (!pb_sn_value.equals("")) {
				query.setParameter("pb_sn_value", "%" + pb_sn_value + "%");
			}
			query.setParameter("pb_sn", "%" + pb_sn + "%");
			pbphid = (List<Integer>) query.getResultList();
			em.clear();
			em.close();
		}

		productionBodies = bodyDao.findAllByProductionBody(phmodel, phprid, Integer.parseInt(sysstatus), pbphid, page_r);

		// 放入包裝(body) [01 是排序][_b__ 是分割直][資料庫欄位名稱]
		JSONArray object_bodys = new JSONArray();
		productionBodies.forEach(one -> {
			JSONObject object_body = new JSONObject();
			object_body.put("01_b__pb_id", one.getPbid());
			object_body.put("02_b__pb_ph_id", one.getProductionHeader().getPhid());
			object_body.put("03_b__ph_pr_id", one.getProductionHeader().getPhprid());
			object_body.put("04_b__ph_model", one.getProductionHeader().getPhmodel());
			object_body.put("05_b__pb_sn", one.getPbsn());

			object_body.put("06_b__pb_f_value", one.getPbfvalue() == null ? "" : one.getPbfvalue());
			object_body.put("07_b__pb_f_note", one.getPbfnote() == null ? "" : one.getPbfnote());
			object_body.put("08_b__pb_check", one.getPbcheck());
			object_body.put("09_b__pb_l_path", one.getPblpath() == null ? "" : one.getPblpath());
			object_body.put("10_b__pb_l_text", one.getPbltext() == null ? "" : one.getPbltext());

			int j_now = 11;
			try {
				// 有效設定的欄位
				for (int k = 0; k < 50; k++) {
					String in_name = "getPbvalue" + String.format("%02d", k + 1);
					Method in_method = body_one.getClass().getMethod(in_name);
					String value = (String) in_method.invoke(body_one);
					// 欄位有定義的顯示
					if (value != null && !value.equals("")) {
						// sn關聯表
						String name_b = "getPbvalue" + String.format("%02d", k + 1);
						Method method_b = one.getClass().getMethod(name_b);
						String value_b = (String) method_b.invoke(one);
						String key_b = String.format("%02d", j_now) + "_b__pb_value" + String.format("%02d", k + 1);
						object_body.put(key_b, (value_b == null ? "" : value_b));
						j_now += 1;
					}
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

			object_body.put((j_now) + "_b__sys_c_date", Fm_Time.to_yMd_Hms(one.getSyscdate()));
			object_body.put((j_now + 1) + "_b__sys_c_user", one.getSyscuser());
			object_body.put((j_now + 2) + "_b__sys_m_date", Fm_Time.to_yMd_Hms(one.getSysmdate()));
			object_body.put((j_now + 3) + "_b__sys_m_user", one.getSysmuser());
			object_body.put((j_now + 4) + "_b__sys_note", one.getSysnote());
			object_body.put((j_now + 5) + "_b__sys_sort", one.getSyssort());
			object_body.put((j_now + 6) + "_b__sys_ver", one.getSysver());
			object_body.put((j_now + 7) + "_b__sys_status", one.getSysstatus());
			object_body.put((j_now + 8) + "_b__sys_header", one.getSysheader());
			object_bodys.put(object_body);
		});
		bean.setBody(new JSONObject().put("search", object_bodys));
		// bean.setBody_type("fatherSon");
		return bean;
	}

	// 存檔 資料清單
	@Transactional
	public boolean createData(JSONObject body, SystemUser user) {
		boolean check = false;
//		try {
//			JSONArray list = body.getJSONArray("create");
//			for (Object one : list) {
//				// 物件轉換
//				ProductionBody sys_c = new ProductionBody();
//				JSONObject data = (JSONObject) one;
//				sys_c.setScname(data.getString("sc_name"));
//				sys_c.setScgname(data.getString("sc_g_name"));
//				sys_c.setScvalue(data.getString("sc_value"));
//				sys_c.setSysnote(data.getString("sys_note"));
//				sys_c.setSyssort(data.getInt("sys_sort"));
//				sys_c.setSysstatus(data.getInt("sys_status"));
//				sys_c.setSysmuser(user.getSuname());
//				sys_c.setSyscuser(user.getSuname());
//
//				// 檢查群組名稱重複
//				ArrayList<ProductionBody> sys_p_g = bodyDao.findAllByConfigGroupTop1(sys_c.getScgname(), PageRequest.of(0, 1));
//				if (sys_p_g != null && sys_p_g.size() > 0) {
//					// 重複 則取同樣G_ID
//					sys_c.setScgid(sys_p_g.get(0).getScgid());
//				} else {
//					// 取得最新G_ID
//					sys_p_g = bodyDao.findAllByTop1(PageRequest.of(0, 1));
//					sys_c.setScgid((sys_p_g.get(0).getScgid() + 1));
//				}
//				bodyDao.save(sys_c);
//				check = true;
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//		}
		return check;
	}

	// 存檔 資料清單
	@Transactional
	public boolean save_asData(JSONObject body, SystemUser user) {
		boolean check = false;
//		try {
//			JSONArray list = body.getJSONArray("save_as");
//			for (Object one : list) {
//				// 物件轉換
//				ProductionBody sys_c = new ProductionBody();
//				JSONObject data = (JSONObject) one;
//				sys_c.setScname(data.getString("sc_name"));
//				sys_c.setScgname(data.getString("sc_g_name"));
//				sys_c.setScvalue(data.getString("sc_value"));
//				sys_c.setSysnote(data.getString("sys_note"));
//				sys_c.setSyssort(data.getInt("sys_sort"));
//				sys_c.setSysstatus(data.getInt("sys_status"));
//				sys_c.setSysmuser(user.getSuname());
//				sys_c.setSyscuser(user.getSuname());
//
//				// 檢查群組名稱重複
//				ArrayList<ProductionBody> sys_c_g = bodyDao.findAllByConfigGroupTop1(sys_c.getScgname(), PageRequest.of(0, 1));
//				if (sys_c_g != null && sys_c_g.size() > 0) {
//					// 重複 則取同樣G_ID
//					sys_c.setScgid(sys_c_g.get(0).getScgid());
//				} else {
//					// 取得最新G_ID
//					sys_c_g = bodyDao.findAllByTop1(PageRequest.of(0, 1));
//					sys_c.setScgid((sys_c_g.get(0).getScgid() + 1));
//				}
//				bodyDao.save(sys_c);
//				check = true;
//			}
//
//		} catch (Exception e) {
//			System.out.println(e);
//		}
		return check;
	}

	// 更新 資料清單
	@Transactional
	public boolean updateData(JSONObject body, SystemUser user) {
		boolean check = false;
//		try {
//			JSONArray list = body.getJSONArray("modify");
//			for (Object one : list) {
//				// 物件轉換
//				ProductionBody sys_p = new ProductionBody();
//				JSONObject data = (JSONObject) one;
//				sys_p.setScid(data.getInt("sc_id"));
//				sys_p.setScname(data.getString("sc_name"));
//				sys_p.setScgid(data.getInt("sc_g_id"));
//				sys_p.setScgname(data.getString("sc_g_name"));
//				sys_p.setScvalue(data.getString("sc_value"));
//				sys_p.setSysnote(data.getString("sys_note"));
//				sys_p.setSyssort(data.getInt("sys_sort"));
//				sys_p.setSysstatus(data.getInt("sys_status"));
//				sys_p.setSysmdate(new Date());
//
//				// 檢查群組名稱重複
//				ArrayList<ProductionBody> sys_p_g = bodyDao.findAllByConfigGroupTop1(sys_p.getScgname(), PageRequest.of(0, 1));
//				if (sys_p_g != null && sys_p_g.size() > 0) {
//					// 重複 則取同樣G_ID
//					sys_p.setScgid(sys_p_g.get(0).getScgid());
//				} else {
//					// 取得最新G_ID
//					sys_p_g = bodyDao.findAllByTop1(PageRequest.of(0, 1));
//					sys_p.setScgid((sys_p_g.get(0).getScgid() + 1));
//				}
//				bodyDao.save(sys_p);
//				check = true;
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//		}
		return check;
	}

	// 移除 資料清單
	@Transactional
	public boolean deleteData(JSONObject body) {

		boolean check = false;
//		try {
//			JSONArray list = body.getJSONArray("delete");
//			for (Object one : list) {
//				// 物件轉換
//				ProductionBody sys_p = new ProductionBody();
//				JSONObject data = (JSONObject) one;
//				sys_p.setScid(data.getInt("sc_id"));
//
//				bodyDao.deleteByScidAndSysheader(sys_p.getScid(), false);
//				check = true;
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//		}
		return check;
	}
}
