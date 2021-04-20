package dtri.com.tw.db.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 *      wp_id : 主key<br>
 *      wp_g_work : 群組<br>
 *      wp_name : 工作程序名稱<br>
 *      wp_g_work : 工作站(群組)<br>
 *      wp_s_work : 工作站(順序)<br>
 *      wp_codename :2維代號(選擇工作流程)<br>
 *      wp_o_work : 工作站one(關聯)<br>
 * 
 * 
 **/
@Entity
@Table(name = "workstation_program")
@EntityListeners(AuditingEntityListener.class)
public class WorkstationProgram {

	public WorkstationProgram() {
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
	@Column(name = "wp_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "workstation_program_seq")
	@SequenceGenerator(name = "workstation_program_seq", sequenceName = "workstation_program_seq",allocationSize =1) 
	private Integer wpid;

	@Column(name = "wp_g_id", nullable = false)
	private Integer wpgid;

	@Column(name = "wp_name", nullable = false, columnDefinition = "varchar(50)")
	private String wpname;

	@Column(name = "wp_g_work", nullable = false)
	private Integer wpgwork;

	@Column(name = "wp_s_work", nullable = false)
	private Integer wpswork;

	@Column(name = "wp_codename", nullable = false, columnDefinition = "varchar(50)")
	private String wpcodename;

	@Column(name = "wp_o_work", nullable = false)
	private Integer wpowork;

	public Boolean getSysgheader() {
		return sysgheader;
	}

	public void setSysgheader(Boolean sysgheader) {
		this.sysgheader = sysgheader;
	}

	public Date getSyscdate() {
		return syscdate;
	}

	public void setSyscdate(Date syscdate) {
		this.syscdate = syscdate;
	}

	public String getSyscuser() {
		return syscuser;
	}

	public void setSyscuser(String syscuser) {
		this.syscuser = syscuser;
	}

	public Date getSysmdate() {
		return sysmdate;
	}

	public void setSysmdate(Date sysmdate) {
		this.sysmdate = sysmdate;
	}

	public String getSysmuser() {
		return sysmuser;
	}

	public void setSysmuser(String sysmuser) {
		this.sysmuser = sysmuser;
	}

	public Integer getSysver() {
		return sysver;
	}

	public void setSysver(Integer sysver) {
		this.sysver = sysver;
	}

	public String getSysnote() {
		return sysnote;
	}

	public void setSysnote(String sysnote) {
		this.sysnote = sysnote;
	}

	public Integer getSysstatus() {
		return sysstatus;
	}

	public void setSysstatus(Integer sysstatus) {
		this.sysstatus = sysstatus;
	}

	public Integer getSyssort() {
		return syssort;
	}

	public void setSyssort(Integer syssort) {
		this.syssort = syssort;
	}

	public Integer getWpid() {
		return wpid;
	}

	public void setWpid(Integer wpid) {
		this.wpid = wpid;
	}

	public Integer getWpgid() {
		return wpgid;
	}

	public void setWpgid(Integer wpgid) {
		this.wpgid = wpgid;
	}

	public String getWpname() {
		return wpname;
	}

	public void setWpname(String wpname) {
		this.wpname = wpname;
	}

	public Integer getWpgwork() {
		return wpgwork;
	}

	public void setWpgwork(Integer wpgwork) {
		this.wpgwork = wpgwork;
	}

	public Integer getWpswork() {
		return wpswork;
	}

	public void setWpswork(Integer wpswork) {
		this.wpswork = wpswork;
	}

	public String getWpcodename() {
		return wpcodename;
	}

	public void setWpcodename(String wpcodename) {
		this.wpcodename = wpcodename;
	}

	public Integer getWpowork() {
		return wpowork;
	}

	public void setWpowork(Integer wpowork) {
		this.wpowork = wpowork;
	}

}
