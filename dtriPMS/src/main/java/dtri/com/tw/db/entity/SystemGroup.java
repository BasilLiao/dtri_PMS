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
 * @see ---共用型---<br>
 *      sys_c_date : 創建時間<br>
 *      sys_c_user : 創建人名<br>
 *      sys_m_date : 修改時間<br>
 *      sys_m_user : 修改人名<br>
 *      sys_ver : 修改版本<br>
 *      sys_note : 備註<br>
 *      sys_status : 資料狀態<br>
 *      sys_sort : 自訂排序<br>
 *      ---權限群組---<br>
 *      sg_id : 群組ID<br>
 *      sg_group_id : 權限群組ID<br>
 *      sg_name : 群組名稱<br>
 *      sg_sper_id : 功能權限ID<br>
 *      sg_permission : 功能權限驗證<br>
 */
@Entity
@Table(name = "system_group")
@EntityListeners(AuditingEntityListener.class)
public class SystemGroup {
	public SystemGroup() {
		this.syscdate = new Date();
		this.syscuser = "system";
		this.sysmdate = new Date();
		this.sysmuser = "system";
		this.sysver = 0;
		this.sysnote = "";
		this.syssort = 0;
		this.sysstatus = 0;
		this.sysheader = false;
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

	@Column(name = "sys_header", nullable = false, columnDefinition = "boolean default false")
	private Boolean sysheader;

	// 群組型
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "system_group_seq")
	@SequenceGenerator(name = "system_group_seq", sequenceName = "system_group_seq")
	@Column(name = "sg_id")
	private Integer sgid;

	@Column(name = "sg_g_id", nullable = false, columnDefinition = "int default 0")
	private Integer sggid;

	@Column(name = "sg_name", nullable = false, columnDefinition = "varchar(50)")
	private String sgname;

	@Column(name = "sg_permission", nullable = false, columnDefinition = "varchar(10) default '0000000000'")
	private String sgpermission;

	@ManyToOne(targetEntity = SystemPermission.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "sg_sp_id")
	private SystemPermission systemPermission;

	public Boolean getSysheader() {
		return sysheader;
	}

	public void setSysheader(Boolean sysgheader) {
		this.sysheader = sysgheader;
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

	public Integer getSgid() {
		return sgid;
	}

	public void setSgid(Integer sgid) {
		this.sgid = sgid;
	}

	public Integer getSggid() {
		return sggid;
	}

	public void setSggid(Integer sggid) {
		this.sggid = sggid;
	}

	public String getSgname() {
		return sgname;
	}

	public void setSgname(String sgname) {
		this.sgname = sgname;
	}

	public String getSgpermission() {
		return sgpermission;
	}

	public void setSgpermission(String sgpermission) {
		this.sgpermission = sgpermission;
	}

	public SystemPermission getSystemPermission() {
		return systemPermission;
	}

	public void setSystemPermission(SystemPermission systemPermission) {
		this.systemPermission = systemPermission;
	}

}
