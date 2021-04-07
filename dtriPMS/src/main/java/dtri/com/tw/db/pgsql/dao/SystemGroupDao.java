package dtri.com.tw.db.pgsql.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dtri.com.tw.db.entity.SystemGroup;

public interface SystemGroupDao extends JpaRepository<SystemGroup, Long> {

	// 查詢群組
	List<SystemGroup> findBySggidOrderBySgidAscSyssortAsc(Integer sggid);

	// 查詢ID
	List<SystemGroup> findBySgidOrderBySgidAscSyssortAsc(Integer sgid);
		
	// 查詢群組名稱
	@Query("SELECT c FROM SystemGroup c "
			+ "WHERE  (:sgname is null or c.sgname = :sgname) and ( c.sysstatus = :sysstatus ) "
			+ "order by c.sggid asc, c.sgid asc")
	List<SystemGroup> findAllBySystemGroup(String sgname, Integer sysstatus, Pageable p);

	// 查詢群組分頁
	List<SystemGroup> findAllByOrderBySgidAscSggidAsc(Pageable p);

	// 查詢全部
	ArrayList<SystemGroup> findAll();

	// 查詢是否重複 群組
	@Query("SELECT c FROM SystemGroup c " + "WHERE  (c.sgname = :sgname) " + "order by c.sgid desc")
	ArrayList<SystemGroup> findAllByGroupTop1(@Param("sgname") String spgname, Pageable pageable);

	// 取得最新G_ID
	@Query("SELECT c FROM SystemGroup c order by c.sgid desc")
	ArrayList<SystemGroup> findAllByTop1(Pageable pageable);

}