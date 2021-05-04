package dtri.com.tw.db.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
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
 *      w_id : 主key<br>
 *      w_g_id : 群組<br>
 *      w_i_id : 項目ID<br>
 *      w_codename : 登記工作站條碼用<br>
 *      w_name : 工作站名稱<br>
 *      w_s_group : 可使用此工作站群組<br>
 * 
 * 
 **/
@Entity
@Table(name = "workstation")
@EntityListeners(AuditingEntityListener.class)
public class Workstation {

	public Workstation() {
		this.syscdate = new Date();
		this.syscuser = "system";
		this.sysmdate = new Date();
		this.sysmuser = "system";
		this.sysver = 0;
		this.sysnote = "";
		this.syssort = 0;
		this.sysstatus = 0;
		this.sysgheader = false;
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

	@Column(name = "sys_g_header", nullable = false, columnDefinition = "boolean default false")
	private Boolean sysgheader;

	// 工作站
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "workstation_seq")
	@SequenceGenerator(name = "workstation_seq", sequenceName = "workstation_seq", allocationSize = 1)
	@Column(name = "w_id")
	private Integer wid;

	@Column(name = "w_g_id", nullable = false)
	private Integer wgid;

	@ManyToOne(targetEntity = WorkstationItem.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "w_i_id")
	private WorkstationItem workstationItem;


	@Column(name = "w_codename", nullable = false, columnDefinition = "varchar(50)")
	private String wcodename;

	@Column(name = "w_name", nullable = false, columnDefinition = "varchar(50)")
	private String wname;

	@Column(name = "w_s_group", columnDefinition = "int default 0")
	private Integer wsgroup;

}
