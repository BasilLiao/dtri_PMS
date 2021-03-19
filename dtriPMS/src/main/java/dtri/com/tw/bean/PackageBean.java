package dtri.com.tw.bean;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Basil 包裝 回傳 or 傳遞 物件
 * 
 **/
public class PackageBean {
	// 常用代號
	// danger--warning--success
	public static final String info_color_success = "success";
	public static final String info_color_warning = "warning";
	public static final String info_color_danger = "danger";
	public static final String info_message_success = "[000] The command was executed [Successfully]!!";
	public static final String info_message_warning = "[001] The command was executed [Warning]!!";
	public static final String info_message_danger = "[002] The command was executed [ERROR]!!";
	public static final String info_administrator = " Please contact the system administrator #321";

	// "resp_content" or "req_content"
	private String type_content;// 請求 or 回傳
	private Date date;// 時間
	private String action;// 請求 動作行為
	private JSONObject header;// title 名稱表
	private JSONArray body;// 資料 內 容物
	private Integer page_total;// 每次 總頁數 ex :10
	private Integer page_batch;// 第幾批
	private Integer page_now_nb;// 第幾分頁
	private String info; // 回傳資訊
	private String info_color; // 回傳資訊 顏色
	private String cell_bk_fn;// 回傳呼叫方法
	private JSONObject cell_bk_vals;// 回傳呼叫傳遞值
	private String html_body;//切換頁面
	private JSONObject info_user;//使用者資訊

	public PackageBean() {
		this.info = info_message_danger + info_administrator;
		this.info_color = info_color_danger;
		this.date = new Date();
	}

	public String getType_content() {
		return type_content;
	}

	public void setType_content(String type_content) {
		this.type_content = type_content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getaction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public JSONArray getBody() {
		return body;
	}

	public void setBody(JSONArray body) {
		this.body = body;
	}

	public Integer getPage_total() {
		return page_total;
	}

	public void setPage_total(Integer page_total) {
		this.page_total = page_total;
	}

	public Integer getPage_batch() {
		return page_batch;
	}

	public void setPage_batch(Integer page_batch) {
		this.page_batch = page_batch;
	}

	public Integer getPage_now_nb() {
		return page_now_nb;
	}

	public void setPage_now_nb(Integer page_now_nb) {
		this.page_now_nb = page_now_nb;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getCell_bk_fn() {
		return cell_bk_fn;
	}

	public void setCell_bk_fn(String cell_bk_fn) {
		this.cell_bk_fn = cell_bk_fn;
	}

	public JSONObject getCell_bk_vals() {
		return cell_bk_vals;
	}

	public void setCell_bk_vals(JSONObject cell_bk_vals) {
		this.cell_bk_vals = cell_bk_vals;
	}

	public JSONObject getHeader() {
		return header;
	}

	public void setHeader(JSONObject header) {
		this.header = header;
	}

	public String getInfo_color() {
		return info_color;
	}

	public void setInfo_color(String info_color) {
		this.info_color = info_color;
	}

	public String getHtml_body() {
		return html_body;
	}

	public void setHtml_body(String html_body) {
		this.html_body = html_body;
	}

	public JSONObject getInfo_user() {
		return info_user;
	}

	public void setInfo_user(JSONObject info_user) {
		this.info_user = info_user;
	}

}
