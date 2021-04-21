package dtri.com.tw.db.pgsql.dao;

import java.util.ArrayList;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dtri.com.tw.db.entity.SystemConfig;

public interface ProductionRecordsDao extends JpaRepository<SystemConfig, Long> {

	// 查詢全部
	ArrayList<SystemConfig> findAll();

	// 查詢一部分
	@Query("SELECT c FROM SystemConfig c "
			+ "WHERE (:scname is null or c.scname LIKE %:scname% ) and (:scgname is null or c.scgname LIKE %:scgname% ) and ( c.sysstatus = :sysstatus )  "
			+ "order by c.scgid asc,c.sysmdate desc")
	ArrayList<SystemConfig> findAllByConfig(@Param("scname") String scname, @Param("scgname") String scgname, @Param("sysstatus") Integer sysstatus,
			Pageable pageable);

	// 查詢是否重複 群組
	@Query("SELECT c FROM SystemConfig c " + "WHERE  (c.scgname = :scgname) " + "order by c.scgid desc")
	ArrayList<SystemConfig> findAllByConfigGroupTop1(@Param("scgname") String scgname, Pageable pageable);

	// 取得最新G_ID
	@Query("SELECT c FROM SystemConfig c order by c.scgid desc")
	ArrayList<SystemConfig> findAllByTop1(Pageable pageable);
	
	//取得ID
	@Query(value = "SELECT CURRVAL('production_config_seq')", nativeQuery = true)
    Integer getProduction_config_seq();



	//delete
	Long deleteByScidAndSysheader(Integer id, Boolean sysheader);
}