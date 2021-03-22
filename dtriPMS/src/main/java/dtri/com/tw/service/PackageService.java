package dtri.com.tw.service;

import java.util.Date;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import dtri.com.tw.bean.PackageBean;
import dtri.com.tw.tools.Fm_Time;

@Service
public class PackageService {

	/**
	 * From To Back分析包裝
	 * 
	 **/
	public PackageBean jsonToObj(JSONObject json_object) {
		PackageBean data = new PackageBean();

		if (!json_object.isNull("req_content")) {
			String checkWord = json_object.getJSONObject("req_content").toString();
			// 排除特定符號 Ex:{= | '}
			checkWord = checkWord.replace("=", "").replace("'", "").replace("|", "");
			// 進行解析 與 分裝物件
			JSONObject req = new JSONObject(checkWord);
			data.setType_content("req");
			data.setDate(req.isNull("date") ? new Date() : Fm_Time.toDate(req.getString("date")));
			data.setAction(req.isNull("action") ? null : req.getString("action"));
			data.setHeader(req.isNull("header") ? null : req.getJSONObject("header"));
			data.setBody(req.isNull("body") ? null : req.getJSONArray("body"));
			data.setPage_batch(req.isNull("page_batch") ? 0 : req.getInt("page_batch"));
			data.setPage_now_nb(req.isNull("page_now_nb") ? 1 : req.getInt("page_now_nb"));
			data.setPage_total(req.isNull("page_total") ? 100 : req.getInt("page_total"));
			data.setCell_bk_fn(req.isNull("cell_bk_fn") ? null : req.getString("cell_bk_fn"));
			data.setCell_bk_vals(req.isNull("cell_bk_vals") ? new JSONObject() : req.getJSONObject("cell_bk_vals"));
			data.setHtml_body(req.isNull("html_body") ? null : req.getString("html_body"));
			data.setInfo("");
		} else {
			data = null;
		}

		return data;
	}

	/**
	 * Back To From資料包裝
	 * 
	 **/
	public String objToJson(PackageBean object) {
		JSONObject data = new JSONObject();
		data.put("date", object.getDate());
		data.put("action", object.getaction());
		data.put("header", object.getHeader());
		data.put("body", object.getBody());
		data.put("page_batch", object.getPage_batch());
		data.put("page_now_nb", object.getPage_now_nb());
		data.put("page_total", object.getPage_total());
		data.put("info", object.getInfo());
		data.put("info_color", object.getInfo_color());
		data.put("info_user", object.getInfo_user());
		data.put("cell_bk_fn", object.getCell_bk_fn());
		data.put("cell_bk_vals", object.getCell_bk_vals());
		data.put("html_body", object.getHtml_body());
		System.out.println(data.toString());
		return new JSONObject().put("resp_content", data).toString();
	}

	/**
	 * 其他類型包裝
	 * 
	 * @param resp_object 回應包
	 * @param req_object  請求包
	 * @param info        訊息
	 * @param info_color  顏色
	 **/
	public PackageBean setObjResp(PackageBean resp_object, PackageBean req_object, String info, String info_color) {
		resp_object.setAction(req_object.getaction() == null ? "AR" : req_object.getaction());
		resp_object.setCell_bk_fn(req_object.getCell_bk_fn() == null ? "" : req_object.getCell_bk_fn());
		resp_object.setCell_bk_vals(
				req_object.getCell_bk_vals() == null ? new JSONObject() : req_object.getCell_bk_vals());
		resp_object.setInfo(info == null ? PackageBean.info_message_success : info);
		resp_object.setInfo_color(info_color == null ? PackageBean.info_color_success : info_color);// --danger--warning--success
		resp_object.setPage_batch(req_object.getPage_batch() == null ? 1 : req_object.getPage_batch());
		resp_object.setPage_now_nb(req_object.getPage_now_nb() == null ? 1 : req_object.getPage_now_nb());
		resp_object.setPage_total(req_object.getPage_total() == null ? 100 : req_object.getPage_total());
		resp_object.setHtml_body(req_object.getHtml_body() == null ? "" : req_object.getHtml_body());
		return resp_object;
	}
}
