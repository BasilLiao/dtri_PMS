package dtri.com.tw.db.pgsql.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dtri.com.tw.db.entity.ProductionHeader;

@Repository
public interface ProductionHeaderDao extends JpaRepository<ProductionHeader, Long> {

	// 查詢全部
	List<ProductionHeader> findAll();

	// 查詢一部分_Header
	@Query("SELECT h FROM ProductionHeader h join h.productionBody b WHERE "//
			+ "(:phmodel is null or h.phmodel LIKE %:phmodel% ) and "//
			+ "(:phprid is null or h.phprid LIKE %:phprid% ) and "//
			+ "( h.sysstatus = :sysstatus ) and "//
			+ "(coalesce(:pbphid, null) is null or h.phid IN :pbphid ) and "//coalesce 回傳非NULL值
			+ "(h.phid != 0) and "//
			+ "(b.sysheader = true)"//
			+ " order by h.sysmdate desc ,b.sysheader desc")
	List<ProductionHeader> findAllByProductionHeader(@Param("phmodel") String phmodel, @Param("phprid") String phprid,
			@Param("sysstatus") Integer sysstatus,@Param("pbphid")List<Integer> pbphid, Pageable pageable);

	// 取得下一筆ID
	@Query(value = "SELECT CURRVAL('production_header_seq')", nativeQuery = true)
	Integer getProductionHeaderSeq();
	
	//查詢重複製令
	List<ProductionHeader> findAllByPhprid(String phprid);

	// delete(header)
	Long deleteByPhidAndSysheader(Integer id, Boolean sysheader);
}