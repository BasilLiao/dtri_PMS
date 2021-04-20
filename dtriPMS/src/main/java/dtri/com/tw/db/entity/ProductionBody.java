package dtri.com.tw.db.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author Basil
 * @see 產品製程-主體<br>
 *      pb_id : ID 0:主產品材料(項目SN) 1:之後為_值內容<br>
 *      pb_ph_id : 群組對應製令ID<br>
 *      pb_sn :SN(出貨序號)<br>
 *      pb_f_value : SN(需維修項目) (JSON 包裝)<br>
 *      pb_f_note : 損壞說明<br>
 *      pb_value : pb_value1-50 SN(材料序號) <br>
 *      pb_text : PLT_Log 內容資訊 <br>
 * 
 */
@IdClass(ProductionBody.PrimaryKey.class)
@Entity
@Table(name = "production_body")
@EntityListeners(AuditingEntityListener.class)
public class ProductionBody implements Serializable {

	private static final long serialVersionUID = 1L;

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
	@Id
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
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "production_body_seq")
	@SequenceGenerator(name = "production_body_seq", sequenceName = "production_body_seq", allocationSize = 1)
	@Column(name = "pb_id")
	private Integer pbid;

	@Column(name = "pb_sn", nullable = false, columnDefinition = "varchar(50)")
	private String pbsn;

	@Column(name = "pb_f_value", columnDefinition = "varchar(255)")
	private String pbfvalue;

	@Column(name = "pb_f_note", columnDefinition = "varchar(255)")
	private String pbfnote;
	
	@Column(name = "pb_schedule", columnDefinition = "varchar(255)")
	private String pbschedule;
	
	@Column(name = "pb_check", columnDefinition = "boolean DEFAULT false")
	private Boolean pbcheck;
	

	@Column(name = "pb_value01", columnDefinition = "varchar(50)")
	private String pbvalue01;

	@Column(name = "pb_value02", columnDefinition = "varchar(50)")
	private String pbvalue02;

	@Column(name = "pb_value03", columnDefinition = "varchar(50)")
	private String pbvalue03;

	@Column(name = "pb_value04", columnDefinition = "varchar(50)")
	private String pbvalue04;

	@Column(name = "pb_value05", columnDefinition = "varchar(50)")
	private String pbvalue05;

	@Column(name = "pb_value06", columnDefinition = "varchar(50)")
	private String pbvalue06;

	@Column(name = "pb_value07", columnDefinition = "varchar(50)")
	private String pbvalue07;

	@Column(name = "pb_value08", columnDefinition = "varchar(50)")
	private String pbvalue08;

	@Column(name = "pb_value09", columnDefinition = "varchar(50)")
	private String pbvalue09;

	@Column(name = "pb_value10", columnDefinition = "varchar(50)")
	private String pbvalue10;

	@Column(name = "pb_value11", columnDefinition = "varchar(50)")
	private String pbvalue11;

	@Column(name = "pb_value12", columnDefinition = "varchar(50)")
	private String pbvalue12;

	@Column(name = "pb_value13", columnDefinition = "varchar(50)")
	private String pbvalue13;

	@Column(name = "pb_value14", columnDefinition = "varchar(50)")
	private String pbvalue14;

	@Column(name = "pb_value15", columnDefinition = "varchar(50)")
	private String pbvalue15;

	@Column(name = "pb_value16", columnDefinition = "varchar(50)")
	private String pbvalue16;

	@Column(name = "pb_value17", columnDefinition = "varchar(50)")
	private String pbvalue17;

	@Column(name = "pb_value18", columnDefinition = "varchar(50)")
	private String pbvalue18;

	@Column(name = "pb_value19", columnDefinition = "varchar(50)")
	private String pbvalue19;

	@Column(name = "pb_value20", columnDefinition = "varchar(50)")
	private String pbvalue20;

	@Column(name = "pb_value21", columnDefinition = "varchar(50)")
	private String pbvalue21;

	@Column(name = "pb_value22", columnDefinition = "varchar(50)")
	private String pbvalue22;

	@Column(name = "pb_value23", columnDefinition = "varchar(50)")
	private String pbvalue23;

	@Column(name = "pb_value24", columnDefinition = "varchar(50)")
	private String pbvalue24;

	@Column(name = "pb_value25", columnDefinition = "varchar(50)")
	private String pbvalue25;

	@Column(name = "pb_value26", columnDefinition = "varchar(50)")
	private String pbvalue26;

	@Column(name = "pb_value27", columnDefinition = "varchar(50)")
	private String pbvalue27;

	@Column(name = "pb_value28", columnDefinition = "varchar(50)")
	private String pbvalue28;

	@Column(name = "pb_value29", columnDefinition = "varchar(50)")
	private String pbvalue29;

	@Column(name = "pb_value30", columnDefinition = "varchar(50)")
	private String pbvalue30;

	@Column(name = "pb_value31", columnDefinition = "varchar(50)")
	private String pbvalue31;

	@Column(name = "pb_value32", columnDefinition = "varchar(50)")
	private String pbvalue32;

	@Column(name = "pb_value33", columnDefinition = "varchar(50)")
	private String pbvalue33;

	@Column(name = "pb_value34", columnDefinition = "varchar(50)")
	private String pbvalue34;

	@Column(name = "pb_value35", columnDefinition = "varchar(50)")
	private String pbvalue35;

	@Column(name = "pb_value36", columnDefinition = "varchar(50)")
	private String pbvalue36;

	@Column(name = "pb_value37", columnDefinition = "varchar(50)")
	private String pbvalue37;

	@Column(name = "pb_value38", columnDefinition = "varchar(50)")
	private String pbvalue38;

	@Column(name = "pb_value39", columnDefinition = "varchar(50)")
	private String pbvalue39;

	@Column(name = "pb_value40", columnDefinition = "varchar(50)")
	private String pbvalue40;

	@Column(name = "pb_value41", columnDefinition = "varchar(50)")
	private String pbvalue41;

	@Column(name = "pb_value42", columnDefinition = "varchar(50)")
	private String pbvalue42;

	@Column(name = "pb_value43", columnDefinition = "varchar(50)")
	private String pbvalue43;

	@Column(name = "pb_value44", columnDefinition = "varchar(50)")
	private String pbvalue44;

	@Column(name = "pb_value45", columnDefinition = "varchar(50)")
	private String pbvalue45;

	@Column(name = "pb_value46", columnDefinition = "varchar(50)")
	private String pbvalue46;

	@Column(name = "pb_value47", columnDefinition = "varchar(50)")
	private String pbvalue47;

	@Column(name = "pb_value48", columnDefinition = "varchar(50)")
	private String pbvalue48;

	@Column(name = "pb_value49", columnDefinition = "varchar(50)")
	private String pbvalue49;

	@Column(name = "pb_value50", columnDefinition = "varchar(50)")
	private String pbvalue50;

	@Column(name = "pb_l_text", columnDefinition = "text default ''")
	private String pbltext;
	
	@Column(name = "pb_l_path", columnDefinition = "varchar(255) default ''")
	private String pblpath;

	/*
	 * @OneToOne(mappedBy = "productionBody") private ProductionHeader
	 * productionHeader;
	 */

	@ManyToOne(targetEntity = ProductionHeader.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "pb_ph_id")
	private ProductionHeader productionHeader;

	public static class PrimaryKey implements Serializable {

		private static final long serialVersionUID = 1L;
		private Integer sysver;
		private Integer pbid;

		public Integer getSysver() {
			return sysver;
		}

		public void setSysver(Integer sysver) {
			this.sysver = sysver;
		}

		public Integer getPbid() {
			return pbid;
		}

		public void setPbid(Integer pbid) {
			this.pbid = pbid;
		}
	}

	
	
	public Boolean getPbcheck() {
		return pbcheck;
	}

	public void setPbcheck(Boolean pbcheck) {
		this.pbcheck = pbcheck;
	}

	public String getPbschedule() {
		return pbschedule;
	}

	public void setPbschedule(String pbschedule) {
		this.pbschedule = pbschedule;
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

	public String getPbvalue01() {
		return pbvalue01;
	}

	public void setPbvalue01(String pbvalue01) {
		this.pbvalue01 = pbvalue01;
	}

	public String getPbvalue02() {
		return pbvalue02;
	}

	public void setPbvalue02(String pbvalue02) {
		this.pbvalue02 = pbvalue02;
	}

	public String getPbvalue03() {
		return pbvalue03;
	}

	public void setPbvalue03(String pbvalue03) {
		this.pbvalue03 = pbvalue03;
	}

	public String getPbvalue04() {
		return pbvalue04;
	}

	public void setPbvalue04(String pbvalue04) {
		this.pbvalue04 = pbvalue04;
	}

	public String getPbvalue05() {
		return pbvalue05;
	}

	public void setPbvalue05(String pbvalue05) {
		this.pbvalue05 = pbvalue05;
	}

	public String getPbvalue06() {
		return pbvalue06;
	}

	public void setPbvalue06(String pbvalue06) {
		this.pbvalue06 = pbvalue06;
	}

	public String getPbvalue07() {
		return pbvalue07;
	}

	public void setPbvalue07(String pbvalue07) {
		this.pbvalue07 = pbvalue07;
	}

	public String getPbvalue08() {
		return pbvalue08;
	}

	public void setPbvalue08(String pbvalue08) {
		this.pbvalue08 = pbvalue08;
	}

	public String getPbvalue09() {
		return pbvalue09;
	}

	public void setPbvalue09(String pbvalue09) {
		this.pbvalue09 = pbvalue09;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPbvalue10() {
		return pbvalue10;
	}

	public void setPbvalue10(String pbvalue10) {
		this.pbvalue10 = pbvalue10;
	}

	public String getPbvalue11() {
		return pbvalue11;
	}

	public void setPbvalue11(String pbvalue11) {
		this.pbvalue11 = pbvalue11;
	}

	public String getPbvalue12() {
		return pbvalue12;
	}

	public void setPbvalue12(String pbvalue12) {
		this.pbvalue12 = pbvalue12;
	}

	public String getPbvalue13() {
		return pbvalue13;
	}

	public void setPbvalue13(String pbvalue13) {
		this.pbvalue13 = pbvalue13;
	}

	public String getPbvalue14() {
		return pbvalue14;
	}

	public void setPbvalue14(String pbvalue14) {
		this.pbvalue14 = pbvalue14;
	}

	public String getPbvalue15() {
		return pbvalue15;
	}

	public void setPbvalue15(String pbvalue15) {
		this.pbvalue15 = pbvalue15;
	}

	public String getPbvalue16() {
		return pbvalue16;
	}

	public void setPbvalue16(String pbvalue16) {
		this.pbvalue16 = pbvalue16;
	}

	public String getPbvalue17() {
		return pbvalue17;
	}

	public void setPbvalue17(String pbvalue17) {
		this.pbvalue17 = pbvalue17;
	}

	public String getPbvalue18() {
		return pbvalue18;
	}

	public void setPbvalue18(String pbvalue18) {
		this.pbvalue18 = pbvalue18;
	}

	public String getPbvalue19() {
		return pbvalue19;
	}

	public void setPbvalue19(String pbvalue19) {
		this.pbvalue19 = pbvalue19;
	}

	public String getPbvalue20() {
		return pbvalue20;
	}

	public void setPbvalue20(String pbvalue20) {
		this.pbvalue20 = pbvalue20;
	}

	public String getPbvalue21() {
		return pbvalue21;
	}

	public void setPbvalue21(String pbvalue21) {
		this.pbvalue21 = pbvalue21;
	}

	public String getPbvalue22() {
		return pbvalue22;
	}

	public void setPbvalue22(String pbvalue22) {
		this.pbvalue22 = pbvalue22;
	}

	public String getPbvalue23() {
		return pbvalue23;
	}

	public void setPbvalue23(String pbvalue23) {
		this.pbvalue23 = pbvalue23;
	}

	public String getPbvalue24() {
		return pbvalue24;
	}

	public void setPbvalue24(String pbvalue24) {
		this.pbvalue24 = pbvalue24;
	}

	public String getPbvalue25() {
		return pbvalue25;
	}

	public void setPbvalue25(String pbvalue25) {
		this.pbvalue25 = pbvalue25;
	}

	public String getPbvalue26() {
		return pbvalue26;
	}

	public void setPbvalue26(String pbvalue26) {
		this.pbvalue26 = pbvalue26;
	}

	public String getPbvalue27() {
		return pbvalue27;
	}

	public void setPbvalue27(String pbvalue27) {
		this.pbvalue27 = pbvalue27;
	}

	public String getPbvalue28() {
		return pbvalue28;
	}

	public void setPbvalue28(String pbvalue28) {
		this.pbvalue28 = pbvalue28;
	}

	public String getPbvalue29() {
		return pbvalue29;
	}

	public void setPbvalue29(String pbvalue29) {
		this.pbvalue29 = pbvalue29;
	}

	public String getPbvalue30() {
		return pbvalue30;
	}

	public void setPbvalue30(String pbvalue30) {
		this.pbvalue30 = pbvalue30;
	}

	public String getPbvalue31() {
		return pbvalue31;
	}

	public void setPbvalue31(String pbvalue31) {
		this.pbvalue31 = pbvalue31;
	}

	public String getPbvalue32() {
		return pbvalue32;
	}

	public void setPbvalue32(String pbvalue32) {
		this.pbvalue32 = pbvalue32;
	}

	public String getPbvalue33() {
		return pbvalue33;
	}

	public void setPbvalue33(String pbvalue33) {
		this.pbvalue33 = pbvalue33;
	}

	public String getPbvalue34() {
		return pbvalue34;
	}

	public void setPbvalue34(String pbvalue34) {
		this.pbvalue34 = pbvalue34;
	}

	public String getPbvalue35() {
		return pbvalue35;
	}

	public void setPbvalue35(String pbvalue35) {
		this.pbvalue35 = pbvalue35;
	}

	public String getPbvalue36() {
		return pbvalue36;
	}

	public void setPbvalue36(String pbvalue36) {
		this.pbvalue36 = pbvalue36;
	}

	public String getPbvalue37() {
		return pbvalue37;
	}

	public void setPbvalue37(String pbvalue37) {
		this.pbvalue37 = pbvalue37;
	}

	public String getPbvalue38() {
		return pbvalue38;
	}

	public void setPbvalue38(String pbvalue38) {
		this.pbvalue38 = pbvalue38;
	}

	public String getPbvalue39() {
		return pbvalue39;
	}

	public void setPbvalue39(String pbvalue39) {
		this.pbvalue39 = pbvalue39;
	}

	public String getPbvalue40() {
		return pbvalue40;
	}

	public void setPbvalue40(String pbvalue40) {
		this.pbvalue40 = pbvalue40;
	}

	public String getPbvalue41() {
		return pbvalue41;
	}

	public void setPbvalue41(String pbvalue41) {
		this.pbvalue41 = pbvalue41;
	}

	public String getPbvalue42() {
		return pbvalue42;
	}

	public void setPbvalue42(String pbvalue42) {
		this.pbvalue42 = pbvalue42;
	}

	public String getPbvalue43() {
		return pbvalue43;
	}

	public void setPbvalue43(String pbvalue43) {
		this.pbvalue43 = pbvalue43;
	}

	public String getPbvalue44() {
		return pbvalue44;
	}

	public void setPbvalue44(String pbvalue44) {
		this.pbvalue44 = pbvalue44;
	}

	public String getPbvalue45() {
		return pbvalue45;
	}

	public void setPbvalue45(String pbvalue45) {
		this.pbvalue45 = pbvalue45;
	}

	public String getPbvalue46() {
		return pbvalue46;
	}

	public void setPbvalue46(String pbvalue46) {
		this.pbvalue46 = pbvalue46;
	}

	public String getPbvalue47() {
		return pbvalue47;
	}

	public void setPbvalue47(String pbvalue47) {
		this.pbvalue47 = pbvalue47;
	}

	public String getPbvalue48() {
		return pbvalue48;
	}

	public void setPbvalue48(String pbvalue48) {
		this.pbvalue48 = pbvalue48;
	}

	public String getPbvalue49() {
		return pbvalue49;
	}

	public void setPbvalue49(String pbvalue49) {
		this.pbvalue49 = pbvalue49;
	}

	public String getPbvalue50() {
		return pbvalue50;
	}

	public void setPbvalue50(String pbvalue50) {
		this.pbvalue50 = pbvalue50;
	}

	

	public String getPbltext() {
		return pbltext;
	}

	public void setPbltext(String pbltext) {
		this.pbltext = pbltext;
	}

	public String getPblpath() {
		return pblpath;
	}

	public void setPblpath(String pblpath) {
		this.pblpath = pblpath;
	}

	public ProductionHeader getProductionHeader() {
		return productionHeader;
	}

	public void setProductionHeader(ProductionHeader productionHeader) {
		this.productionHeader = productionHeader;
	}

}
