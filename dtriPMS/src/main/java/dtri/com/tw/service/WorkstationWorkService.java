package dtri.com.tw.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.OneToOne;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.tw.bean.PackageBean;
import dtri.com.tw.db.entity.MaintainCode;
import dtri.com.tw.db.entity.ProductionBody;
import dtri.com.tw.db.entity.ProductionHeader;
import dtri.com.tw.db.entity.ProductionRecords;
import dtri.com.tw.db.entity.ProductionSN;
import dtri.com.tw.db.entity.SystemConfig;
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.db.entity.Workstation;
import dtri.com.tw.db.entity.WorkstationProgram;
import dtri.com.tw.db.pgsql.dao.MaintainCodeDao;
import dtri.com.tw.db.pgsql.dao.ProductionBodyDao;
import dtri.com.tw.db.pgsql.dao.ProductionHeaderDao;
import dtri.com.tw.db.pgsql.dao.SystemConfigDao;
import dtri.com.tw.db.pgsql.dao.WorkstationDao;
import dtri.com.tw.db.pgsql.dao.WorkstationProgramDao;
import dtri.com.tw.tools.Fm_Time;

@Service
public class WorkstationWorkService {
	@Autowired
	private ProductionHeaderDao phDao;
	@Autowired
	private ProductionBodyDao pbDao;
	@Autowired
	private WorkstationDao wkDao;
	@Autowired
	private WorkstationProgramDao wkpDao;
	@Autowired
	private MaintainCodeDao codeDao;

	// 取得當前 資料清單
	public PackageBean getData(JSONObject body, int page, int p_size) {
		PackageBean bean = new PackageBean();
		ArrayList<ProductionHeader> pHeaders = new ArrayList<ProductionHeader>();
		ArrayList<ProductionBody> pBodys = new ArrayList<ProductionBody>();
		ArrayList<Workstation> works = new ArrayList<Workstation>();

		// 查詢的頁數，page=從0起算/size=查詢的每頁筆數
		if (p_size < 1) {
			page = 0;
			p_size = 100;
		}
		PageRequest page_r = PageRequest.of(page, p_size, Sort.by("scgid").descending());
		String w_c_name = null;
		String ph_pr_id = null;
		String pb_sn = null;
		// 初次載入需要標頭 / 之後就不用
		if (body == null || body.isNull("search")) {
			// 放入包裝(header) [01 是排序][_h__ 是分割直][資料庫欄位名稱]
			JSONObject object_header = new JSONObject();
			// fix
			ArrayList<MaintainCode> codes = codeDao.findAllByMaintainCode(null, null, 0, PageRequest.of(0, 999));
			JSONObject fix_obj = new JSONObject();
			JSONObject fix_type = new JSONObject();
			String mc_g_code = "", mc_g_name = "";
			JSONObject fix_item = new JSONObject();
			for (MaintainCode one : codes) {
				// TYPE
				if (one.getSysheader()) {
					if (fix_item.length() > 0) {
						fix_type.put("name", mc_g_name);
						fix_type.put("item", fix_item);
						fix_obj.put(mc_g_code, fix_type);
						fix_item = new JSONObject();
						fix_type = new JSONObject();
					}
					mc_g_name = one.getMcgname();
					mc_g_code = one.getMcvalue();
				} else {
					// ITEM
					fix_item.put(one.getMcvalue(), one.getMcname());
				}
			}
			// 補上最後一圈
			if (fix_item.length() > 0) {
				fix_type.put("name", mc_g_name);
				fix_type.put("item", fix_item);
				fix_obj.put(mc_g_code, fix_type);
				fix_item = new JSONObject();
				fix_type = new JSONObject();
			}
			object_header.put("fix_list", fix_obj);
			bean.setHeader(object_header);

			// 放入修改 [(key)](modify/Create/Delete) 格式
			JSONArray obj_m = new JSONArray();
			JSONArray n_val = new JSONArray();
			JSONArray a_val = new JSONArray();

			// doc
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-6", false, n_val, "ph_s_date", "投線日"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-6", false, n_val, "ph_pr_id", "製令單號"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-6", false, n_val, "pr_p_model", "產品型號"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-6", false, n_val, "pr_bom_id", "BOM料號"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-6", false, n_val, "pr_order_id", "訂單編號"));

			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-6", false, n_val, "pr_c_name", "訂購客戶"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-6", false, n_val, "pr_p_quantity", "全部數量"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-6", false, n_val, "sys_status", "狀態"));

			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-6", false, n_val, "pb_l_size", "PLT_Log_Size"));
			obj_m.put(FFS.h_m(FFS.INP, FFS.TEXT, "", "", FFS.DIS, "col-md-12", false, n_val, "pb_l_path", "PLT_Log位置"));
			obj_m.put(FFS.h_m(FFS.TTA, FFS.TEXT, "", "", FFS.DIS, "col-md-12", false, n_val, "pb_l_text", "PLT_Log內容"));

			obj_m.put(FFS.h_m(FFS.TTA, FFS.TEXT, "", "", FFS.DIS, "col-md-12", false, n_val, "pr_b_item", "規格定義"));
			obj_m.put(FFS.h_m(FFS.TTA, FFS.TEXT, "", "", FFS.DIS, "col-md-12", false, n_val, "pr_s_item", "軟體定義"));

			obj_m.put(FFS.h_m(FFS.TTA, FFS.TEXT, "", "", FFS.DIS, "col-md-12", false, n_val, "sys_note", "備註"));
			object_header.put("doc_list", obj_m);
			// bean.setCell_modify(obj_m);

			// 放入包裝(search)
			JSONArray object_searchs = new JSONArray();
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "sc_g_name", "群組名稱", n_val));
			object_searchs.put(FFS.h_s(FFS.INP, FFS.TEXT, "", "col-md-2", "sc_name", "名稱", n_val));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			object_searchs.put(FFS.h_s(FFS.SEL, FFS.TEXT, "0", "col-md-2", "sys_status", "狀態", a_val));
			bean.setCell_searchs(object_searchs);
		} else {

			// 進行-特定檢查
			w_c_name = body.getJSONObject("search").getString("w_c_name");
			w_c_name = w_c_name.equals("") ? null : w_c_name;
			ph_pr_id = body.getJSONObject("search").getString("ph_pr_id");
			ph_pr_id = ph_pr_id.equals("") ? null : ph_pr_id;
			pb_sn = body.getJSONObject("search").getString("pb_sn");
			pb_sn = pb_sn.equals("") ? null : pb_sn;

			// 工作站
			ArrayList<Workstation> w_one = wkDao.findAllByWcname(w_c_name, PageRequest.of(0, 1));
			ArrayList<WorkstationProgram> wp_one = new ArrayList<WorkstationProgram>();
			List<ProductionHeader> ph_one = new ArrayList<ProductionHeader>();
			List<ProductionBody> pb_one = new ArrayList<ProductionBody>();

			ProductionRecords records = new ProductionRecords();
			records.setPrid(ph_pr_id);
			ph_one = phDao.findAllByProductionRecords(records);
			// 比對-檢查製令
			if (ph_one.size() == 1) {
				// 比對-檢查程序+工作站
				wp_one = wkpDao.findAllByWpgidAndWpwgidAndSysheaderOrderBySyssortAsc(ph_one.get(0).getPhwpid(), w_one.get(0).getWgid(), false);
				if (wp_one.size() == 1) {
					// 比對-檢查SN關聯
					pb_one = pbDao.findAllByPbsnAndPbgid(pb_sn, ph_one.get(0).getPhpbgid());
					if (pb_one.size() == 1) {

						// 放入包裝(body) [01 是排序][_b__ 是分割直][資料庫欄位名稱]
						// doc
						ProductionBody pb_one_fn = pb_one.get(0);
						JSONArray object_doc = new JSONArray();
						JSONArray object_sn = new JSONArray();
						JSONObject object_body_all = new JSONObject();
						ph_one.forEach(one -> {
							JSONObject object_body = new JSONObject();
							object_body.put(FFS.M + "ph_s_date", one.getPhsdate() == null ? "" : one.getPhsdate());
							object_body.put(FFS.M + "ph_pr_id", one.getProductionRecords().getPrid());
							object_body.put(FFS.M + "pr_p_model", one.getProductionRecords().getPrpmodel());
							object_body.put(FFS.M + "pr_bom_id", one.getProductionRecords().getPrbomid());
							object_body.put(FFS.M + "pr_order_id", one.getProductionRecords().getProrderid());

							object_body.put(FFS.M + "pr_c_name", one.getProductionRecords().getPrcname());
							object_body.put(FFS.M + "pr_p_quantity", one.getProductionRecords().getPrpquantity());
							object_body.put(FFS.M + "sys_status", one.getSysstatus());

							object_body.put(FFS.M + "pb_l_size", pb_one_fn.getPblsize());
							object_body.put(FFS.M + "pb_l_path", pb_one_fn.getPblpath());
							object_body.put(FFS.M + "pb_l_text", pb_one_fn.getPbltext());

							object_body.put(FFS.M + "pr_b_item", one.getProductionRecords().getPrbitem());
							object_body.put(FFS.M + "pr_s_item", one.getProductionRecords().getPrsitem());
							object_doc.put(object_body);
						});
						object_body_all.put("search", object_doc);

						// SN_list
						w_one = wkDao.findAllByWgidAndSysheaderOrderBySyssortAsc(w_one.get(0).getWgid(), false);
						w_one.forEach(w -> {
							JSONObject object_body = new JSONObject();
							object_body.put(w.getWorkstationItem().getWipbcell(), w.getWorkstationItem().getWipbvalue());
							object_sn.put(object_body);
						});

						object_body_all.put("sn_list", object_sn);
						
						object_body_all.put("workdatation_program_name", wp_one.get(0).getWpname());
						object_body_all.put("workdatation_name",w_one.get(0).getWpbname());
						bean.setBody(object_body_all);
					} else {
						return null;
					}
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
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
				SystemConfig sys_c = new SystemConfig();
				JSONObject data = (JSONObject) one;
				sys_c.setScname(data.getString("sc_name"));
				sys_c.setScgname(data.getString("sc_g_name"));
				sys_c.setScvalue(data.getString("sc_value"));
				sys_c.setSysnote(data.getString("sys_note"));
				sys_c.setSyssort(data.getInt("sys_sort"));
				sys_c.setSysstatus(data.getInt("sys_status"));
				sys_c.setSysmuser(user.getSuname());
				sys_c.setSyscuser(user.getSuname());

				// 檢查群組名稱重複
//				ArrayList<SystemConfig> sys_p_g = configDao.findAllByConfigGroupTop1(sys_c.getScgname(), PageRequest.of(0, 1));
//				if (sys_p_g != null && sys_p_g.size() > 0) {
//					// 重複 則取同樣G_ID
//					sys_c.setScgid(sys_p_g.get(0).getScgid());
//				} else {
//					// 取得最新G_ID
//					sys_c.setScgid(configDao.getSystem_config_g_seq());
//				}
//				configDao.save(sys_c);
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
				SystemConfig sys_c = new SystemConfig();
				JSONObject data = (JSONObject) one;
				sys_c.setScname(data.getString("sc_name"));
				sys_c.setScgname(data.getString("sc_g_name"));
				sys_c.setScvalue(data.getString("sc_value"));
				sys_c.setSysnote(data.getString("sys_note"));
				sys_c.setSyssort(data.getInt("sys_sort"));
				sys_c.setSysstatus(data.getInt("sys_status"));
				sys_c.setSysmuser(user.getSuname());
				sys_c.setSyscuser(user.getSuname());

				// 檢查群組名稱重複
//				ArrayList<SystemConfig> sys_c_g = configDao.findAllByConfigGroupTop1(sys_c.getScgname(), PageRequest.of(0, 1));
//				if (sys_c_g != null && sys_c_g.size() > 0) {
//					// 重複 則取同樣G_ID
//					sys_c.setScgid(sys_c_g.get(0).getScgid());
//				} else {
//					// 取得最新G_ID
//					sys_c.setScgid(configDao.getSystem_config_g_seq());
//				}
//				configDao.save(sys_c);
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
				SystemConfig sys_p = new SystemConfig();
				JSONObject data = (JSONObject) one;
				sys_p.setScid(data.getInt("sc_id"));
				sys_p.setScname(data.getString("sc_name"));
				sys_p.setScgid(data.getInt("sc_g_id"));
				sys_p.setScgname(data.getString("sc_g_name"));
				sys_p.setScvalue(data.getString("sc_value"));
				sys_p.setSysnote(data.getString("sys_note"));
				sys_p.setSyssort(data.getInt("sys_sort"));
				sys_p.setSysstatus(data.getInt("sys_status"));
				sys_p.setSysmuser(user.getSuname());
				sys_p.setSysmdate(new Date());

				// 檢查群組名稱重複
//				ArrayList<SystemConfig> sys_p_g = configDao.findAllByConfigGroupTop1(sys_p.getScgname(), PageRequest.of(0, 1));
//				if (sys_p_g != null && sys_p_g.size() > 0) {
//					// 重複 則取同樣G_ID
//					sys_p.setScgid(sys_p_g.get(0).getScgid());
//				} else {
//					// 取得最新G_ID
//					sys_p.setScgid(configDao.getSystem_config_g_seq());
//				}
//				configDao.save(sys_p);
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
				SystemConfig sys_p = new SystemConfig();
				JSONObject data = (JSONObject) one;
				sys_p.setScid(data.getInt("sc_id"));

				// configDao.deleteByScidAndSysheader(sys_p.getScid(), false);
				check = true;
			}
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return check;
	}
}
