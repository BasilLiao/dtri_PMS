package dtri.com.tw.db.pgsql.dao;

import java.util.ArrayList;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dtri.com.tw.db.entity.WorkstationItem;

public interface WorkstationItemDao extends JpaRepository<WorkstationItem, Long> {

	// 查詢全部
	ArrayList<WorkstationItem> findAll();

	// 查詢一部分
	@Query("SELECT c FROM WorkstationItem c " //
			+ "WHERE (:wipbcell is null or c.wipbcell LIKE %:wipbcell% ) and "//
			+ "(:wipbvalue is null or c.wipbvalue LIKE %:wipbvalue% ) and "//
			+ "( c.sysstatus = :sysstatus )  "//
			+ "order by c.wiid asc,c.sysmdate desc")
	ArrayList<WorkstationItem> findAllByWorkstationItem(@Param("wipbcell") String wipbcell, @Param("wipbvalue") String wipbvalue,
			@Param("sysstatus") Integer sysstatus, Pageable pageable);

	// delete
	Long deleteByWiidAndSysheader(Integer id, Boolean sysheader);
}