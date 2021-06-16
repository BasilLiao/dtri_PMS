package dtri.com.tw.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.tw.bean.PackageBean;
import dtri.com.tw.db.entity.MaintainCode;
import dtri.com.tw.db.entity.ProductionBody;
import dtri.com.tw.db.entity.ProductionHeader;
import dtri.com.tw.db.entity.ProductionRecords;
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
import dtri.com.tw.tools.Fm_json;

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
	@Autowired
	private FtpService ftpService;
	@Autowired
	private SystemConfigDao sysDao;

	// 取得當前 資料清單
	public PackageBean getData(JSONObject body, int page, int p_size, SystemUser user) {
		PackageBean bean = new PackageBean();

		// 查詢的頁數，page=從0起算/size=查詢的每頁筆數
		if (p_size < 1) {
			page = 0;
			p_size = 100;
		}
		String w_c_name = null;
		String ph_pr_id = null;
		String pb_sn = null;
		// 初次載入需要標頭 / 之後就不用
		if (body == null || body.isNull("search")) {
			// 放入包裝(header) [01 是排序][_h__ 是分割直][資料庫欄位名稱]
			JSONObject object_header = new JSONObject();
			// fix
			ArrayList<MaintainCode> codes = codeDao.findAllByMaintainCode(null, null, 0, PageRequest.of(0, 9999));
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
			obj_m.put(FFS.h_m(FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.See.DIS, "col-md-6", false, n_val, "ph_s_date", "投線日"));
			obj_m.put(FFS.h_m(FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.See.DIS, "col-md-6", false, n_val, "ph_pr_id", "製令單號"));
			obj_m.put(FFS.h_m(FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.See.DIS, "col-md-6", false, n_val, "pr_p_model", "產品型號"));
			obj_m.put(FFS.h_m(FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.See.DIS, "col-md-6", false, n_val, "pr_bom_id", "BOM料號"));
			obj_m.put(FFS.h_m(FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.See.DIS, "col-md-6", false, n_val, "pr_order_id", "訂單編號"));

			obj_m.put(FFS.h_m(FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.See.DIS, "col-md-6", false, n_val, "pr_c_name", "訂購客戶"));
			obj_m.put(FFS.h_m(FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.See.DIS, "col-md-6", false, n_val, "pr_p_quantity", "全部數量"));
			obj_m.put(FFS.h_m(FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.See.DIS, "col-md-6", false, n_val, "sys_status", "狀態"));

			obj_m.put(FFS.h_m(FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.See.DIS, "col-md-6", false, n_val, "pb_l_size", "PLT_Log_Size"));
			obj_m.put(FFS.h_m(FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.See.DIS, "col-md-12", false, n_val, "pb_l_path", "PLT_Log位置"));
			obj_m.put(FFS.h_m(FFM.Tag.TTA, FFM.Type.TEXT, "", "", FFM.See.DIS, "col-md-12", false, n_val, "pb_l_text", "PLT_Log內容"));

			obj_m.put(FFS.h_m(FFM.Tag.TTA, FFM.Type.TEXT, "", "", FFM.See.DIS, "col-md-12", false, n_val, "pr_b_item", "規格定義"));
			obj_m.put(FFS.h_m(FFM.Tag.TTA, FFM.Type.TEXT, "", "", FFM.See.DIS, "col-md-12", false, n_val, "pr_s_item", "軟體定義"));

			obj_m.put(FFS.h_m(FFM.Tag.TTA, FFM.Type.TEXT, "", "", FFM.See.DIS, "col-md-12", false, n_val, "sys_note", "備註"));
			object_header.put("doc_list", obj_m);
			// bean.setCell_modify(obj_m);

			// 放入包裝(search)
			JSONArray object_searchs = new JSONArray();
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "sc_g_name", "群組名稱", n_val));
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "sc_name", "名稱", n_val));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			object_searchs.put(FFS.h_s(FFM.Tag.SEL, FFM.Type.TEXT, "0", "col-md-2", "sys_status", "狀態", a_val));
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
			ArrayList<WorkstationProgram> wp_all = new ArrayList<WorkstationProgram>();
			List<ProductionHeader> ph_all = new ArrayList<ProductionHeader>();
			List<ProductionBody> pb_all = new ArrayList<ProductionBody>();

			// Step1. FTP檢查[]
			ArrayList<SystemConfig> config = sysDao.findAllByConfig(null, "FTP", 0, PageRequest.of(0, 99));
			JSONObject c_json = new JSONObject();
			config.forEach(c -> {
				c_json.put(c.getScname(), c.getScvalue());
			});
			Integer year = Year.now().getValue();
			String url = c_json.getString("IP"), //
					username = c_json.getString("ACCOUNT"), //
					password = c_json.getString("PASSWORD"), //
					remotePath = c_json.getString("PATH") + year, //
					remotePathBackup = c_json.getString("PATH_BACKUP"), //
					localPath = "";//
			int port = c_json.getInt("PORT");
			String[] searchName = { ph_pr_id, "", pb_sn };
			JSONArray list_log = ftpService.downFile(url, port, username, password, remotePath, remotePathBackup, searchName, localPath,
					user.getSuaccount());
			pb_all = pbDao.findAllByPbsn(pb_sn);

			// Log 更新 資料(檢查是否有LOG+內容是否正確)
			if (list_log != null && list_log.length() > 0 && list_log.getJSONObject(0).getBoolean("check")) {
				JSONObject one = list_log.getJSONObject(0);

				// 檢查所有可能對應的欄位
				Iterator<String> keys = one.keys();
				ProductionBody body_title = pbDao.findAllByPbid(0).get(0);
				ProductionBody body_one = pb_all.get(0);
				while (keys.hasNext()) {
					String cell_key = keys.next();
					// sn關聯表
					int j = 0;
					for (j = 0; j < 50; j++) {
						String get_name = "getPbvalue" + String.format("%02d", j + 1);
						String set_name = "setPbvalue" + String.format("%02d", j + 1);
						try {
							// 取出欄位名稱 ->存入body_title資料
							Method set_method = body_one.getClass().getMethod(set_name, String.class);
							Method get_method = body_title.getClass().getMethod(get_name);
							String name = (String) get_method.invoke(body_title);

							if (name != null && name.equals(cell_key)) {
								set_method.invoke(body_one, one.getString(cell_key));
								break;
							} else if (name == null) {
								break;
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
				}
				body_one.setPbltext(one.getString("pb_l_text"));
				body_one.setPblpath(one.getString("pb_l_path"));
				body_one.setPblsize(one.getInt("pb_l_size") + "");
				pbDao.save(body_one);
			} else {
				// 查無此單
				// return new PackageBean();
			}

			// Step2. 製令+工作站+SN關聯+Doc 檢查
			ProductionRecords records = new ProductionRecords();
			records.setPrid(ph_pr_id);
			ph_all = phDao.findAllByProductionRecords(records);
			// 比對-檢查製令
			if (ph_all.size() == 1) {
				// 比對-檢查程序+工作站
				wp_all = wkpDao.findAllByWpgidAndWpwgidAndSysheaderOrderBySyssortAsc(ph_all.get(0).getPhwpid(), w_one.get(0).getWgid(), false);
				if (wp_all.size() == 1) {
					// 比對-檢查SN關聯
					pb_all = pbDao.findAllByPbsnAndPbgid(pb_sn, ph_all.get(0).getPhpbgid());
					if (pb_all.size() == 1) {

						// 放入包裝(body) [01 是排序][_b__ 是分割直][資料庫欄位名稱]
						// doc
						ProductionBody pb_one = pb_all.get(0);
						JSONArray object_doc = new JSONArray();
						JSONArray object_sn = new JSONArray();
						JSONObject object_body_all = new JSONObject();
						ph_all.forEach(one -> {
							JSONObject object_body = new JSONObject();
							object_body.put(FFM.Hmb.M + "ph_s_date", one.getPhsdate() == null ? "" : one.getPhsdate());
							object_body.put(FFM.Hmb.M + "ph_pr_id", one.getProductionRecords().getPrid());
							object_body.put(FFM.Hmb.M + "pr_p_model", one.getProductionRecords().getPrpmodel());
							object_body.put(FFM.Hmb.M + "pr_bom_id", one.getProductionRecords().getPrbomid());
							object_body.put(FFM.Hmb.M + "pr_order_id", one.getProductionRecords().getProrderid());

							object_body.put(FFM.Hmb.M + "pr_c_name", one.getProductionRecords().getPrcname());
							object_body.put(FFM.Hmb.M + "pr_p_quantity", one.getProductionRecords().getPrpquantity());
							object_body.put(FFM.Hmb.M + "sys_status", one.getSysstatus());

							object_body.put(FFM.Hmb.M + "pb_l_size", pb_one.getPblsize());
							object_body.put(FFM.Hmb.M + "pb_l_path", pb_one.getPblpath());
							object_body.put(FFM.Hmb.M + "pb_l_text", pb_one.getPbltext());

							object_body.put(FFM.Hmb.M + "pr_b_item", one.getProductionRecords().getPrbitem());
							object_body.put(FFM.Hmb.M + "pr_s_item", one.getProductionRecords().getPrsitem());
							object_doc.put(object_body);
						});
						object_body_all.put("search", object_doc);

						// SN_list
						ArrayList<Workstation> w_for_sn = wkDao.findAllByWgidAndSysheaderOrderBySyssortAsc(w_one.get(0).getWgid(), false);
						w_for_sn.forEach(w -> {
							JSONObject object_body = new JSONObject();
							object_body.put(w.getWorkstationItem().getWipbcell(), w.getWorkstationItem().getWipbvalue());
							object_sn.put(object_body);
						});

						object_body_all.put("sn_list", object_sn);

						object_body_all.put("workdatation_program_name", wp_all.get(0).getWpname());
						object_body_all.put("workdatation_name", w_one.get(0).getWpbname());
						bean.setBody(object_body_all);

					} else {
						return new PackageBean();
					}
				} else {
					return new PackageBean();
				}
			} else {
				return new PackageBean();
			}
		}
		return bean;
	}

	// 存檔 資料清單
	@Transactional
	public boolean createData(JSONObject body, SystemUser user) {
		boolean check = false;
		try {
//			JSONArray list = body.getJSONArray("create");
//			for (Object one : list) {
//				// 物件轉換
//				SystemConfig sys_c = new SystemConfig();
//				JSONObject data = (JSONObject) one;
//
//			}
//			check = true;
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
			// JSONArray list = body.getJSONArray("save_as");
			// for (Object one : list) {
			// 物件轉換
			// SystemConfig sys_c = new SystemConfig();
			// JSONObject data = (JSONObject) one;
			// }
			// check = true;
		} catch (Exception e) {
			System.out.println(e);
		}
		return check;
	}

	// 更新 資料清單
	@Transactional
	public boolean updateData(JSONObject body, SystemUser user) {
		boolean check = false;
		// w_c_name = 工作站代號
		// ph_pr_id = 製令單
		// pb_position = 產品放置位置
		// pb_f_value = 錯誤代碼
		// pb_sn = SN產品身分
		// pb_value01 - 50

		try {
			JSONObject list = body.getJSONObject("modify");
			System.out.println(list);

			// Step0.查詢SN關聯
			if (list.get("pb_sn") != "") {
				List<ProductionBody> body_s = pbDao.findAllByPbsn(list.getString("pb_sn"));
				if (body_s.size() == 1) {
					// Step1.更新[ProductionBody]
					ProductionBody body_one = body_s.get(0);
					body_one.setPbfvalue(list.getString("pb_f_value"));
					body_one.setPbposition(list.getString("pb_position"));
					try {
						// 可能的SN範圍
						for (int k = 0; k < 50; k++) {
							// 有欄位?
							if (list.has("pb_value" + String.format("%02d", k + 1))) {
								String value = list.getString("pb_value" + String.format("%02d", k + 1));
								String in_name = "setPbvalue" + String.format("%02d", k + 1);
								Method in_method = body_one.getClass().getMethod(in_name, String.class);
								// 欄位有值
								if (value != null && !value.equals("")) {
									in_method.invoke(body_one, value);
								}
							}
						}
						// 工作站->工作站欄位名稱
						ArrayList<Workstation> wk_s = wkDao.findAllByWcname(list.getString("w_c_name"), PageRequest.of(0, 1));
						JSONObject pb_schedule = new JSONObject();

						// 有此工作站+格式正確
						if (wk_s.size() == 1 && Fm_json.isJSONValid(body_one.getPbschedule())) {
							pb_schedule = new JSONObject(body_one.getPbschedule());

							String yn = pb_schedule.getJSONObject(wk_s.get(0).getWid() + "").getString("type");
							boolean yn_check = wk_s.get(0).getWreplace();
							// 不可重複?
							if (yn.equals("Y") && !yn_check) {
								return false;
							}
						} else {
							return false;
						}

						// Step1-1.工作站進度
						body_one.setPbschedule(pb_schedule.toString());
						boolean check_work_fn = true;
						String previous_work = "";
						Iterator<String> keys = pb_schedule.keys();

						while (keys.hasNext()) {
							String key = keys.next();
							if (pb_schedule.get(key) instanceof JSONObject) {
								// 全部都完成?
								String now_work = pb_schedule.getJSONObject(key).getString("type");
								if (now_work.equals("N")) {
									check_work_fn = false;
								}
								// 是否有前置站無
								if (previous_work.equals("")) {
									previous_work = now_work;
								} else {
									// 正常
									if ((now_work.equals("N") && previous_work.equals("N")) || //
											(now_work.equals("N") && previous_work.equals("Y")) || //
											(now_work.equals("Y") && previous_work.equals("Y"))) {
										previous_work = now_work;
									}
									// 不正常
									else {
										pb_schedule.getJSONObject(wk_s.get(0).getWid() + "").put("type", "N");
										body_one.setPbschedule(pb_schedule.toString());
										return false;
									}
								}
							}
						}
						pb_schedule.getJSONObject(wk_s.get(0).getWid() + "").put("type", "Y");
						body_one.setPbschedule(pb_schedule.toString());

						body_one.setPbcheck(check_work_fn);

						// 可能的工作站範圍
						String w_pb_cell = wk_s.get(0).getWpbcell();
						for (int k = 0; k < 20; k++) {
							// 有欄位?
							if (w_pb_cell.equals("pb_w_name" + String.format("%02d", k + 1))) {

								String value = user.getSuaccount();
								String in_name = "setPbwname" + String.format("%02d", k + 1);
								Method in_method = body_one.getClass().getMethod(in_name, String.class);
								// 欄位有值
								if (value != null && !value.equals("")) {
									in_method.invoke(body_one, value);
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
					body_one.setSysmdate(new Date());
					body_one.setSysmuser(user.getSuaccount());
					pbDao.save(body_one);

					// Step3. 製令單+規格更新[ProductionRecords]
					ProductionRecords phprid = new ProductionRecords();
					phprid.setPrid(list.getString("ph_pr_id"));
					ProductionHeader p_header = phDao.findAllByProductionRecords(phprid).get(0);
					// 啟動時間
					if (p_header.getSysstatus() == 0) {
						p_header.setPhsdate(new Date());
					}
					p_header.setSysstatus(1);
					// 規格
					ProductionRecords p_records = p_header.getProductionRecords();

					// 關聯SN
					List<ProductionBody> p_body = pbDao.findAllByPbgidOrderByPbsnAsc(p_header.getPhpbgid());
					int finish = 0;
					// 完成?
					for (ProductionBody productionBody : p_body) {
						if (productionBody.getPbcheck()) {
							finish += 1;
						}
					}
					p_records.setPrpokquantity(finish);
					p_header.setPhschedule(p_records.getPrpokquantity() + "／" + p_records.getPrpquantity());
					p_header.setProductionRecords(p_records);
					phDao.save(p_header);

					check = true;
				}
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
			// JSONArray list = body.getJSONArray("delete");
//			for (Object one : list) {
//				// 物件轉換
//				SystemConfig sys_p = new SystemConfig();
//				JSONObject data = (JSONObject) one;
//				sys_p.setScid(data.getInt("sc_id"));
//
//				// configDao.deleteByScidAndSysheader(sys_p.getScid(), false);
//				check = true;
//			}
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return check;
	}
}
