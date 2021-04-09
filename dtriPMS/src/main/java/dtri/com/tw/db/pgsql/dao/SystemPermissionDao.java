package dtri.com.tw.db.pgsql.dao;

import java.util.ArrayList;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dtri.com.tw.db.entity.SystemPermission;

@Repository
public interface SystemPermissionDao extends JpaRepository<SystemPermission, Long> {

	// 查詢群組權限
	ArrayList<SystemPermission> findBySpgid(Integer spgid);

	// 查詢全部
	ArrayList<SystemPermission> findAllByOrderBySpgidAscSpidAsc(Pageable pageable);

	// 查詢一部分
	@Query("SELECT c FROM SystemPermission c "
			+ "WHERE (:spname is null or c.spname LIKE %:spname% ) and (:spgname is null or c.spgname LIKE %:spgname% ) and ( c.sysstatus = :sysstatus )  "
			+ "order by c.spgid asc,c.sysmdate desc")
	ArrayList<SystemPermission> findAllByPermission(@Param("spname") String spname, @Param("spgname") String spgname,
			@Param("sysstatus") Integer sysstatus, Pageable pageable);

	// 查詢是否重複 群組
	@Query("SELECT c FROM SystemPermission c "
			+ "WHERE  (c.spgname = :spgname) "
			+ "order by c.spgid desc")
	ArrayList<SystemPermission> findAllByPermissionGroupTop1(@Param("spgname") String spgname,Pageable pageable);

	// 取得最新G_ID
	@Query("SELECT c FROM SystemPermission c order by c.spgid desc")
	ArrayList<SystemPermission> findAllByTop1(Pageable pageable);

	//delete
	Long deleteBySpidAndSysheader(Integer id, Boolean sysheader);
}