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
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.db.pgsql.dao.ProductionBodyDao;
import dtri.com.tw.db.pgsql.dao.ProductionHeaderDao;
import dtri.com.tw.tools.Fm_Time;

@Service
public class ProductionHeaderService {
	@Autowired
	private ProductionHeaderDao productionHeaderDao;

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
		List<Integer> pbphid = null;

		// 初次載入需要標頭 / 之後就不用
		if (body == null || body.isNull("search")) {
			// 放入包裝(header) [01 是排序][_h__ 是分割直][資料庫欄位名稱]
			JSONObject object_header = new JSONObject();
			int ord = 0;
			// header
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_id", FFS.h_t("TL_ID", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_model", FFS.h_t("TL_產品型號", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_pr_id", FFS.h_t("TL_製令單號", "180px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_wp_id", FFS.h_t("TL_工作站關聯", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_s_date", FFS.h_t("TL_製令開始", "120px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_e_date", FFS.h_t("TL_製令結束", "120px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "ph_schedule", FFS.h_t("TL_進度(X／X)", "130px", FFS.SHO));
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
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-1", false, n_val, "ph_wp_id", "TL_工作站"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", true, n_val, "ph_schedule", "TL_進度"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, n_val, "ph_model", "TL_產品型號"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, n_val, "ph_pr_id", "TL_製令單號"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", true, n_val, "ph_s_date", "TL_開始(時)"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", true, n_val, "ph_e_date", "TL_結束(時)"));
			// sys
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
			JSONArray object_searchs = new JSONArray();
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "ph_model", "TL_產品型號", new JSONArray()));
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "ph_pr_id", "TL_製令單號", new JSONArray()));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			object_searchs.put(FFS.h_s(FFS.SEL, FFS.TEXT, "0", "col-md-1", "sys_status", "TL_狀態", a_val));

			// 項目查詢(選單)
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "pb_sn", "SN_出貨序號", n_val));
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

		// 全查
		productionHeaders = productionHeaderDao.findAllByProductionHeader(phmodel, phprid, Integer.parseInt(sysstatus), pbphid, page_r);

		// 放入包裝(body) [01 是排序][_b__ 是分割直][資料庫欄位名稱]
		JSONArray object_bodys = new JSONArray();
		productionHeaders.forEach(one -> {
			// 有多少BODY(至少一筆)

			for (int j = 0; j < 1; j++) {
				JSONObject object_body = new JSONObject();
				// header
				int ord = 0;
				object_body.put(FFS.ord((ord += 1), FFS.B) + "ph_id", one.getPhid());
				object_body.put(FFS.ord((ord += 1), FFS.B) + "ph_model", one.getPhmodel());
				object_body.put(FFS.ord((ord += 1), FFS.B) + "ph_pr_id", one.getPhprid());

				object_body.put(FFS.ord((ord += 1), FFS.B) + "ph_wp_id", one.getPhwpid());
				object_body.put(FFS.ord((ord += 1), FFS.B) + "ph_s_date", one.getPhsdate() == null ? "" : one.getPhsdate());
				object_body.put(FFS.ord((ord += 1), FFS.B) + "ph_e_date", one.getPhedate() == null ? "" : one.getPhedate());
				object_body.put(FFS.ord((ord += 1), FFS.B) + "ph_schedule", one.getPhschedule());
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
			}

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
			if (list.length() < 1) {
				return check;
			}
			for (Object one : list) {
				// 物件轉換
				ProductionHeader pro_h = new ProductionHeader();
				ProductionBody pro_b = new ProductionBody();
				JSONObject data = (JSONObject) one;
				// header
				int id_h = productionHeaderDao.getProductionHeaderSeq();
				int id_b = productionBodyDao.getProductionBodySeq();
				// 查詢重複
				List<ProductionHeader> headers = productionHeaderDao.findAllByPhprid(data.getString("ph_pr_id"));

				if (headers.size() > 0) {
					pro_h = headers.get(0);
				}
				// 無重複->新建
				if (headers.size() < 1) {
					pro_h = new ProductionHeader();
					pro_h.setPhid(id_h + 1);
					pro_h.setPhmodel(data.getString("ph_model"));
					pro_h.setPhprid(data.getString("ph_pr_id"));
					pro_h.setPhwpid(0);
					pro_h.setPhschedule("0／0");
					pro_h.setSysheader(true);
					pro_h.setSysnote(data.getString("sys_note"));
					pro_h.setSyssort(data.getInt("sys_sort"));
					pro_h.setSysver(0);
					pro_h.setSysstatus(data.getInt("sys_status"));
					pro_h.setSysmuser(user.getSuname());
					pro_h.setSyscuser(user.getSuname());
					productionHeaderDao.save(pro_h);

					// body
					// 父(群組代表)
					id_b += 1;
					pro_b.setPbid(id_b);
					pro_b.setProductionHeader(pro_h);
					pro_b.setSysver(0);
					pro_b.setPbsn("SN_Group");
					pro_b.setPbschedule("");
					pro_b.setSysheader(true);
					pro_b.setSysmuser(user.getSuname());
					pro_b.setSyscuser(user.getSuname());
					productionBodyDao.save(pro_b);
					// 子
					pro_b = new ProductionBody();
					pro_b.setPbid(id_b + 1);
					pro_b.setSysheader(false);
					pro_b.setProductionHeader(pro_h);
					pro_b.setPbsn("");
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
			if (list.length() < 1) {
				return check;
			}
			for (Object one : list) {
				// 物件轉換
				ProductionHeader pro_h = new ProductionHeader();
				ProductionBody pro_b = new ProductionBody();
				JSONObject data = (JSONObject) one;
				// header
				// 查詢重複
				List<ProductionHeader> headers = productionHeaderDao.findAllByPhprid(data.getString("ph_pr_id"));
				if (headers.size() > 0) {
					pro_h = headers.get(0);
				}
				int id_b = productionBodyDao.getProductionBodySeq();
				int id_h = productionHeaderDao.getProductionHeaderSeq();
				// 無重複->新建
				if (headers.size() < 1) {
					pro_h = new ProductionHeader();
					pro_h.setPhid(id_h + 1);
					pro_h.setPhmodel(data.getString("ph_model"));
					pro_h.setPhprid(data.getString("ph_pr_id"));
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
					productionHeaderDao.save(pro_h);

					// 父(群組代表)
					id_b += 1;
					pro_b.setPbid(id_b);
					pro_b.setProductionHeader(pro_h);
					pro_b.setSysver(0);
					pro_b.setPbsn("SN_Group");
					pro_b.setPbschedule("");
					pro_b.setSysheader(true);
					pro_b.setSysmuser(user.getSuname());
					pro_b.setSyscuser(user.getSuname());
					productionBodyDao.save(pro_b);
					// 子
					pro_b = new ProductionBody();
					pro_b.setPbid(id_b + 1);
					pro_b.setSysheader(false);
					pro_b.setProductionHeader(pro_h);
					pro_b.setSysstatus(data.getInt("sys_status"));
					pro_b.setSyssort(data.getInt("sys_sort"));
					pro_b.setSysmuser(user.getSuname());
					pro_b.setSyscuser(user.getSuname());
					pro_b.setPbsn("");
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
				List<ProductionHeader> headers = productionHeaderDao.findAllByPhprid(data.getString("ph_pr_id"));

				if (headers.size() != 1) {
					return check;
				}
				pro_h = new ProductionHeader();
				pro_h.setPhid(data.getInt("ph_id"));
				pro_h.setPhmodel(data.getString("ph_model"));
				pro_h.setPhprid(data.getString("ph_pr_id"));
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
				productionBodyDao.deleteByProductionHeader(pheader);
				productionHeaderDao.deleteByPhidAndSysheader(data.getInt("ph_id"), true);
				check = true;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return check;
	}
}
