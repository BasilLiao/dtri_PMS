package dtri.com.tw.db.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author Basil
 * 
 * @see ---共用型---<br>
 *      sys_c_date : 創建時間<br>
 *      sys_c_user : 創建人名<br>
 *      sys_m_date : 修改時間<br>
 *      sys_m_user : 修改人名<br>
 *      sys_ver : 修改版本<br>
 *      sys_note : 備註<br>
 *      sys_status : 資料狀態<br>
 *      sys_sort : 自訂排序<br>
 *      ---工作站項目---<br>
 *      wo_i_id : 主key<br>
 *      wo_g_id : 群組<br>
 *      wo_codename : 登記工作站條碼用<br>
 *      wo_g_name : 工作站名稱<br>
 *      wo_i_name : 工作站項目名稱<br>
 *      wo_control : 工作站模板代號<br>
 *      wo_employee : 可使用此工作站人員[001,002]<br>
 * 
 * 
 **/
@Entity
@Table(name = "workstation_project")
@EntityListeners(AuditingEntityListener.class)
public class WorkstationProject {

	public WorkstationProject() {
		this.syscdate = new Date();
		this.syscuser = "system";
		this.sysmdate = new Date();
		this.sysmuser = "system";
		this.sysver = 0;
		this.sysnote = "";
		this.syssort = 0;
		this.sysstatus = 0;
	}

	// 共用型
	@Column(name = "sys_c_date", nullable = false, columnDefinition = "TIMESTAMP default now()")
	private Date syscdate;

	@Column(name = "sys_c_user", nullable = false, columnDefinition = "varchar(50) default 'system'")
	private String syscuser;

	@Column(name = "sys_m_date", nullable = false, columnDefinition = "TIMESTAMP default now()")
	private Date sysmdate;

	@Column(name = "sys_m_user", nullable = false, columnDefinition = "varchar(50) default 'system'")
	private String sysmuser;

	@Column(name = "sys_ver", columnDefinition = "int default 0")
	private Integer sysver;

	@Column(name = "sys_note", columnDefinition = "text default ''")
	private String sysnote;

	@Column(name = "sys_status", columnDefinition = "int default 0")
	private Integer sysstatus;

	@Column(name = "sys_sort", columnDefinition = "int default 0")
	private Integer syssort;

	// 工作站
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wo_i_id")
	private Integer woiid;

	@Column(name = "wo_g_id", nullable = false)
	private Integer wogid;

	@Column(name = "wo_codename", nullable = false, columnDefinition = "varchar(50)")
	private String wocodename;

	@Column(name = "wo_g_name", nullable = false, columnDefinition = "varchar(50)")
	private String wogname;

	@Column(name = "wo_i_name", nullable = false, columnDefinition = "varchar(50)")
	private String woiname;

	@Column(name = "wo_control", nullable = false, columnDefinition = "varchar(50)")
	private String wocontrol;

	@Column(name = "wo_employee", columnDefinition = "varchar(200) default ''")
	private String woemployee;

}
