package dtri.com.tw.service;

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
import dtri.com.tw.db.entity.ProductionHeader;
import dtri.com.tw.db.entity.ProductionRecords;
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.db.pgsql.dao.ProductionHeaderDao;
import dtri.com.tw.db.pgsql.dao.ProductionRecordsDao;
import dtri.com.tw.tools.Fm_Time;

@Service
public class ProductionRecordsService {
	@Autowired
	private ProductionRecordsDao recordsDao;
	@Autowired
	private ProductionHeaderDao headerDao;

	// 取得當前 資料清單
	public PackageBean getData(JSONObject body, int page, int p_size) {
		PackageBean bean = new PackageBean();
		ArrayList<ProductionRecords> productionRecords = new ArrayList<ProductionRecords>();

		// 查詢的頁數，page=從0起算/size=查詢的每頁筆數
		if (p_size < 1) {
			page = 0;
			p_size = 100;
		}
		PageRequest pageable = PageRequest.of(page, p_size, Sort.by("prid").descending());
		String prcname = null;
		String prorderid = null;
		String prssn = null;
		String prbomid = null;
		String sysstatus = "0";
		// 初次載入需要標頭 / 之後就不用
		if (body == null || body.isNull("search")) {
			// 放入包裝(header) [01 是排序][_h__ 是分割直][資料庫欄位名稱]
			JSONObject object_header = new JSONObject();
			int ord = 0;
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_id", FFS.h_t("PR_工單序號ID", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_order_id", FFS.h_t("PR_訂單編號", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_c_name", FFS.h_t("PR_客戶名稱", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_p_quantity", FFS.h_t("PR_生產數量", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_p_model", FFS.h_t("PR_產品型號", "150px", FFS.SHO));

			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_bom_id", FFS.h_t("PR_BOM料號", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_c_from", FFS.h_t("PR_單據來源", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_b_item", FFS.h_t("PR_規格定義", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_s_item", FFS.h_t("PR_軟體定義", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_s_sn", FFS.h_t("PR_產品SN_開始", "150px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "pr_e_sn", FFS.h_t("PR_產品SN_結束", "150px", FFS.SHO));

			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_c_date", FFS.h_t("PR_建立時間", "180px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_c_user", FFS.h_t("PR_建立人", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_m_date", FFS.h_t("PR_修改時間", "180px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_m_user", FFS.h_t("PR_修改人", "100px", FFS.SHO));

			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_note", FFS.h_t("PR_備註", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_sort", FFS.h_t("PR_排序", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_ver", FFS.h_t("PR_版本", "100px", FFS.SHO));
			object_header.put(FFS.ord((ord += 1), FFS.H) + "sys_status", FFS.h_t("PR_狀態", "100px", FFS.SHO));
			bean.setHeader(object_header);

			// 放入修改 [(key)](modify/Create/Delete) 格式
			JSONArray obj_m = new JSONArray();
			JSONArray values = new JSONArray();

			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, new JSONArray(), "pr_id", "PR_工單序號ID"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", false, new JSONArray(), "pr_order_id", "PR_訂單編號"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, new JSONArray(), "pr_c_name", "PR_客戶名稱"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, new JSONArray(), "pr_p_quantity", "PR_生產數量"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, new JSONArray(), "pr_p_model", "PR_產品型號"));

			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, new JSONArray(), "pr_bom_id", "PR_BOM料號"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, new JSONArray(), "pr_c_from", "PR_單據來源"));
			obj_m.put(FFS.h_m(FFS.TTA, FFS.TEXT, "", "", FFS.SHO, "col-md-6", true, new JSONArray(), "pr_b_item", "PR_規格定義"));
			obj_m.put(FFS.h_m(FFS.TTA, FFS.TEXT, "", "", FFS.SHO, "col-md-6", true, new JSONArray(), "pr_s_item", "PR_軟體定義"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, new JSONArray(), "pr_s_sn", "PR_產品SN_開始"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.SHO, "col-md-2", true, new JSONArray(), "pr_e_sn", "PR_產品SN_結束"));

			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, new JSONArray(), "sys_c_date", "PR_建立時間"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, new JSONArray(), "sys_c_user", "PR_建立人"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, new JSONArray(), "sys_m_date", "PR_修改時間"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-2", false, new JSONArray(), "sys_m_user", "PR_修改人"));

			obj_m.put(FFS.h_m(FFS.TTA, FFS.TEXT, "", "", FFS.SHO, "col-md-12", false, new JSONArray(), "sys_note", "PR_備註"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, "0", "0", FFS.SHO, "col-md-2", true, new JSONArray(), "sys_sort", "PR_排序"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.NUMB, "", "", FFS.DIS, "col-md-2", false, new JSONArray(), "sys_ver", "PR_版本"));

			values = new JSONArray();
			values.put((new JSONObject()).put("value", "正常").put("key", "0"));
			values.put((new JSONObject()).put("value", "異常").put("key", "1"));
			obj_m.put(FFS.h_m(FFS.SEL, FFS.TEXT, "", "0", FFS.SHO, "col-md-2", true, values, "sys_status", "狀態"));
			bean.setCell_modify(obj_m);

			// 放入包裝(search)
			JSONArray object_searchs = new JSONArray();
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "pr_c_name", "客戶名稱", new JSONArray()));
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "pr_id", "製令工單號", new JSONArray()));

			values = new JSONArray();
			values.put((new JSONObject()).put("value", "正常").put("key", "0"));
			values.put((new JSONObject()).put("value", "異常").put("key", "1"));
			object_searchs.put(FFS.h_s(FFS.SEL, FFS.TEXT, "0", "col-md-2", "sys_status", "狀態", values));
			bean.setCell_searchs(object_searchs);
		} else {
			// 進行-特定查詢
			prcname = body.getJSONObject("search").getString("pr_c_name");
			prcname = prcname.equals("") ? null : prcname;

			prorderid = body.getJSONObject("search").getString("pr_id");
			prorderid = prorderid.equals("") ? null : prorderid;

			prssn = body.getJSONObject("search").getString("pr_s_sn");
			prssn = prorderid.equals("") ? null : prssn;

			prbomid = body.getJSONObject("search").getString("pr_bom_id");
			prbomid = prorderid.equals("") ? null : prbomid;

			sysstatus = body.getJSONObject("search").getString("sys_status");
			sysstatus = sysstatus.equals("") ? "0" : sysstatus;
		}
		productionRecords = recordsDao.findAllByRecords(prcname, prorderid, prbomid, prssn, Integer.parseInt(sysstatus), pageable);

		// 放入包裝(body) [01 是排序][_b__ 是分割直][資料庫欄位名稱]
		JSONArray object_bodys = new JSONArray();
		productionRecords.forEach(one -> {
			JSONObject object_body = new JSONObject();
			int ord = 0;
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_id", one.getPrid());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_order_id", one.getProrderid());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_c_name", one.getPrcname());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_p_quantity", one.getPrpquantity());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_p_model", one.getPrpmodel());

			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_bom_id", one.getPrbomid());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_c_from", one.getPrcfrom());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_b_item", one.getPrbitem());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_s_item", one.getPrsitem());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_s_sn", one.getPrssn());
			object_body.put(FFS.ord((ord += 1), FFS.B) + "pr_e_sn", one.getPresn());

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
				ProductionRecords entity = new ProductionRecords();
				JSONObject data = (JSONObject) one;
				entity.setPrid(data.getString("pr_id"));
				entity.setPrbomid(data.getString("pr_bom_id"));
				entity.setPrcfrom(data.getString("pr_c_from"));
				entity.setPrcname(data.getString("pr_c_name"));
				entity.setPrssn(data.getString("pr_s_sn"));
				entity.setPresn(data.getString("pr_e_sn"));
				entity.setProrderid(data.getString("pr_order_id"));
				entity.setPrpmodel(data.getString("pr_p_model"));
				entity.setPrpquantity(data.getInt("pr_p_quantity"));
				entity.setPrbitem(data.getString("pr_b_item"));
				entity.setPrsitem(data.getString("pr_s_item"));
				entity.setSysmuser(user.getSuname());
				entity.setSyscuser(user.getSuname());

				// 檢查是否有 製令規格
				ArrayList<ProductionRecords> entitys = recordsDao.findAllByPrid(data.getString("pr_id"), PageRequest.of(0, 10));
				// 檢查是否有-製令單 one to one
				ProductionRecords search = new ProductionRecords();
				search.setPrid(data.getString("pr_id"));
				List<ProductionHeader> headers = headerDao.findAllByproductionRecords(search);
				if (entitys.size() < 1 && headers.size() == 1) {
					recordsDao.save(entity);
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

	// 存檔 資料清單
	@Transactional
	public boolean save_asData(JSONObject body, SystemUser user) {
		boolean check = false;
		try {
			JSONArray list = body.getJSONArray("save_as");
			for (Object one : list) {
				// 物件轉換
				ProductionRecords entity = new ProductionRecords();
				JSONObject data = (JSONObject) one;
				entity.setPrid(data.getString("pr_id"));
				entity.setPrbomid(data.getString("pr_bom_id"));
				entity.setPrcfrom(data.getString("pr_c_from"));
				entity.setPrcname(data.getString("pr_c_name"));
				entity.setPrssn(data.getString("pr_s_sn"));
				entity.setPresn(data.getString("pr_e_sn"));
				entity.setProrderid(data.getString("pr_order_id"));
				entity.setPrpmodel(data.getString("pr_p_model"));
				entity.setPrpquantity(data.getInt("pr_p_quantity"));
				entity.setPrbitem(data.getString("pr_b_item"));
				entity.setPrsitem(data.getString("pr_s_item"));
				entity.setSysmuser(user.getSuname());
				entity.setSyscuser(user.getSuname());

				// 檢查是否有 製令規格
				ArrayList<ProductionRecords> entitys = recordsDao.findAllByPrid(data.getString("pr_id"), PageRequest.of(0, 10));
				// 檢查是否有-製令單 one to one
				ProductionRecords search = new ProductionRecords();
				search.setPrid(data.getString("pr_id"));
				List<ProductionHeader> headers = headerDao.findAllByproductionRecords(search);
				if (entitys.size() < 1 && headers.size() == 1) {
					recordsDao.save(entity);
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
				ProductionRecords entity = new ProductionRecords();
				JSONObject data = (JSONObject) one;
				entity.setPrid(data.getString("pr_id"));
				entity.setPrbomid(data.getString("pr_bom_id"));
				entity.setPrcfrom(data.getString("pr_c_from"));
				entity.setPrcname(data.getString("pr_c_name"));
				entity.setPrssn(data.getString("pr_s_sn"));
				entity.setPresn(data.getString("pr_e_sn"));
				entity.setProrderid(data.getString("pr_order_id"));
				entity.setPrpmodel(data.getString("pr_p_model"));
				entity.setPrpquantity(data.getInt("pr_p_quantity"));
				entity.setPrbitem(data.getString("pr_b_item"));
				entity.setPrsitem(data.getString("pr_s_item"));
				entity.setSysmuser(user.getSuname());

				// 檢查是否有 製令規格
				// ArrayList<ProductionRecords> entitys =
				// recordsDao.findAllByPrid(data.getString("pr_id"), PageRequest.of(0, 10));
				// 檢查是否有-製令單 one to one
				ProductionRecords search = new ProductionRecords();
				search.setPrid(data.getString("pr_id"));
				List<ProductionHeader> headers = headerDao.findAllByproductionRecords(search);
				if (headers.size() == 1) {
					recordsDao.save(entity);
				} else {
					return false;
				}
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
				ProductionRecords entity = new ProductionRecords();
				JSONObject data = (JSONObject) one;
				entity.setPrid(data.getString("pr_id"));

				recordsDao.deleteByPridAndSysheader(entity.getPrid(), false);
				check = true;
			}
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return check;
	}
}
