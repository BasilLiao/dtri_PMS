package dtri.com.tw.db.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author Basil
 * @see 產品製程-主體<br>
 *      pb_id : ID 0:主產品材料(項目SN) 1:之後為_值內容<br>
 *      pb_sn :SN(材料序號)<br>
 *      pb_f_value : SN(需維修項目) (JSON 包裝)<br>
 *      pb_f_note : 損壞說明<br>
 *      pb_value : pb_value1-50 SN(材料序號) <br>
 *      pb_text : PLT_Log 內容資訊 <br>
 * 
 */
@Entity
@Table(name = "production_body")
@EntityListeners(AuditingEntityListener.class)
public class ProductionBody {
	public ProductionBody() {
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

	@Column(name = "sys_sort", columnDefinition = "int default 0")
	private Integer syssort;

	@Column(name = "sys_status", columnDefinition = "int default 0")
	private Integer sysstatus;

	@Column(name = "sys_header", nullable = false, columnDefinition = "boolean default false")
	private Boolean sysheader;

	// 功能項目
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "production_header_seq")
	@SequenceGenerator(name = "production_header_seq", sequenceName = "production_header_seq")
	@Column(name = "pb_id")
	private Integer pbid;

	@Column(name = "pb_sn", nullable = false, columnDefinition = "varchar(50)")
	private String pbsn;

	@Column(name = "pb_f_value", nullable = false, columnDefinition = "varchar(50)")
	private String pbfvalue;

	@Column(name = "pb_f_note", nullable = false)
	private String pbfnote;

	@Column(name = "pb_value1", columnDefinition = "varchar(50)")
	private String pbvalue1;

	@Column(name = "pb_value2", columnDefinition = "varchar(50)")
	private String pbvalue2;

	@Column(name = "pb_value3", columnDefinition = "varchar(50)")
	private String pbvalue3;

	@Column(name = "pb_value4", columnDefinition = "varchar(50)")
	private String pbvalue4;

	@Column(name = "pb_value5", columnDefinition = "varchar(50)")
	private String pbvalue5;

	@Column(name = "pb_text", nullable = true, columnDefinition = "TIMESTAMP")
	private Date pbtext;

	@OneToOne(mappedBy = "productionBody")
	private ProductionHeader productionHeader;

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

	public Integer getSyssort() {
		return syssort;
	}

	public void setSyssort(Integer syssort) {
		this.syssort = syssort;
	}

	public Integer getSysstatus() {
		return sysstatus;
	}

	public void setSysstatus(Integer sysstatus) {
		this.sysstatus = sysstatus;
	}

	public Boolean getSysheader() {
		return sysheader;
	}

	public void setSysheader(Boolean sysheader) {
		this.sysheader = sysheader;
	}

	public Integer getPbid() {
		return pbid;
	}

	public void setPbid(Integer pbid) {
		this.pbid = pbid;
	}

	public String getPbsn() {
		return pbsn;
	}

	public void setPbsn(String pbsn) {
		this.pbsn = pbsn;
	}

	public String getPbfvalue() {
		return pbfvalue;
	}

	public void setPbfvalue(String pbfvalue) {
		this.pbfvalue = pbfvalue;
	}

	public String getPbfnote() {
		return pbfnote;
	}

	public void setPbfnote(String pbfnote) {
		this.pbfnote = pbfnote;
	}

	public String getPbvalue1() {
		return pbvalue1;
	}

	public void setPbvalue1(String pbvalue1) {
		this.pbvalue1 = pbvalue1;
	}

	public String getPbvalue2() {
		return pbvalue2;
	}

	public void setPbvalue2(String pbvalue2) {
		this.pbvalue2 = pbvalue2;
	}

	public String getPbvalue3() {
		return pbvalue3;
	}

	public void setPbvalue3(String pbvalue3) {
		this.pbvalue3 = pbvalue3;
	}

	public String getPbvalue4() {
		return pbvalue4;
	}

	public void setPbvalue4(String pbvalue4) {
		this.pbvalue4 = pbvalue4;
	}

	public String getPbvalue5() {
		return pbvalue5;
	}

	public void setPbvalue5(String pbvalue5) {
		this.pbvalue5 = pbvalue5;
	}

	public Date getPbtext() {
		return pbtext;
	}

	public void setPbtext(Date pbtext) {
		this.pbtext = pbtext;
	}

	public ProductionHeader getProductionHeader() {
		return productionHeader;
	}

	public void setProductionHeader(ProductionHeader productionHeader) {
		this.productionHeader = productionHeader;
	}

}
