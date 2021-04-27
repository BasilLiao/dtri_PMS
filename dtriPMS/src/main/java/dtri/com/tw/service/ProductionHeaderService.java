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
import dtri.com.tw.db.pgsql.dao.ProductionRecordsDao;
import dtri.com.tw.tools.Fm_Time;

@Service
public class ProductionHeaderService {
	@Autowired
	private ProductionHeaderDao productionHeaderDao;
	@Autowired
	private ProductionRecordsDao productionRecordsDao;

	@Autowired
	private ProductionBodyDao productionBodyDao;

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
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_model", FFS.h_t("TL_產品型號", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_pr_id", FFS.h_t("TL_製令單號", "180px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_wp_id", FFS.h_t("TL_工作站關聯", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_s_date", FFS.h_t("TL_製令開始", "120px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_e_date", FFS.h_t("TL_製令結束", "120px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_schedule", FFS.h_t("TL_進度(X／X)", "130px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_type", FFS.h_t("TL_製令類型", "150px", FFS.SHO));
			// 規格
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_order_id", FFS.h_t("PR_訂單編號", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_c_name", FFS.h_t("PR_客戶名稱", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_bom_id", FFS.h_t("PR_BOM料號", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_c_from", FFS.h_t("PR_單據來源", "150px", FFS.SHO));

			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_b_item", FFS.h_t("PR_規格定義", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_s_item", FFS.h_t("PR_軟體定義", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_p_quantity", FFS.h_t("PR_生產數量", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_p_ok_quantity", FFS.h_t("PR_完成數量", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_s_sn", FFS.h_t("PR_產品SN_開始", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_e_sn", FFS.h_t("PR_產品SN_結束", "150px", FFS.SHO));
			
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
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-1", false, n_val, "ph_wp_id", "TL_工作站"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-1", true, n_val, "ph_schedule", "TL_進度"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, n_val, "ph_model", "TL_產品型號"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, n_val, "ph_pr_id", "TL_製令單號"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", true, n_val, "ph_s_date", "TL_開始(時)"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", true, n_val, "ph_e_date", "TL_結束(時)"));
			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value","一般製令" ).put("key", "A511"));
			a_val.put((new JSONObject()).put("value","重工製令" ).put("key", "A521"));
			a_val.put((new JSONObject()).put("value","維護製令" ).put("key", "A522"));
			a_val.put((new JSONObject()).put("value","拆解製令" ).put("key", "A431"));
			a_val.put((new JSONObject()).put("value","委外製令" ).put("key", "A512"));
			obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "A511", "A511", FFS.SHO, "col-md-2", true, a_val, "ph_type", "TL_製令類型"));
			// 規格-ProductionRecords
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, n_val, "pr_order_id", "PR_訂單編號"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, n_val, "pr_c_name", "PR_客戶名稱"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "13碼", "", FFS.SHO, "col-md-2", false, n_val, "pr_s_sn", "PR_產品SN_開始"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "13碼", "", FFS.SHO, "col-md-2", false, n_val, "pr_e_sn", "PR_產品SN_結束"));

			obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, "0", "0", FFS.SHO, "col-md-1", true, n_val, "pr_p_quantity", "PR_需生產數"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, "0", "0", FFS.SHO, "col-md-1", true, n_val, "pr_p_ok_quantity", "PR_已完成數"));
			obj_m.put(FFS.h_m(FFS.TTA, FFS.TEXT, "", "", FFS.DIS, "col-md-6", true, n_val, "pr_b_item", "PR_規格定義"));
			obj_m.put(FFS.h_m(FFS.TTA, FFS.TEXT, "", "", FFS.DIS, "col-md-6", true, n_val, "pr_s_item", "PR_軟體定義"));
			// sys
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, n_val, "pr_bom_id", "PR_BOM料號"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, n_val, "pr_c_from", "PR_單據來源"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, n_val, "sys_c_date", "TL_建立時間"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, n_val, "sys_c_user", "TL_建立人"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, n_val, "sys_m_date", "TL_修改時間"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, n_val, "sys_m_user", "TL_修改人"));

			obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, "", "", FFS.DIS, "col-md-1", false, n_val, "sys_ver", "TL_版本"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, "0", "0", FFS.SHO, "col-md-1", true, n_val, "sys_sort", "TL_排序"));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "0", "0", FFS.SHO, "col-md-2", true, a_val, "sys_status", "TL_狀態"));
			obj_m.put(FFS.h_m(FFS.TTA, FFS.TEXT, "", "", FFS.SHO, "col-md-12", false, n_val, "sys_note", "TL_備註"));

			bean.setCell_modify(obj_m);

			// 放入包裝(search)
			// 製令查詢
			JSONArray object_searchs = new JSONArray();
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "ph_model", "TL_產品型號", n_val));
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "ph_pr_id", "TL_製令單號", n_val));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			object_searchs.put(FFS.h_s(FFS.SEL, FFS.TEXT, "0", "col-md-2", "sys_status", "TL_狀態", a_val));
			// 規格查詢
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
		productionHeaders.forEach(one -> {
			JSONObject object_body = new JSONObject();
			// header
			int ord = 0;
			object_body.put(FFS.ord((ord += 1), FFS.B) + "ph_id", one.getPhid());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "ph_pb_g_id", one.getPhpbgid());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "ph_model", one.getPhmodel());
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
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_b_item", one.getProductionRecords().getPrbitem());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_s_item", one.getProductionRecords().getPrsitem());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_p_quantity", one.getProductionRecords().getPrpquantity());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_p_ok_quantity", one.getProductionRecords().getPrpokquantity());

			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_s_sn", one.getProductionRecords().getPrssn());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_e_sn", one.getProductionRecords().getPresn());

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
				ProductionHeader pro_h = new ProductionHeader();
				ProductionBody pro_b = new ProductionBody();
				ProductionRecords pro_r = new ProductionRecords();
				JSONObject data = (JSONObject) one;
				// header
				int id_h = productionHeaderDao.getProductionHeaderSeq();
				int id_b = productionBodyDao.getProductionBodySeq();
				int id_b_g =productionBodyDao.getProductionBodyGSeq();
				// 查詢重複
				ProductionRecords search = new ProductionRecords();
				search.setPrid(data.getString("ph_pr_id"));
				List<ProductionHeader> headers = productionHeaderDao.findAllByproductionRecords(search);

				if (headers.size() > 0) {
					pro_h = headers.get(0);
				}
				// 無重複->新建
				if (headers.size() < 1) {
					// 規格
					pro_r.setPrid(data.getString("ph_pr_id"));
					pro_r.setPrbomid(data.getString("pr_bom_id"));
					pro_r.setPrcfrom("MES");
					pro_r.setPrcname(data.getString("pr_c_name"));
					pro_r.setPrssn(data.getString("pr_s_sn"));
					pro_r.setPresn(data.getString("pr_e_sn"));
					pro_r.setProrderid(data.getString("pr_order_id"));
					pro_r.setPrpmodel(data.getString("ph_model"));
					pro_r.setPrbomid(data.getString("pr_bom_id"));
					pro_r.setPrpquantity(data.getInt("pr_p_quantity"));
					pro_r.setPrpokquantity(data.getInt("pr_p_ok_quantity"));
					pro_r.setPrbitem("");
					pro_r.setPrsitem("");
					pro_r.setSysmuser(user.getSuname());
					pro_r.setSyscuser(user.getSuname());
					// productionRecordsDao.save(pro_r);

					// header
					pro_h = new ProductionHeader();
					pro_h.setPhid(id_h + 1);
					pro_h.setPhmodel(data.getString("ph_model"));
					pro_h.setPhtype(data.getString("ph_type"));
					pro_h.setProductionRecords(pro_r);
					pro_h.setPhwpid(0);
					pro_h.setPhschedule(data.getInt("pr_p_ok_quantity") + "／" + data.getInt("pr_p_quantity"));
					pro_h.setSysheader(true);
					pro_h.setSysnote(data.getString("sys_note"));
					pro_h.setSyssort(data.getInt("sys_sort"));
					pro_h.setSysver(0);
					pro_h.setSysstatus(data.getInt("sys_status"));
					pro_h.setSysmuser(user.getSuname());
					pro_h.setSyscuser(user.getSuname());
					pro_h.setPhpbgid(id_b_g);
					productionHeaderDao.save(pro_h);

					// body
					pro_b = new ProductionBody();
					pro_b.setPbschedule("");
					pro_b.setSysver(0);
					pro_b.setPbid(id_b + 1);
					pro_b.setPbgid(id_b_g);
					pro_b.setSysheader(false);
					pro_b.setPbsn("");
					pro_b.setPbcheck(false);
					pro_b.setPbusefulsn(0);
					pro_b.setSysstatus(data.getInt("sys_status"));
					pro_b.setSyssort(data.getInt("sys_sort"));
					pro_b.setSysmuser(user.getSuname());
					pro_b.setSyscuser(user.getSuname());
					productionBodyDao.save(pro_b);

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
				ProductionBody pro_b = new ProductionBody();
				JSONObject data = (JSONObject) one;
				// header
				// 查詢重複
				ProductionRecords search = new ProductionRecords();
				search.setPrid(data.getString("ph_pr_id"));
				List<ProductionHeader> headers = productionHeaderDao.findAllByproductionRecords(search);
				if (headers.size() > 0) {
					pro_h = headers.get(0);
				}
				int id_b = productionBodyDao.getProductionBodySeq();
				int id_h = productionHeaderDao.getProductionHeaderSeq();
				// 無重複->新建
				if (headers.size() < 1) {
					pro_h = new ProductionHeader();
					ProductionRecords pro_r = new ProductionRecords();
					pro_r.setPrid(data.getString("ph_pr_id"));
					pro_h.setPhid(id_h + 1);
					pro_h.setPhmodel(data.getString("ph_model"));
					pro_h.setProductionRecords(pro_r);
					pro_h.setPhwpid(0);
					pro_h.setPhschedule("");
					pro_h.setSysheader(true);
					pro_h.setSysnote(data.getString("sys_note"));
					pro_h.setSyssort(data.getInt("sys_sort"));
					pro_h.setSysver(0);
					pro_h.setSysstatus(data.getInt("sys_status"));
					pro_h.setSysmuser(user.getSuname());
					pro_h.setSyscuser(user.getSuname());

					// body
					pro_b = new ProductionBody();
					pro_b.setPbgid(pro_h.getPhid());
					pro_b.setPbschedule("");
					pro_b.setSysver(0);
					pro_b.setPbid(id_b + 1);
					pro_b.setSysheader(false);
					pro_b.setPbsn("");
					pro_b.setPbcheck(false);
					pro_b.setSysstatus(data.getInt("sys_status"));
					pro_b.setSyssort(data.getInt("sys_sort"));
					pro_b.setSysmuser(user.getSuname());
					pro_b.setSyscuser(user.getSuname());
					productionBodyDao.save(pro_b);
				} else {
					return false;
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
			for (Object one : list) {
				// 物件轉換
				ProductionHeader pro_h = new ProductionHeader();
				JSONObject data = (JSONObject) one;
				// header
				// 查詢重複
				ProductionRecords search = new ProductionRecords();
				search.setPrid(data.getString("ph_pr_id"));
				List<ProductionHeader> headers = productionHeaderDao.findAllByproductionRecords(search);

				if (headers.size() != 1) {
					return check;
				}
				pro_h = new ProductionHeader();
				ProductionRecords pro_r = new ProductionRecords();
				pro_r.setPrid(data.getString("ph_pr_id"));
				pro_h = new ProductionHeader();
				pro_h.setPhid(data.getInt("ph_id"));
				pro_h.setPhmodel(data.getString("ph_model"));
				pro_h.setProductionRecords(pro_r);
				pro_h.setPhwpid(data.getInt("ph_wp_id"));
				pro_h.setPhschedule(data.getString("ph_schedule"));
				pro_h.setSysheader(true);
				pro_h.setSysnote(data.getString("sys_note"));
				pro_h.setSyssort(data.getInt("sys_sort"));
				pro_h.setSysver(data.getInt("sys_ver"));
				pro_h.setSysstatus(data.getInt("sys_status"));
				pro_h.setSysmuser(user.getSuname());
				pro_h.setSysmdate(new Date());
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
