package dtri.com.tw.bean;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Basil <br>
 *         date 時間<br>
 *         body 資料內容<br>
 *         herder 資料開頭<br>
 *         page_total 總頁筆數 <br>
 *         page_bach 批次數量 <br>
 *         page_now_nb 正在第幾分頁 <br>
 **/
public class DataInfoResp {
	private Date date;
	private JSONArray body;
	private JSONObject header;
	private int page_total;
	private int page_bach;
	private int page_now_nb;

	public DataInfoResp() {

	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public JSONObject getHeader() {
		return header;
	}

	public void setHeader(JSONObject header) {
		this.header = header;
	}

}