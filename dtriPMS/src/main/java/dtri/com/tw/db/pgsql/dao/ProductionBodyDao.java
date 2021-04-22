package dtri.com.tw.db.pgsql.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dtri.com.tw.db.entity.ProductionBody;
import dtri.com.tw.db.entity.ProductionHeader;

public interface ProductionBodyDao extends JpaRepository<ProductionBody, Long> {
	// 查詢 標籤
	List<ProductionBody> findAllByPbid(Integer id);

	// 取得下當前筆ID
	@Query(value = "SELECT CURRVAL('production_body_seq')", nativeQuery = true)
	Integer getProductionBodySeq();

	// 查詢SN重複
	List<ProductionBody> findAllByPbsn(String pbsn);

	// 查詢一部分_Body
	@Query("SELECT b FROM ProductionBody b join b.productionHeader h WHERE "//
			+ "(:phmodel is null or h.phmodel LIKE %:phmodel% ) and "//
			+ "(:phprid is null or h.phprid LIKE %:phprid% ) and "//
			+ "( h.sysstatus = :sysstatus ) and "//
			+ "(coalesce(:pbid, null) is null or b.pbid IN :pbid ) and "// coalesce 回傳非NULL值
			+ "(b.pbid!=0) and (b.sysheader!=true) "//
			+ " order by b.sysmdate desc ,b.sysheader desc")
	List<ProductionBody> findAllByProductionBody(@Param("phmodel") String phmodel, @Param("phprid") String phprid,
			@Param("sysstatus") Integer sysstatus, @Param("pbid") List<Integer> pbid, Pageable pageable);

	// 查詢一部分_Body By Check
	@Query("SELECT b FROM ProductionBody b join b.productionHeader h WHERE "//
			+ "(:phmodel is null or h.phmodel LIKE %:phmodel% ) and "//
			+ "(:phprid is null or h.phprid LIKE %:phprid% ) and "//
			+ "( h.sysstatus = :sysstatus ) and "//
			+ "(coalesce(:pbid, null) is null or b.pbid IN :pbid ) and "// coalesce 回傳非NULL值
			+ "(b.pbid!=0) and (b.sysheader!=true) and  (b.pbcheck=:pbcheck)"//
			+ " order by b.sysmdate desc ,b.sysheader desc")
	List<ProductionBody> findAllByProductionBody(@Param("phmodel") String phmodel, @Param("phprid") String phprid,
			@Param("sysstatus") Integer sysstatus, @Param("pbid") List<Integer> pbid, @Param("pbcheck") Boolean pbcheck, Pageable pageable);

	// 移除By 群組
	Long deleteByProductionHeader(ProductionHeader id);

	// 移除單一SN
	Long deleteByPbid(Integer id);

}