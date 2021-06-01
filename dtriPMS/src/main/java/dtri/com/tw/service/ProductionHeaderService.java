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
import dtri.com.tw.db.entity.ProductionSN;
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.db.entity.Workstation;
import dtri.com.tw.db.entity.WorkstationProgram;
import dtri.com.tw.db.pgsql.dao.ProductionBodyDao;
import dtri.com.tw.db.pgsql.dao.ProductionHeaderDao;
import dtri.com.tw.db.pgsql.dao.ProductionSNDao;
import dtri.com.tw.db.pgsql.dao.WorkstationDao;
import dtri.com.tw.db.pgsql.dao.WorkstationProgramDao;
import dtri.com.tw.tools.Fm_SN;
import dtri.com.tw.tools.Fm_Time;

@Service
public class ProductionHeaderService {
	@Autowired
	private ProductionHeaderDao productionHeaderDao;

	@Autowired
	private WorkstationProgramDao programDao;

	@Autowired
	private WorkstationDao workDao;

	@Autowired
	private ProductionBodyDao productionBodyDao;

	@Autowired
	private ProductionSNDao snDao;

	@Autowired
	EntityManager em;

	// 取得當前 資料清單
	public PackageBean getData(JSONObject body, int page, int p_size) {
		PackageBean bean = new PackageBean();
		List<ProductionHeader> productionHeaders = new ArrayList<ProductionHeader>();
		ProductionBody body_one = productionBodyDao.findAllByPbid(0).get(0);
		// 查詢的頁數，page=從0起算/size=查詢的每頁筆數
		if (p_size < 1) {
			page = 0;
			p_size = 100;
		}
		PageRequest page_r = PageRequest.of(page, p_size, Sort.by("phid").descending());
		String phmodel = "";
		String phprid = "";
		String sysstatus = "0";
		String pb_sn_value = "";
		String pb_sn_name = "";
		String pb_sn = "";
		String pr_order_id = "";
		String pr_c_name = "";
		String pr_bom_id = "";
		String pr_b_item = "";
		String pr_s_item = "";
		List<Integer> pbid = null;
		// 初次載入需要標頭 / 之後就不用
		if (body == null || body.isNull("search")) {
			// 放入包裝(header) [01 是排序][_h__ 是分割直][資料庫欄位名稱]
			JSONObject object_header = new JSONObject();
			int ord = 0;
			// header
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_id", FFS.h_t("TL_ID", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_pb_g_id", FFS.h_t("TL_SN_G_ID", "130px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_pr_id", FFS.h_t("TL_製令單號", "180px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_wp_id", FFS.h_t("TL_工作站程序", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_s_date", FFS.h_t("TL_製令開始", "120px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_e_date", FFS.h_t("TL_製令結束", "120px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_schedule", FFS.h_t("TL_進度(X／X)", "130px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_type", FFS.h_t("TL_製令類", "150px", FFS.SHO));
			// 規格
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_order_id", FFS.h_t("PR_訂單編號", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_c_name", FFS.h_t("PR_客戶名稱", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_bom_id", FFS.h_t("PR_BOM料號", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_c_from", FFS.h_t("PR_單據來源", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_p_model", FFS.h_t("PR_產品型號", "150px", FFS.SHO));

			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_b_item", FFS.h_t("PR_規格定義", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_s_item", FFS.h_t("PR_軟體定義", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_p_quantity", FFS.h_t("PR_生產數", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_p_ok_quantity", FFS.h_t("PR_完成數", "150px", FFS.SHO));

			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_s_sn", FFS.h_t("PR_產品SN_開始", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_e_sn", FFS.h_t("PR_產品SN_結束", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_w_years", FFS.h_t("PR_保固", "100px", FFS.SHO));

			// sn 設定
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ps_sn_1", FFS.h_t("P(SN)機種", "110px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ps_sn_2", FFS.h_t("P(SN)廠別", "110px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ps_sn_3", FFS.h_t("P(SN)保固", "110px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ps_sn_4", FFS.h_t("P(SN)週期", "110px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ps_sn_5", FFS.h_t("P(SN)面板", "110px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ps_sn_6", FFS.h_t("P(SN)流水", "110px", FFS.SHO));

			// sys
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_c_date", FFS.h_t("TL_建立時間", "180px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_c_user", FFS.h_t("TL_建立人", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_m_date", FFS.h_t("TL_修改時間", "180px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_m_user", FFS.h_t("TL_修改人", "100px", FFS.SHO));

			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_note", FFS.h_t("TL_備註", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_sort", FFS.h_t("TL_排序", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_ver", FFS.h_t("TL_版本", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_status", FFS.h_t("TL_狀態", "100px", FFS.SHO));

			bean.setHeader(object_header);

			// 放入修改 [(key)](modify/Create/Delete) 格式
			JSONArray obj_m = new JSONArray();
			JSONArray n_val = new JSONArray();
			JSONArray a_val = new JSONArray();
			// header
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-1", false, n_val, "ph_id", "TL_ID"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-1", false, n_val, "ph_pb_g_id", "TL_SN_ID"));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "一般製令_No(sn)").put("key", "A511_No_SN"));
			a_val.put((new JSONObject()).put("value", "一般製令_Cr(sn)").put("key", "A511"));
			a_val.put((new JSONObject()).put("value", "重工製令_No(sn)").put("key", "A521_No_SN"));
			a_val.put((new JSONObject()).put("value", "重工_Re(sn)").put("key", "A521"));
			a_val.put((new JSONObject()).put("value", "維護製令").put("key", "A522"));
			a_val.put((new JSONObject()).put("value", "拆解製令").put("key", "A431"));
			a_val.put((new JSONObject()).put("value", "委外製令").put("key", "A512"));
			obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "A511", "A511", FFS.SHO, "col-md-2", true, a_val, "ph_type", "TL_製令類"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, n_val, "ph_pr_id", "TL_製令單號"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", true, n_val, "ph_s_date", "TL_開始(時)"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", true, n_val, "ph_e_date", "TL_結束(時)"));
			ArrayList<WorkstationProgram> list_p = programDao.findAllBySysheader(true);
			JSONArray a_val_p = new JSONArray();
			list_p.forEach(p -> {
				a_val_p.put((new JSONObject()).put("value", p.getWpname()).put("key", p.getWpgid()));
			});
			obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, a_val_p, "ph_wp_id", "TL_工作站"));
			// 規格-ProductionRecords
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, n_val, "pr_p_model", "PR_產品型號"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, n_val, "pr_bom_id", "PR_BOM料號"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, n_val, "pr_order_id", "PR_訂單編號"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, n_val, "pr_c_name", "PR_客戶名稱"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "13碼", "", FFS.DIS, "col-md-2", false, n_val, "pr_s_sn", "PR_產品SN_開始"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "13碼", "", FFS.DIS, "col-md-2", false, n_val, "pr_e_sn", "PR_產品SN_結束"));

			obj_m.put(FFS.h_m(FFS.TTA, FFS.TEXT, "", "", FFS.DIS, "col-md-6", true, n_val, "pr_b_item", "PR_規格定義"));
			obj_m.put(FFS.h_m(FFS.TTA, FFS.TEXT, "", "", FFS.DIS, "col-md-6", true, n_val, "pr_s_item", "PR_軟體定義"));
			// sys
			obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, "0", "0", FFS.SHO, "col-md-1", true, n_val, "pr_p_quantity", "PR_需產數"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, "0", "0", FFS.DIS, "col-md-1", true, n_val, "pr_p_ok_quantity", "PR_完成數"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, n_val, "pr_c_from", "PR_單據來源"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, n_val, "sys_c_date", "TL_建立時間"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, n_val, "sys_c_user", "TL_建立人"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, n_val, "sys_m_date", "TL_修改時間"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, n_val, "sys_m_user", "TL_修改人"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", true, n_val, "ph_schedule", "TL_進度"));

			// sn規格定義(固定範圍)
			JSONArray a_val_1 = new JSONArray();
			JSONArray a_val_2 = new JSONArray();
			JSONArray a_val_3 = new JSONArray();
			String a_val_4 = "0000";
			JSONArray a_val_5 = new JSONArray();
			String a_val_6 = "000";
			ArrayList<ProductionSN> p_sn = snDao.findAllBySysheaderOrderByPsgidAsc(false);
			for (ProductionSN productionSN : p_sn) {
				switch (productionSN.getPsgid()) {
				case 1:
					a_val_1.put((new JSONObject()).put("value", productionSN.getPsname()).put("key", productionSN.getPsvalue()));
					break;
				case 2:
					a_val_2.put((new JSONObject()).put("value", productionSN.getPsname()).put("key", productionSN.getPsvalue()));
					break;
				case 3:
					a_val_3.put((new JSONObject()).put("value", productionSN.getPsname()).put("key", productionSN.getPsvalue()));
					break;
				case 4:
					a_val_4 = productionSN.getPsvalue();
					break;
				case 5:
					a_val_5.put((new JSONObject()).put("value", productionSN.getPsname()).put("key", productionSN.getPsvalue()));
					break;
				case 6:
					a_val_6 = productionSN.getPsvalue();
					break;
				default:
					break;
				}
			}
			obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "", "", FFS.SHO, "col-md-1", true, a_val_1, "ps_sn_1", "P_SN_機種"));
			obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "", "", FFS.SHO, "col-md-1", true, a_val_2, "ps_sn_2", "P_SN_廠別"));
			obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "", "", FFS.SHO, "col-md-1", true, a_val_3, "ps_sn_3", "P_SN_保固"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, a_val_4, a_val_4, FFS.DIS, "col-md-1", true, n_val, "ps_sn_4", "P_SN_週期"));
			obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "", "", FFS.SHO, "col-md-1", true, a_val_5, "ps_sn_5", "P_SN_面板"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, a_val_6, a_val_6, FFS.DIS, "col-md-1", true, n_val, "ps_sn_6", "P_SN_流水"));

			obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, "0", "0", FFS.DIS, "col-md-1", false, n_val, "sys_ver", "TL_版本"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, "0", "0", FFS.DIS, "col-md-1", true, n_val, "sys_sort", "TL_排序"));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "待命中").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "生產中").put("key", "1"));
			a_val.put((new JSONObject()).put("value", "已完成").put("key", "2"));
			a_val.put((new JSONObject()).put("value", "暫停中").put("key", "8"));
			a_val.put((new JSONObject()).put("value", "已終止").put("key", "9"));
			obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "0", "0", FFS.DIS, "col-md-1", true, a_val, "sys_status", "TL_狀態"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, "0", "0", FFS.SHO, "col-md-1", false, n_val, "pr_w_years", "PR_保固"));
			obj_m.put(FFS.h_m(FFS.TTA, FFS.TEXT, "", "", FFS.SHO, "col-md-12", false, n_val, "sys_note", "TL_備註"));

			bean.setCell_modify(obj_m);

			// 放入包裝(search)
			// 製令查詢
			JSONArray object_searchs = new JSONArray();
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "ph_pr_id", "TL_製令單號", n_val));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			object_searchs.put(FFS.h_s(FFS.SEL, FFS.TEXT, "0", "col-md-2", "sys_status", "TL_狀態", a_val));
			// 規格查詢
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "pr_p_model", "PR_產品型號", n_val));
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "pr_order_id", "PR_訂單號", n_val));
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "pr_c_name", "PR_客戶名稱", n_val));
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "pr_bom_id", "PR_BOM", n_val));
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "pr_b_item", "PR_軟體定義", n_val));
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "pr_s_item", "PR_規格定義", n_val));

			// 項目查詢(選單)

			a_val = new JSONArray();
			for (int j = 0; j < 50; j++) {
				String m_name = "getPbvalue" + String.format("%02d", j + 1);
				try {
					Method method = body_one.getClass().getMethod(m_name);
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
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "pb_sn", "SN_出貨序號", n_val));

			bean.setCell_searchs(object_searchs);
		} else {
			// 進行-特定查詢
			phmodel = body.getJSONObject("search").getString("pr_p_model");
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
			pr_order_id = body.getJSONObject("search").getString("pr_order_id");
			pr_order_id = (pr_order_id.equals("")) ? null : pr_order_id;

			pr_c_name = body.getJSONObject("search").getString("pr_c_name");
			pr_c_name = (pr_c_name.equals("")) ? null : pr_c_name;

			pr_bom_id = body.getJSONObject("search").getString("pr_bom_id");
			pr_bom_id = (pr_bom_id.equals("")) ? null : pr_bom_id;

			pr_b_item = body.getJSONObject("search").getString("pr_b_item");
			pr_b_item = (pr_b_item.equals("")) ? null : pr_b_item;

			pr_s_item = body.getJSONObject("search").getString("pr_s_item");
			pr_s_item = (pr_s_item.equals("")) ? null : pr_s_item;
		}
		// 產品型號+製令單號
		String nativeQuery = "SELECT b.pb_g_id FROM production_body b join production_header h on b.pb_g_id = h.ph_pb_g_id  WHERE ";
		if (!pb_sn_value.equals("")) {
			nativeQuery += " (:pb_sn_value='' or " + pb_sn_name + " LIKE :pb_sn_value) and ";
		}
		nativeQuery += " (:pb_sn='' or b.pb_sn LIKE :pb_sn) and ";
		nativeQuery += " (b.pb_g_id != 0) group by b.pb_g_id ";
		nativeQuery += " order by b.pb_g_id limit " + p_size + " offset " + page + " ";
		Query query = em.createNativeQuery(nativeQuery);
		if (!pb_sn_value.equals("")) {
			query.setParameter("pb_sn_value", "%" + pb_sn_value + "%");
		}
		query.setParameter("pb_sn", "%" + pb_sn + "%");
		pbid = (List<Integer>) query.getResultList();
		// 如果有查SN 則要有值/沒查則pass
		if ((!pb_sn_value.equals("") && pbid.size() > 0) || pb_sn_value.equals("")) {
			productionHeaders = productionHeaderDao.findAllByProductionHeader(phmodel, phprid, Integer.parseInt(sysstatus), pbid, pr_order_id,
					pr_c_name, pr_bom_id, pr_b_item, pr_s_item, page_r);
		}
		// 放入包裝(body) [01 是排序][_b__ 是分割直][資料庫欄位名稱]
		JSONArray object_bodys = new JSONArray();
		JSONObject object_bodys_all = new JSONObject();

		productionHeaders.forEach(one -> {
			JSONObject object_body = new JSONObject();
			// header
			int ord = 0;
			object_body.put(FFS.ord((ord += 1), FFS.B) + "ph_id", one.getPhid());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "ph_pb_g_id", one.getPhpbgid());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "ph_pr_id", one.getProductionRecords().getPrid());

			object_body.put(FFS.ord((ord += 1), FFS.B) + "ph_wp_id", one.getPhwpid());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "ph_s_date", one.getPhsdate() == null ? "" : one.getPhsdate());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "ph_e_date", one.getPhedate() == null ? "" : one.getPhedate());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "ph_schedule", one.getPhschedule());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "ph_type", one.getPhtype());

			// 規格-ProductionRecords
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_order_id", one.getProductionRecords().getProrderid());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_c_name", one.getProductionRecords().getPrcname());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_bom_id", one.getProductionRecords().getPrbomid());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_c_from", one.getProductionRecords().getPrcfrom());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_p_model", one.getProductionRecords().getPrpmodel());

			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_b_item", one.getProductionRecords().getPrbitem());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_s_item", one.getProductionRecords().getPrsitem());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_p_quantity", one.getProductionRecords().getPrpquantity());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_p_ok_quantity", one.getProductionRecords().getPrpokquantity());

			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_s_sn", one.getProductionRecords().getPrssn());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_e_sn", one.getProductionRecords().getPresn());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_w_years", one.getProductionRecords().getPrwyears());
			// SN設定
			object_body.put(FFS.ord((ord += 1), FFS.B) + "ps_sn_1", one.getProductionRecords().getPrssn().subSequence(0, 3));
			object_body.put(FFS.ord((ord += 1), FFS.B) + "ps_sn_2", one.getProductionRecords().getPrssn().subSequence(3, 4));
			object_body.put(FFS.ord((ord += 1), FFS.B) + "ps_sn_3", one.getProductionRecords().getPrssn().subSequence(4, 5));
			object_body.put(FFS.ord((ord += 1), FFS.B) + "ps_sn_4", one.getProductionRecords().getPrssn().subSequence(5, 9));
			object_body.put(FFS.ord((ord += 1), FFS.B) + "ps_sn_5", one.getProductionRecords().getPrssn().subSequence(9, 10));
			object_body.put(FFS.ord((ord += 1), FFS.B) + "ps_sn_6", one.getProductionRecords().getPrssn().subSequence(10, 13));

			// sys
			object_body.put(FFS.ord((ord += 1), FFS.B) + "sys_c_date", Fm_Time.to_yMd_Hms(one.getSyscdate()));
			object_body.put(FFS.ord((ord += 1), FFS.B) + "sys_c_user", one.getSyscuser());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "sys_m_date", Fm_Time.to_yMd_Hms(one.getSysmdate()));
			object_body.put(FFS.ord((ord += 1), FFS.B) + "sys_m_user", one.getSysmuser());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "sys_note", one.getSysnote());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "sys_sort", one.getSyssort());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "sys_ver", one.getSysver());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "sys_status", one.getSysstatus());

			object_bodys.put(object_body);
		});
		object_bodys_all.put("search", object_bodys);

		// 刷新修改畫面 [(key)](modify/Create/Delete) 格式
		JSONArray obj_m = new JSONArray();
		JSONArray n_val = new JSONArray();

		// sn規格定義(固定範圍)
		JSONArray a_val_1 = new JSONArray();
		JSONArray a_val_2 = new JSONArray();
		JSONArray a_val_3 = new JSONArray();
		String a_val_4 = "0000";
		JSONArray a_val_5 = new JSONArray();
		String a_val_6 = "000";
		ArrayList<ProductionSN> p_sn = snDao.findAllBySysheaderOrderByPsgidAsc(false);
		for (ProductionSN productionSN : p_sn) {
			switch (productionSN.getPsgid()) {
			case 1:
				a_val_1.put((new JSONObject()).put("value", productionSN.getPsname()).put("key", productionSN.getPsvalue()));
				break;
			case 2:
				a_val_2.put((new JSONObject()).put("value", productionSN.getPsname()).put("key", productionSN.getPsvalue()));
				break;
			case 3:
				a_val_3.put((new JSONObject()).put("value", productionSN.getPsname()).put("key", productionSN.getPsvalue()));
				break;
			case 4:
				a_val_4 = productionSN.getPsvalue();
				break;
			case 5:
				a_val_5.put((new JSONObject()).put("value", productionSN.getPsname()).put("key", productionSN.getPsvalue()));
				break;
			case 6:
				a_val_6 = productionSN.getPsvalue();
				break;
			default:
				break;
			}
		}
		//obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "", "", FFS.SHO, "col-md-1", true, a_val_1, "ps_sn_1", "P_SN_機種"));
		//obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "", "", FFS.SHO, "col-md-1", true, a_val_2, "ps_sn_2", "P_SN_廠別"));
		//obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "", "", FFS.SHO, "col-md-1", true, a_val_3, "ps_sn_3", "P_SN_保固"));
		obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, a_val_4, a_val_4, FFS.DIS, "col-md-1", true, n_val, "ps_sn_4", "P_SN_週期"));
		//obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "", "", FFS.SHO, "col-md-1", true, a_val_5, "ps_sn_5", "P_SN_面板"));
		obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, a_val_6, a_val_6, FFS.DIS, "col-md-1", true, n_val, "ps_sn_6", "P_SN_流水"));
		bean.setCell_refresh(obj_m);

		bean.setBody(object_bodys_all);
		
		//是否為群組模式? type:[group/general] || 新增時群組? createOnly:[all/general] 
		bean.setBody_type(new JSONObject("{'type':'group','createOnly':'general'}"));
		return bean;
	}

	@Transactional
	public boolean createData(JSONObject body, SystemUser user) {
		boolean check = false;
		try {
			JSONArray list = body.getJSONArray("create");

			for (Object one : list) {
				// 物件轉換
				ProductionHeader pro_h = new ProductionHeader();
				// ProductionBody pro_b = new ProductionBody();
				ProductionRecords pro_r = new ProductionRecords();
				ArrayList<ProductionSN> pro_sn = new ArrayList<ProductionSN>();
				JSONObject data = (JSONObject) one;

				// 查詢重複
				ProductionRecords search = new ProductionRecords();
				search.setPrid(data.getString("ph_pr_id"));
				List<ProductionHeader> headers = productionHeaderDao.findAllByProductionRecords(search);

				if (headers.size() > 0) {
					pro_h = headers.get(0);
				}
				// 無重複->新建
				if (headers.size() < 1) {
					// header
					// int id_h = productionHeaderDao.getProductionHeaderSeq();
					// int id_b = productionBodyDao.getProductionBodySeq();
					int id_b_g = productionBodyDao.getProductionBodyGSeq();

					// sn
					pro_sn = new ArrayList<ProductionSN>();
					ProductionSN pro_sn_one = new ProductionSN();
					pro_sn_one.setPsvalue(data.getString("ps_sn_1"));
					pro_sn_one.setPsgid(1);
					pro_sn.add(pro_sn_one);

					pro_sn_one = new ProductionSN();
					pro_sn_one.setPsvalue(data.getString("ps_sn_2"));
					pro_sn_one.setPsgid(2);
					pro_sn.add(pro_sn_one);

					pro_sn_one = new ProductionSN();
					pro_sn_one.setPsvalue(data.getString("ps_sn_3"));
					pro_sn_one.setPsgid(3);
					pro_sn.add(pro_sn_one);

					pro_sn_one = new ProductionSN();
					pro_sn_one.setPsvalue(data.getString("ps_sn_4"));
					pro_sn_one.setPsgid(4);
					pro_sn_one.setPsname("[YYWW]");
					pro_sn.add(pro_sn_one);

					pro_sn_one = new ProductionSN();
					pro_sn_one.setPsvalue(data.getString("ps_sn_5"));
					pro_sn_one.setPsgid(5);
					pro_sn.add(pro_sn_one);

					pro_sn_one = new ProductionSN();
					pro_sn_one.setPsvalue(data.getString("ps_sn_6"));
					pro_sn_one.setPsgid(6);
					pro_sn_one.setPsname("[000]");
					pro_sn.add(pro_sn_one);
					JSONObject sn_list = Fm_SN.analyze_Sn(pro_sn, true, data.getInt("pr_p_quantity"));

					// 更新SN區段
					ProductionSN pro_sn_YYMM = snDao.findAllByPsid(8).get(0);
					pro_sn_YYMM.setPsvalue(sn_list.getString("sn_YYWW"));
					ProductionSN pro_sn_SN = snDao.findAllByPsid(12).get(0);
					pro_sn_SN.setPsvalue(sn_list.getString("sn_000"));
					snDao.save(pro_sn_SN);
					snDao.save(pro_sn_YYMM);

					JSONArray sn_lists = sn_list.getJSONArray("sn_list");

					// 工作站資訊
					JSONObject json_work = new JSONObject();
					ArrayList<WorkstationProgram> programs = programDao.findAllByWpgidAndSysheaderOrderBySyssortAsc(data.getInt("ph_wp_id"), false);
					for (WorkstationProgram p_one : programs) {
						ArrayList<Workstation> works = workDao.findAllByWgidAndSysheaderOrderBySyssortAsc(p_one.getWpwgid(), true);
						JSONObject json_one = new JSONObject();
						json_one.put("name", works.get(0).getWpbname());
						json_one.put("type", "N");
						json_work.put(works.get(0).getWid() + "", json_one);
					}

					sn_lists.forEach(s -> {
						// body
						ProductionBody pro_b = new ProductionBody();
						pro_b.setSysver(0);
						pro_b.setPbgid(id_b_g);
						pro_b.setSysheader(false);
						pro_b.setPbsn(s.toString());
						pro_b.setPbcheck(false);
						pro_b.setPbusefulsn(0);
						pro_b.setPbwyears(data.getInt("pr_w_years"));
						pro_b.setSysstatus(data.getInt("sys_status"));
						pro_b.setSyssort(data.getInt("sys_sort"));
						pro_b.setPblpath("");
						pro_b.setPblsize("");
						pro_b.setPbltext("");
						pro_b.setPbschedule(json_work.toString());
						pro_b.setSysmuser(user.getSuaccount());
						pro_b.setSyscuser(user.getSuaccount());
						productionBodyDao.save(pro_b);
					});

					// 規格
					pro_r.setPrid(data.getString("ph_pr_id"));
					pro_r.setPrbomid(data.getString("pr_bom_id"));
					pro_r.setPrcfrom("MES");
					pro_r.setPrcname(data.getString("pr_c_name"));
					pro_r.setProrderid(data.getString("pr_order_id"));
					pro_r.setPrpmodel(data.getString("pr_p_model"));
					pro_r.setPrbitem("");
					pro_r.setPrsitem("");
					pro_r.setPrpquantity(data.getInt("pr_p_quantity"));
					pro_r.setPrpokquantity(data.getInt("pr_p_ok_quantity"));
					pro_r.setPrwyears(data.getInt("pr_w_years"));
					pro_r.setPrssn(sn_lists.get(0).toString());
					pro_r.setPresn(sn_lists.get(sn_lists.length() - 1).toString());
					pro_r.setSysmuser(user.getSuaccount());
					pro_r.setSyscuser(user.getSuaccount());

					// productionRecordsDao.save(pro_r);

					// header
					pro_h = new ProductionHeader();
					// pro_h.setPhid(id_h + 1);
					pro_h.setPhwpid(data.getInt("ph_wp_id"));
					pro_h.setProductionRecords(pro_r);
					pro_h.setPhschedule(data.getInt("pr_p_ok_quantity") + "／" + data.getInt("pr_p_quantity"));
					pro_h.setSysheader(true);
					pro_h.setSysnote(data.getString("sys_note"));
					pro_h.setSyssort(data.getInt("sys_sort"));
					pro_h.setSysver(0);
					pro_h.setSysstatus(data.getInt("sys_status"));
					pro_h.setSysmuser(user.getSuaccount());
					pro_h.setSyscuser(user.getSuaccount());
					pro_h.setPhtype(data.getString("ph_type"));
					pro_h.setPhpbgid(id_b_g);
					productionHeaderDao.save(pro_h);

				} else {
					return check;
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
			for (Object one : list) {
				// 物件轉換
				ProductionHeader pro_h = new ProductionHeader();
				// ProductionBody pro_b = new ProductionBody();
				ProductionRecords pro_r = new ProductionRecords();
				ArrayList<ProductionSN> pro_sn = new ArrayList<ProductionSN>();
				JSONObject data = (JSONObject) one;

				// 查詢重複
				ProductionRecords search = new ProductionRecords();
				search.setPrid(data.getString("ph_pr_id"));
				List<ProductionHeader> headers = productionHeaderDao.findAllByProductionRecords(search);

				if (headers.size() > 0) {
					pro_h = headers.get(0);
				}
				// 無重複->新建
				if (headers.size() < 1) {
					// header
					// int id_h = productionHeaderDao.getProductionHeaderSeq();
					// int id_b = productionBodyDao.getProductionBodySeq();
					int id_b_g = productionBodyDao.getProductionBodyGSeq();

					// sn
					pro_sn = new ArrayList<ProductionSN>();
					ProductionSN pro_sn_one = new ProductionSN();
					pro_sn_one.setPsvalue(data.getString("ps_sn_1"));
					pro_sn_one.setPsgid(1);
					pro_sn.add(pro_sn_one);

					pro_sn_one = new ProductionSN();
					pro_sn_one.setPsvalue(data.getString("ps_sn_2"));
					pro_sn_one.setPsgid(2);
					pro_sn.add(pro_sn_one);

					pro_sn_one = new ProductionSN();
					pro_sn_one.setPsvalue(data.getString("ps_sn_3"));
					pro_sn_one.setPsgid(3);
					pro_sn.add(pro_sn_one);

					pro_sn_one = new ProductionSN();
					pro_sn_one.setPsvalue(data.getString("ps_sn_4"));
					pro_sn_one.setPsgid(4);
					pro_sn_one.setPsname("[YYWW]");
					pro_sn.add(pro_sn_one);

					pro_sn_one = new ProductionSN();
					pro_sn_one.setPsvalue(data.getString("ps_sn_5"));
					pro_sn_one.setPsgid(5);
					pro_sn.add(pro_sn_one);

					pro_sn_one = new ProductionSN();
					pro_sn_one.setPsvalue(data.getString("ps_sn_6"));
					pro_sn_one.setPsgid(6);
					pro_sn_one.setPsname("[000]");
					pro_sn.add(pro_sn_one);
					JSONObject sn_list = Fm_SN.analyze_Sn(pro_sn, true, data.getInt("pr_p_quantity"));

					// 更新SN區段
					ProductionSN pro_sn_YYMM = snDao.findAllByPsid(8).get(0);
					pro_sn_YYMM.setPsvalue(sn_list.getString("sn_YYWW"));
					ProductionSN pro_sn_SN = snDao.findAllByPsid(12).get(0);
					pro_sn_SN.setPsvalue(sn_list.getString("sn_000"));
					snDao.save(pro_sn_SN);
					snDao.save(pro_sn_YYMM);

					JSONArray sn_lists = sn_list.getJSONArray("sn_list");

					// 工作站資訊
					JSONObject json_work = new JSONObject();
					ArrayList<WorkstationProgram> programs = programDao.findAllByWpgidAndSysheaderOrderBySyssortAsc(data.getInt("ph_wp_id"), false);
					for (WorkstationProgram p_one : programs) {
						ArrayList<Workstation> works = workDao.findAllByWgidAndSysheaderOrderBySyssortAsc(p_one.getWpwgid(), true);
						JSONObject json_one = new JSONObject();
						json_one.put("name", works.get(0).getWpbname());
						json_one.put("type", "N");
						json_work.put(works.get(0).getWid() + "", json_one);
					}

					sn_lists.forEach(s -> {
						// body
						ProductionBody pro_b = new ProductionBody();
						pro_b.setSysver(0);
						pro_b.setPbgid(id_b_g);
						pro_b.setSysheader(false);
						pro_b.setPbsn(s.toString());
						pro_b.setPbcheck(false);
						pro_b.setPbusefulsn(0);
						pro_b.setPbwyears(data.getInt("pr_w_years"));
						pro_b.setSysstatus(data.getInt("sys_status"));
						pro_b.setSyssort(data.getInt("sys_sort"));
						pro_b.setPblpath("");
						pro_b.setPblsize("");
						pro_b.setPbltext("");
						pro_b.setPbschedule(json_work.toString());
						pro_b.setSysmuser(user.getSuaccount());
						pro_b.setSyscuser(user.getSuaccount());
						productionBodyDao.save(pro_b);
					});

					// 規格
					pro_r.setPrid(data.getString("ph_pr_id"));
					pro_r.setPrbomid(data.getString("pr_bom_id"));
					pro_r.setPrcfrom("MES");
					pro_r.setPrcname(data.getString("pr_c_name"));
					pro_r.setProrderid(data.getString("pr_order_id"));
					pro_r.setPrpmodel(data.getString("pr_p_model"));
					pro_r.setPrbitem("");
					pro_r.setPrsitem("");
					pro_r.setPrpquantity(data.getInt("pr_p_quantity"));
					pro_r.setPrpokquantity(data.getInt("pr_p_ok_quantity"));
					pro_r.setPrwyears(data.getInt("pr_w_years"));
					pro_r.setPrssn(sn_lists.get(0).toString());
					pro_r.setPresn(sn_lists.get(sn_lists.length() - 1).toString());
					pro_r.setSysmuser(user.getSuaccount());
					pro_r.setSyscuser(user.getSuaccount());

					// productionRecordsDao.save(pro_r);

					// header
					pro_h = new ProductionHeader();
					// pro_h.setPhid(id_h + 1);
					pro_h.setPhwpid(data.getInt("ph_wp_id"));
					pro_h.setProductionRecords(pro_r);
					pro_h.setPhschedule(data.getInt("pr_p_ok_quantity") + "／" + data.getInt("pr_p_quantity"));
					pro_h.setSysheader(true);
					pro_h.setSysnote(data.getString("sys_note"));
					pro_h.setSyssort(data.getInt("sys_sort"));
					pro_h.setSysver(0);
					pro_h.setSysstatus(0);
					pro_h.setSysmuser(user.getSuaccount());
					pro_h.setSyscuser(user.getSuaccount());
					pro_h.setPhtype(data.getString("ph_type"));
					pro_h.setPhpbgid(id_b_g);
					productionHeaderDao.save(pro_h);

				} else {
					return check;
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
			ArrayList<ProductionSN> pro_sn = new ArrayList<ProductionSN>();
			for (Object one : list) {
				// 物件轉換
				ProductionHeader pro_h = new ProductionHeader();
				JSONObject data = (JSONObject) one;

				// 查詢重複
				ProductionRecords search = new ProductionRecords();
				search.setPrid(data.getString("ph_pr_id"));
				List<ProductionHeader> headers = productionHeaderDao.findAllByProductionRecords(search);

				// 查詢資料
				if (headers.size() != 1) {
					return check;
				}
				// sn
				pro_sn = new ArrayList<ProductionSN>();
				ProductionSN pro_sn_one = new ProductionSN();
				pro_sn_one.setPsvalue(data.getString("ps_sn_1"));
				pro_sn_one.setPsgid(1);
				pro_sn.add(pro_sn_one);

				pro_sn_one = new ProductionSN();
				pro_sn_one.setPsvalue(data.getString("ps_sn_2"));
				pro_sn_one.setPsgid(2);
				pro_sn.add(pro_sn_one);

				pro_sn_one = new ProductionSN();
				pro_sn_one.setPsvalue(data.getString("ps_sn_3"));
				pro_sn_one.setPsgid(3);
				pro_sn.add(pro_sn_one);

				pro_sn_one = new ProductionSN();
				pro_sn_one.setPsvalue(data.getString("ps_sn_4"));
				pro_sn_one.setPsgid(4);
				pro_sn_one.setPsname("[YYWW]");
				pro_sn.add(pro_sn_one);

				pro_sn_one = new ProductionSN();
				pro_sn_one.setPsvalue(data.getString("ps_sn_5"));
				pro_sn_one.setPsgid(5);
				pro_sn.add(pro_sn_one);

				pro_sn_one = new ProductionSN();
				pro_sn_one.setPsvalue(data.getString("ps_sn_6"));
				pro_sn_one.setPsgid(6);
				pro_sn_one.setPsname("[000]");
				pro_sn.add(pro_sn_one);
				JSONObject sn_list = Fm_SN.analyze_Sn(pro_sn, false, data.getInt("pr_p_quantity"));

				// 更新SN區段
				ProductionSN pro_sn_YYMM = snDao.findAllByPsid(8).get(0);
				pro_sn_YYMM.setPsvalue(sn_list.getString("sn_YYWW"));
				ProductionSN pro_sn_SN = snDao.findAllByPsid(12).get(0);
				pro_sn_SN.setPsvalue(sn_list.getString("sn_000"));
				snDao.save(pro_sn_SN);
				snDao.save(pro_sn_YYMM);

				JSONArray sn_lists = sn_list.getJSONArray("sn_list");

				// 工作站資訊
				JSONObject json_work = new JSONObject();
				ArrayList<WorkstationProgram> programs = programDao.findAllByWpgidAndSysheaderOrderBySyssortAsc(data.getInt("ph_wp_id"), false);
				for (WorkstationProgram p_one : programs) {
					ArrayList<Workstation> works = workDao.findAllByWgidAndSysheaderOrderBySyssortAsc(p_one.getWpwgid(), true);
					JSONObject json_one = new JSONObject();
					json_one.put("name", works.get(0).getWpbname());
					json_one.put("type", "N");
					json_work.put(works.get(0).getWid() + "", json_one);

				}

				// body
				int id_b_g = data.getInt("ph_pb_g_id");
				List<ProductionBody> pro_bs = productionBodyDao.findAllByPbgidOrderByPbsnAsc(id_b_g);
				int i = 0;
				for (ProductionBody pro_b : pro_bs) {
					pro_b.setSysver(0);
					pro_b.setPbgid(id_b_g);
					pro_b.setSysheader(false);
					pro_b.setPbsn(sn_lists.get(i).toString());
					pro_b.setPbusefulsn(0);
					pro_b.setPbwyears(data.getInt("pr_w_years"));
					pro_b.setSysstatus(data.getInt("sys_status"));
					pro_b.setSyssort(data.getInt("sys_sort"));
					pro_b.setPbschedule(json_work.toString());
					pro_b.setSysmuser(user.getSuaccount());
					productionBodyDao.save(pro_b);
					i += 1;
				}

				// 規格
				pro_h = productionHeaderDao.findAllByPhid(data.getInt("ph_id")).get(0);
				ProductionRecords pro_r = pro_h.getProductionRecords();
				pro_r.setPrid(data.getString("ph_pr_id"));
				pro_r.setPrbomid(data.getString("pr_bom_id"));
				pro_r.setPrcfrom("MES");
				pro_r.setPrcname(data.getString("pr_c_name"));
				pro_r.setPrssn(data.getString("pr_s_sn"));
				pro_r.setPresn(data.getString("pr_e_sn"));
				pro_r.setPrwyears(data.getInt("pr_w_years"));
				pro_r.setProrderid(data.getString("pr_order_id"));
				pro_r.setPrpmodel(data.getString("pr_p_model"));
				pro_r.setPrbomid(data.getString("pr_bom_id"));
				pro_r.setPrpquantity(data.getInt("pr_p_quantity"));
				pro_r.setPrpokquantity(data.getInt("pr_p_ok_quantity"));
				// pro_r.setPrbitem("");
				// pro_r.setPrsitem("");
				pro_r.setSysmuser(user.getSuaccount());
				pro_h.setSysmdate(new Date());

				// header
				pro_h.setPhtype(data.getString("ph_type"));
				pro_h.setPhwpid(data.getInt("ph_wp_id"));
				pro_h.setProductionRecords(pro_r);
				pro_h.setPhschedule(data.getInt("pr_p_ok_quantity") + "／" + data.getInt("pr_p_quantity"));
				pro_h.setSysheader(true);
				pro_h.setSysnote(data.getString("sys_note"));
				pro_h.setSyssort(data.getInt("sys_sort"));
				pro_h.setSysver(0);
				// 數量不為0 不可改回/待命狀態[0]
				if (data.getInt("sys_status") == 0) {
					if (data.getInt("pr_p_ok_quantity") == 0) {
						pro_h.setSysstatus(data.getInt("sys_status"));
					}
				} else {
					pro_h.setSysstatus(data.getInt("sys_status"));
				}
				pro_h.setSysmuser(user.getSuaccount());
				pro_h.setSysmdate(new Date());
				pro_h.setPhpbgid(data.getInt("ph_pb_g_id"));
				productionHeaderDao.save(pro_h);

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
				JSONObject data = (JSONObject) one;
				ProductionHeader pheader = new ProductionHeader();
				pheader.setPhid(data.getInt("ph_id"));
				// productionBodyDao.deleteByProductionHeader(pheader);
				productionHeaderDao.deleteByPhidAndSysheader(data.getInt("ph_id"), true);
				check = true;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return check;
	}
}
