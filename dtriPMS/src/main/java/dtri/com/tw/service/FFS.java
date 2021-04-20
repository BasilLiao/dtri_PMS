package dtri.com.tw.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * 
 * FrontFormatService = FFS
 * 
 */
@Service
public class FFS {
	// 屬性設置
	public static final String INP = "input", TTA = "textarea", SEL = "select", CHE = "checkbox";
	public static final String TEXT = "text", NUMB = "number",PASS = "password";
	public static final String DIS = "disabled", SHO = "show";
	// 大小
	public static final String S10 = "100px", S15 = "150px", S20 = "200px", S30 = "300px";
	// 規則設定
	public static final String H = "_h__", M = "m__", B = "_b__";

	// 格式(CUD)需求包裝
	/**
	 * h_m = h_modify
	 * 
	 * @param tag         : 標籤
	 * @param type        : 類型 select/input/textarea
	 * @param placeholder : 預設文字
	 * @param value       : 預設值
	 * @param show        : disabled/show 顯示?
	 * @param col         : 規格
	 * @param required    : 是否必寫
	 * @param values      : 多選項select 才需要[{key:value,key:value}]
	 * @param id          : ID
	 * @param name        : 名稱
	 * 
	 * @return 格式 : {type:"select/input/textarea", <br>
	 *         required:"true/false",placeholder:"XXXX",<br>
	 *         show:"d-none/disabled/show", values:[{key:value,key:value}]}
	 * 
	 **/
	public static JSONObject h_m(String tag, String type, String placeholder, String value, String show, String col, boolean required,
			JSONArray values, String id, String name) {
		JSONObject object_value = new JSONObject();
		object_value.put("name", name);
		object_value.put("id", FFS.M+id);// id
		object_value.put("tag", tag);// 類型
		object_value.put("type", type);// 驗證格式
		object_value.put("required", required);// 是否必寫
		object_value.put("placeholder", placeholder);// 預設文字
		object_value.put("value", value);// 預設值
		object_value.put("show", show);// 顯示?
		object_value.put("col", col);// 顯示?
		object_value.put("values", values);// 多選項select (可能有)
		return object_value;
	}

	// 格式(R)需求包裝
	/**
	 * h_search = h_s
	 * 
	 * @param name   :查詢名稱
	 * @param tag    :類型 select/input/textarea
	 * @param type   :驗證格式 text/number/date
	 * @param value  :預設值
	 * @param col    :格是寬度col-(md/lg/sm)-數字
	 * @param id     :該欄位ID
	 * @param values :多選項select 才需要[{key:value,key:value}]
	 * 
	 * @return 格式 :
	 *         {name:xxx,type:(select/input/textarea),col:(col-(md/lg/sm)-數字),id:"",values:[{key:value,key:value}]}
	 **/
	public static JSONObject h_s(String tag, String type, String value, String col, String id, String name, JSONArray values) {
		JSONObject one_search = new JSONObject();
		one_search.put("name", name);
		one_search.put("tag", tag);
		one_search.put("type", type);
		one_search.put("value", value);// 預設值
		one_search.put("col", col);
		one_search.put("id", id);
		one_search.put("values", values);
		return one_search;
	}

	// Table header 格式(標題/寬度)
	/**
	 * h_t=h_title
	 * 
	 * @param name 欄位名稱
	 * @param size 欄位寬度
	 * 
	 * @return 格式 : {name:xxx,size:50px}
	 **/
	public static JSONObject h_t(String name, String size, String show) {
		JSONObject one_title = new JSONObject();
		one_title.put("name", name);// 欄位名稱
		one_title.put("size", size);// 寬度?
		one_title.put("show", show);// 顯示?
		return one_title;
	}

	// 自動排序
	/**
	 * @param ord 排序 雙位數
	 * 
	 **/
	public static String ord(int ord, String w) {
		return String.format("%02d", ord) + w;
	}

}
