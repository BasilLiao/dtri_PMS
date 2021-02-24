package dtri.com.tw.bean;

import java.util.Date;

import org.json.JSONArray;

/**
 * @author Basil <br>
 *         user 請求人<br>
 *         date 時間<br>
 *         active 行動<br>
 *         body 資料內容<br>
 *         page_total 總頁筆數 <br>
 *         page_bach 批次數量 <br>
 *         page_now_nb 正在第幾分頁 <br>
 **/
public class DataInfoReq {
	private String user;
	private Date date;
	private String active;
	private JSONArray body;
	private int page_total;
	private int page_bach;
	private int page_now_nb;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public JSONArray getBody() {
		return body;
	}

	public void setBody(JSONArray body) {
		this.body = body;
	}

	public int getPage_total() {
		return page_total;
	}

	public void setPage_total(int page_total) {
		this.page_total = page_total;
	}

	public int getPage_bach() {
		return page_bach;
	}

	public void setPage_bach(int page_bach) {
		this.page_bach = page_bach;
	}

	public int getPage_now_nb() {
		return page_now_nb;
	}

	public void setPage_now_nb(int page_now_nb) {
		this.page_now_nb = page_now_nb;
	}

}