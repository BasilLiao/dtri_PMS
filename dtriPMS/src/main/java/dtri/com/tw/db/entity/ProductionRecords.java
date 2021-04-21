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
 * @see 系統設定<br>
 *      sc_id : ID<br>
 *      sc_name : 名稱<br>
 *      sc_g_id : 群組ID<br>
 *      sc_g_name : 群組名稱<br>
 *      sc_value : 設定參數<br>
 */
@Entity
@Table(name = "production_records")
@EntityListeners(AuditingEntityListener.class)
public class ProductionRecords {
	public ProductionRecords() {
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
	@Column(name = "pr_id")
	private String prid;

	@Column(name = "pr_order_id", nullable = false, columnDefinition = "varchar(60) default ''")
	private String prorderid;

	@Column(name = "pr_c_name", nullable = false, columnDefinition = "varchar(60) default ''")
	private String prcname;

	@Column(name = "pr_p_quantity", nullable = false, columnDefinition = "int default 0")
	private Integer prpquantity;

	@Column(name = "pr_p_model", nullable = false, columnDefinition = "varchar(50) default ''")
	private String prpmodel;

	@Column(name = "pr_bom_id", nullable = false, columnDefinition = "varchar(50) default ''")
	private String prbomid;

	@Column(name = "pr_v_motherboard", nullable = false, columnDefinition = "varchar(50) default ''")
	private String prvmotherboard;

	@Column(name = "pr_c_from", nullable = false, columnDefinition = "varchar(50) default ''")
	private String prcfrom;

	@Column(name = "pr_b_item", nullable = false, columnDefinition = "text default ''")
	private String prbitem;

	@Column(name = "pr_s_item", nullable = false, columnDefinition = "text default ''")
	private String prsitem;

	@Column(name = "pr_s_sn", nullable = false, columnDefinition = "varchar(50) default ''")
	private String prssn;

	@Column(name = "pr_e_sn", nullable = false, columnDefinition = "varchar(50) default ''")
	private String presn;

}
