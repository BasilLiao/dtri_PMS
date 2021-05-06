package dtri.com.tw.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
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
import dtri.com.tw.db.entity.ProductionHeader;
import dtri.com.tw.db.entity.ProductionRecords;
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.db.pgsql.dao.ProductionBodyDao;
import dtri.com.tw.db.pgsql.dao.ProductionHeaderDao;
import dtri.com.tw.tools.Fm_Time;

@Service
public class ProductionBodyService {
	@Autowired
	private ProductionBodyDao bodyDao;
	@Autowired
	private ProductionHeaderDao headerDao;

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
		PageRequest pageable = PageRequest.of(page, p_size, Sort.by("pbid").descending());
		String phprid = "";
		String phmodel = "";
		String sysstatus = "0";
		String pb_sn_value = "";
		String pb_sn_name = "";
		String pb_sn = "";
		String pb_sn_check = "";
		List<Integer> pbid = null;
		// 初次載入需要標頭 / 之後就不用
		if (body == null || body.isNull("search")) {

			// 放入包裝(header) [01 是排序][_h__ 是分割直][資料庫欄位名稱]
			JSONObject object_header = new JSONObject();
			int ord = 0;
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pb_id", FFS.h_t("SN_ID", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pb_g_id", FFS.h_t("SN_G_ID", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_id", FFS.h_t("TL_ID", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_pr_id", FFS.h_t("TL_工單號", "160px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_model", FFS.h_t("TL_產品型號", "160px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pb_sn", FFS.h_t("SN_(出貨/產品)", "250px", FFS.SHO));

			object_header.put(FFS.ord((ord += 1), FFS.H) + "pb_shipping_date", FFS.h_t("SN_出貨日", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pb_recycling_date", FFS.h_t("SN_回收日", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pb_position", FFS.h_t("SN_保固年", "150px", FFS.SHO));

			object_header.put(FFS.ord((ord += 1), FFS.H) + "pb_f_value", FFS.h_t("SN_維修項目", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pb_f_note", FFS.h_t("SN_維修說明", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pb_check", FFS.h_t("SN_完成?", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pb_useful_sn", FFS.h_t("SN_狀態", "150px", FFS.SHO));

			object_header.put(FFS.ord((ord += 1), FFS.H) + "pb_l_path", FFS.h_t("SN_檢測Log位置", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pb_l_text", FFS.h_t("SN_檢測Log內容", "150px", FFS.SHO));

			// sn關聯表
			int j = 0;
			Method method;
			for (j = 0; j < 50; j++) {
				String m_name = "getPbvalue" + String.format("%02d", j + 1);
				try {
					method = body_one.getClass().getMethod(m_name);
					String value = (String) method.invoke(body_one);
					String name = "pb_value" + String.format("%02d", j + 1);
					if (value != null && !value.equals("")) {
						object_header.put(FFS.ord((ord += 1), FFS.H) + name, FFS.h_t("SN_[" + value + "]", "180px", FFS.SHO));
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
			// 過站簽名
			for (j = 0; j < 20; j++) {
				String m_name = "getPbwname" + String.format("%02d", j + 1);
				try {
					method = body_one.getClass().getMethod(m_name);
					String value = (String) method.invoke(body_one);
					String name = "pb_w_name" + String.format("%02d", j + 1);
					if (value != null && !value.equals("")) {
						object_header.put(FFS.ord((ord += 1), FFS.H) + name, FFS.h_t("WP_過站簽名[" + value + "]", "270px", FFS.SHO));
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

			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_c_date", FFS.h_t("建立時間", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_c_user", FFS.h_t("建立人", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_m_date", FFS.h_t("修改時間", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_m_user", FFS.h_t("修改人", "100px", FFS.SHO));

			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_note", FFS.h_t("備註", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_sort", FFS.h_t("排序", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_ver", FFS.h_t("版本", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_status", FFS.h_t("狀態", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_header", FFS.h_t("群組", "100px", FFS.SHO));
			bean.setHeader(object_header);

			// 放入修改 [m__(key)](modify/Create/Delete) 格式
			JSONArray obj_m = new JSONArray();
			JSONArray a_val = new JSONArray();
			JSONArray n_val = new JSONArray();
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, n_val, "pb_id", "SN_ID"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-1", false, n_val, "pb_g_id", "TL_S_ID"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-1", false, n_val, "ph_id", "TL_ID"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, n_val, "ph_model", "TL_產品型號"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, n_val, "ph_pr_id", "TL_工單號"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, n_val, "pb_sn", "SN_(出貨/產品)"));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "完成").put("key", "true"));
			a_val.put((new JSONObject()).put("value", "未完成").put("key", "false"));
			obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "false", "false", FFS.SHO, "col-md-1", true, a_val, "pb_check", "SN_流程"));
			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "有效").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "已出貨").put("key", "1"));
			a_val.put((new JSONObject()).put("value", "可拆解").put("key", "2"));
			a_val.put((new JSONObject()).put("value", "已失效").put("key", "3"));
			obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "false", "false", FFS.SHO, "col-md-1", true, a_val, "pb_useful_sn", "SN_狀態"));

			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-12", true, n_val, "pb_f_value", "SN_維修項目"));
			obj_m.put(FFS.h_m(FFS.TTA, FFS.TEXT, "", "", FFS.DIS, "col-md-12", true, n_val, "pb_f_note", "SN_維修說明"));

			// sn關聯表
			for (j = 0; j < 50; j++) {
				String m_name = "getPbvalue" + String.format("%02d", j + 1);
				try {
					method = body_one.getClass().getMethod(m_name);
					String value = (String) method.invoke(body_one);
					String name = "pb_value" + String.format("%02d", j + 1);
					if (value != null && !value.equals("")) {
						obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", false, n_val, name, value));
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

			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-12", false, n_val, "pb_l_path", "SN_檢測Log位置"));
			obj_m.put(FFS.h_m(FFS.TTA, FFS.TEXT, "", "", FFS.DIS, "col-md-12", false, n_val, "pb_l_text", "SN_檢測Log內容"));
			obj_m.put(FFS.h_m(FFS.TTA, FFS.TEXT, "", "", FFS.SHO, "col-md-12", false, n_val, "sys_note", "備註"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, "", "", FFS.DIS, "col-md-1", false, n_val, "sys_header", "群組代表?"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, "", "", FFS.DIS, "col-md-1", false, n_val, "sys_ver", "版本"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, n_val, "sys_c_date", "建立時間"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, n_val, "sys_c_user", "建立人"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, n_val, "sys_m_date", "修改時間"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, n_val, "sys_m_user", "修改人"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, "0", "0", FFS.SHO, "col-md-1", true, n_val, "sys_sort", "排序"));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "", "0", FFS.SHO, "col-md-1", true, a_val, "sys_status", "狀態"));
			bean.setCell_modify(obj_m);

			// 放入包裝(search)
			JSONArray object_searchs = new JSONArray();
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "ph_model", "TL_產品型號", n_val));
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "ph_pr_id", "TL_製令單號", n_val));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			object_searchs.put(FFS.h_s(FFS.SEL, FFS.TEXT, "0", "col-md-1", "sys_status", "TL_狀態", a_val));

			// 項目查詢(選單)
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "pb_sn", "SN_出貨序號", n_val));
			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "完成").put("key", "true"));
			a_val.put((new JSONObject()).put("value", "未完成").put("key", "false"));
			object_searchs.put(FFS.h_s(FFS.SEL, FFS.TEXT, "0", "col-md-1", "pb_sn_check", "SN_流程完成?", a_val));
			// SN
			a_val = new JSONArray();
			for (j = 0; j < 50; j++) {
				String m_name = "getPbvalue" + String.format("%02d", j + 1);
				try {
					method = body_one.getClass().getMethod(m_name);
					String value = (String) method.invoke(body_one);
					String name = String.format("pb_value" + String.format("%02d", j + 1));
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
			object_searchs.put(FFS.h_s(FFS.SEL, FFS.TEXT, "0", "col-md-2", "pb_sn_name", "SN_類型", a_val));
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "0", "col-md-2", "pb_sn_value", "SN_值", n_val));

			bean.setCell_searchs(object_searchs);
		} else {
			// 進行-特定查詢
			phmodel = body.getJSONObject("search").getString("ph_model");
			phmodel = (phmodel == null) ? "" : phmodel;

			phprid = body.getJSONObject("search").getString("ph_pr_id");
			phprid = (phprid == null) ? "" : phprid;

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
			pb_sn_check = body.getJSONObject("search").getString("pb_sn_check");
			pb_sn_check = (pb_sn_check.equals("")) ? null : pb_sn_check;
		}

		// 查詢SN欄位+產品型號+製令單號
		String nativeQuery = "SELECT b.pb_id FROM production_body b join production_header h on b.pb_g_id = h.ph_pb_g_id  WHERE ";
		if (!pb_sn_value.equals("")) {
			nativeQuery += " (:pb_sn_value='' or " + pb_sn_name + " LIKE :pb_sn_value) and ";
		}
		nativeQuery += " (:pb_sn='' or b.pb_sn LIKE :pb_sn) and ";
		nativeQuery += " (:ph_model='' or h.ph_model LIKE :ph_model) and ";
		nativeQuery += " (:ph_pr_id='' or h.ph_pr_id LIKE :ph_pr_id) and ";
		nativeQuery += " (b.pb_g_id != 0) group by b.pb_id ";
		nativeQuery += " order by b.pb_id limit " + p_size + " offset " + page + " ";
		Query query = em.createNativeQuery(nativeQuery);
		if (!pb_sn_value.equals("")) {
			query.setParameter("pb_sn_value", "%" + pb_sn_value + "%");
		}
		query.setParameter("pb_sn", "%" + pb_sn + "%");
		query.setParameter("ph_model", "%" + phmodel + "%");
		query.setParameter("ph_pr_id", "%" + phprid + "%");
		pbid = (List<Integer>) query.getResultList();
		em.clear();
		em.close();
		// 如果有查SN 則要有值/沒查則pass
		if ((!pb_sn_value.equals("") && pbid.size() > 0) || pb_sn_value.equals("")) {
			if (pb_sn_check == null) {
				// 查詢有特定pb_id
				productionBodies = bodyDao.findAllByProductionBody(Integer.parseInt(sysstatus), pbid);
			} else {
				productionBodies = bodyDao.findAllByProductionBody(Integer.parseInt(sysstatus), pbid, Boolean.parseBoolean(pb_sn_check));
			}
		}

		// 放入包裝(body) [01 是排序][_b__ 是分割直][資料庫欄位名稱]
		JSONArray object_bodys = new JSONArray();
		productionBodies.forEach(one -> {
			JSONObject object_body = new JSONObject();
			ProductionHeader productionHeader = new ProductionHeader();
			int ord = 0;
			// 查詢最新的製令單
			productionHeader = headerDao.findTopByPhpbgidOrderBySysmdateDesc(one.getPbgid());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pb_id", one.getPbid());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pb_g_id", one.getPbgid());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "ph_id", productionHeader.getPhid());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "ph_pr_id", productionHeader.getProductionRecords().getPrid());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "ph_model", productionHeader.getPhmodel());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pb_sn", one.getPbsn());

			object_body.put(FFS.ord((ord += 1), FFS.B) + "pb_shipping_date", one.getPbshippingdate() == null ? "" : one.getPbshippingdate());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pb_recycling_date", one.getPbrecyclingdate() == null ? "" : one.getPbrecyclingdate());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pb_position", one.getPbposition() == null ? "" : one.getPbposition());

			object_body.put(FFS.ord((ord += 1), FFS.B) + "pb_f_value", one.getPbfvalue() == null ? "" : one.getPbfvalue());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pb_f_note", one.getPbfnote() == null ? "" : one.getPbfnote());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pb_check", one.getPbcheck());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pb_useful_sn", one.getPbusefulsn());

			object_body.put(FFS.ord((ord += 1), FFS.B) + "pb_l_path", one.getPblpath() == null ? "" : one.getPblpath());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pb_l_text", one.getPbltext() == null ? "" : one.getPbltext());

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
						String key_b = "pb_value" + String.format("%02d", k + 1);
						object_body.put(FFS.ord((ord += 1), FFS.B) + key_b, (value_b == null ? "" : value_b));
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
			try {
				// 有效設定的欄位
				for (int k = 0; k < 20; k++) {
					String in_name = "getPbwname" + String.format("%02d", k + 1);
					Method in_method = body_one.getClass().getMethod(in_name);
					String value = (String) in_method.invoke(body_one);
					// 欄位有定義的顯示
					if (value != null && !value.equals("")) {
						// sn關聯表
						String name_b = "getPbwname" + String.format("%02d", k + 1);
						Method method_b = one.getClass().getMethod(name_b);
						String value_b = (String) method_b.invoke(one);
						String key_b = "pb_w_name" + String.format("%02d", k + 1);
						object_body.put(FFS.ord((ord += 1), FFS.B) + key_b, (value_b == null ? "" : value_b));
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
				ProductionBody p_body = new ProductionBody();
				JSONObject data = (JSONObject) one;
				ProductionRecords search = new ProductionRecords();
				search.setPrid(data.getString("ph_pr_id"));
				List<ProductionHeader> p_Headers = headerDao.findAllByproductionRecords(search);
				List<ProductionBody> p_Bodys = bodyDao.findAllByPbsn(data.getString("pb_sn"));
				// 有資料
				if (p_Headers.size() < 1) {
					return check;
				}
				// 重複?
				if (p_Bodys.size() > 0) {
					return check;
				}
				// ProductionBody
				p_body.setPbusefulsn(data.getInt("pb_useful_sn"));
				p_body.setPbsn(data.getString("pb_sn"));
				p_body.setPbgid(p_Headers.get(0).getPhpbgid());
				p_body.setPbcheck(data.getBoolean("pb_check"));
				p_body.setSysnote(data.getString("sys_note"));
				p_body.setSyssort(data.getInt("sys_sort"));
				p_body.setSysstatus(data.getInt("sys_status"));
				p_body.setSysmuser(user.getSuname());
				p_body.setSyscuser(user.getSuname());
				// SN類別
				try {
					for (int k = 0; k < 50; k++) {
						// 有欄位?
						if (data.has("pb_value" + String.format("%02d", k + 1))) {
							String value = data.getString("pb_value" + String.format("%02d", k + 1));
							String in_name = "setPbvalue" + String.format("%02d", k + 1);
							Method in_method = p_body.getClass().getMethod(in_name, String.class);
							// 欄位有值
							if (value != null && !value.equals("")) {
								in_method.invoke(p_body, value);
							}
						}
					}
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
					return check;
				} catch (SecurityException e) {
					e.printStackTrace();
					return check;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					return check;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					return check;
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					return check;
				}
				bodyDao.save(p_body);
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
			for (Object one : list) {
				// 物件轉換
				ProductionBody p_body = new ProductionBody();
				JSONObject data = (JSONObject) one;
				ProductionRecords search = new ProductionRecords();
				search.setPrid(data.getString("ph_pr_id"));
				List<ProductionHeader> p_Headers = headerDao.findAllByproductionRecords(search);
				List<ProductionBody> p_Bodys = bodyDao.findAllByPbsn(data.getString("pb_sn"));
				// 有資料
				if (p_Headers.size() < 1) {
					return check;
				}
				// 重複?
				if (p_Bodys.size() > 0) {
					return check;
				}

				p_body.setPbusefulsn(data.getInt("pb_useful_sn"));
				p_body.setPbsn(data.getString("pb_sn"));
				p_body.setPbgid(p_Headers.get(0).getPhpbgid());
				p_body.setPbcheck(data.getBoolean("pb_check"));
				p_body.setSysnote(data.getString("sys_note"));
				p_body.setSyssort(data.getInt("sys_sort"));
				p_body.setSysstatus(data.getInt("sys_status"));
				p_body.setSysmuser(user.getSuname());
				p_body.setSyscuser(user.getSuname());
				// SN類別
				try {
					for (int k = 0; k < 50; k++) {
						// 有欄位?
						if (data.has("pb_value" + String.format("%02d", k + 1))) {
							String value = data.getString("pb_value" + String.format("%02d", k + 1));
							String in_name = "setPbvalue" + String.format("%02d", k + 1);
							Method in_method = p_body.getClass().getMethod(in_name, String.class);
							// 欄位有值
							if (value != null && !value.equals("")) {
								in_method.invoke(p_body, value);
							}
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
				bodyDao.save(p_body);
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
			for (Object one : list) {
				JSONObject data = (JSONObject) one;
				List<ProductionBody> p_Bodys = bodyDao.findAllByPbsn(data.getString("pb_sn"));
				ProductionRecords search = new ProductionRecords();
				search.setPrid(data.getString("ph_pr_id"));
				List<ProductionHeader> p_Headers = headerDao.findAllByproductionRecords(search);
				// 有資料?
				if (p_Headers.size() < 1) {
					return check;
				}
				// 重複? 不包含自己
				if (p_Bodys.size() > 0 && (!p_Bodys.get(0).getPbsn().equals(data.getString("pb_sn")))) {
					return check;
				}
				// 物件轉換
				ProductionBody pro_b = new ProductionBody();
				pro_b.setPbid(data.getInt("pb_id"));
				pro_b.setPbusefulsn(data.getInt("pb_useful_sn"));
				pro_b.setPbsn(data.getString("pb_sn"));
				pro_b.setPbgid(p_Headers.get(0).getPhpbgid());
				pro_b.setPbcheck(data.getBoolean("pb_check"));
				pro_b.setPbfvalue(data.getString("pb_f_value"));
				pro_b.setPbltext(data.getString("pb_l_text"));
				pro_b.setSysstatus(data.getInt("sys_status"));
				pro_b.setSyssort(data.getInt("sys_sort"));
				pro_b.setSysmuser(user.getSuname());
				pro_b.setSysmdate(new Date());
				try {
					for (int k = 0; k < 50; k++) {
						// 有欄位?
						if (data.has("pb_value" + String.format("%02d", k + 1))) {
							String value = data.getString("pb_value" + String.format("%02d", k + 1));
							String in_name = "setPbvalue" + String.format("%02d", k + 1);
							Method in_method = pro_b.getClass().getMethod(in_name, String.class);
							// 欄位有值
							if (value != null && !value.equals("")) {
								in_method.invoke(pro_b, value);
							}
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
				bodyDao.save(pro_b);
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
				JSONObject data = (JSONObject) one;
				bodyDao.deleteByPbid(data.getInt("pb_id"));
				check = true;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return check;
	}
}
