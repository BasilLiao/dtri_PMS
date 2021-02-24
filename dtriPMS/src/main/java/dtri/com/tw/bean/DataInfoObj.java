package dtri.com.tw.bean;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

/**
 * @author Basil @<br>
 *         接收+回傳<br>
 *         cell_bk_fn 回傳呼叫方法<br>
 *         cell_bk_vals 回傳參數<br>
 *         req_content 請求內容<br>
 *         resp_content 回覆內容<br>
 *         resp_info 回復訊息<br>
 *         resp_all 回傳所有JSON資訊<br>
 */
public class DataInfoObj {
	private String cell_bk_fn;
	private JSONObject cell_bk_vals;
	private Map<String, DataInfoReq> req_content;
	private Map<String, DataInfoResp> resp_content;
	private String info;
	private JSONObject resp_data;

	public DataInfoObj() {
		this.setReq_content(new HashMap<String, DataInfoReq>());
		this.setResp_content(new HashMap<String, DataInfoResp>());

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

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Map<String, DataInfoReq> getReq_content() {
		return req_content;
	}

	public void setReq_content(Map<String, DataInfoReq> req_content) {
		this.req_content = req_content;
	}

	public Map<String, DataInfoResp> getResp_content() {
		return resp_content;
	}

	public void setResp_content(Map<String, DataInfoResp> resp_content) {
		this.resp_content = resp_content;
	}

	public JSONObject getResp_data() {
		return resp_data;
	}

	public void setResp_data(JSONObject resp_data) {
		this.resp_data = resp_data;
	}

}