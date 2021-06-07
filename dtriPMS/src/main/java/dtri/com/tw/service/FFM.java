package dtri.com.tw.service;

public class FFM {
	/** 限制規格-ID格式 **/
	public static enum Hmb {
		H, M, B
	}

	/** 限制規格-顯示 **/
	public static enum See {
		DIS, SHO
	}

	/** 限制規格-標籤 **/
	public static enum Tag {
		INP, TTA, SEL, CHE
	}

	/** 限制規格-類型 **/
	public static enum Type {
		TEXT, NUMB, PASS, DATE,CHE
	}

	/** 限制規格-大小 **/
	public static enum Size {
		S10, S15, S20, S25, S30
	}

	// 解析-代號-屬性設置
	public static String choose(String key) {
		String value = "";
		switch (key) {
		// See
		case "DIS":
			value = "disabled";
			break;
		case "SHO":
			value = "show";
			break;
		// Tag
		case "INP":
			value = "input";
			break;
		case "TTA":
			value = "textarea";
			break;
		case "SEL":
			value = "select";
			break;
		case "CHE":
			value = "checkbox";
			break;
		// Type
		case "TEXT":
			value = "text";
			break;
		case "NUMB":
			value = "number";
			break;
		case "PASS":
			value = "password";
			break;
		case "DATE":
			value = "date";
			break;
		// SIZE
		case "S10":
			value = "100px";
			break;
		case "S15":
			value = "150px";
			break;
		case "S20":
			value = "200px";
			break;
		case "S25":
			value = "250px";
			break;
		case "S30":
			value = "300px";
			break;
		// ID-格式
		case "H":
			value = "_h__";
			break;
		case "B":
			value = "_b__";
			break;
		case "M":
			value = "_m__";
			break;
		// col
		}
		return value;
	}
}
