package dtri.com.tw.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class FrontFormatService {

	// 格式(CUD)需求包裝
	/**
	 * @param name        :名稱
	 * @param tag         : 標籤
	 * @param type        : 類型 select/input/textarea
	 * @param required    : 是否必寫
	 * @param placeholder : 預設文字
	 * @param show        : disabled/show 顯示?
	 * @param values      : 多選項select 才需要[{key:value,key:value}]
	 * 
	 * @return 格式 : {type:"select/input/textarea", <br>
	 *         required:"true/false",placeholder:"XXXX",<br>
	 *         show:"d-none/disabled/show", values:[{key:value,key:value}]}
	 * 
	 **/
	public JSONObject h_modify(String tag, String type, String placeholder, String show, String col, boolean required, JSONArray values, String id,
			String name) {
		JSONObject object_value = new JSONObject();
		object_value.put("name", name);
		object_value.put("id", id);// id
		object_value.put("tag", tag);// 類型
		object_value.put("type", type);// 驗證格式
		object_value.put("required", required);// 是否必寫
		object_value.put("placeholder", placeholder);// 預設文字
		object_value.put("show", show);// 顯示?
		object_value.put("col", col);// 顯示?
		object_value.put("values", values);// 多選項select (可能有)
		return object_value;
	}

	// 格式(R)需求包裝
	/**
	 * @param name   :查詢名稱
	 * @param tag    :類型 select/input/textarea
	 * @param type   :驗證格式 text/number/date
	 * @param col    :格是寬度col-(md/lg/sm)-數字
	 * @param id     :該欄位ID
	 * @param values :多選項select 才需要[{key:value,key:value}]
	 * 
	 * @return 格式 :
	 *         {name:xxx,type:(select/input/textarea),col:(col-(md/lg/sm)-數字),id:"",values:[{key:value,key:value}]}
	 **/
	public JSONObject h_search(String tag, String type, String col, String id, String name, JSONArray values) {
		JSONObject one_search = new JSONObject();
		one_search.put("name", name);
		one_search.put("tag", tag);
		one_search.put("type", type);
		one_search.put("col", col);
		one_search.put("id", id);
		one_search.put("values", values);
		return one_search;
	}

	// Table header 格式(標題/寬度)
	/**
	 * @param name 欄位名稱
	 * @param size 欄位寬度
	 * 
	 * @return 格式 : {name:xxx,size:50px}
	 **/
	public JSONObject h_title(String name, String size, String show) {
		JSONObject one_title = new JSONObject();
		one_title.put("name", name);// 欄位名稱
		one_title.put("size", size);// 寬度?
		one_title.put("show", show);// 顯示?
		return one_title;
	}

}
