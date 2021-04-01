package dtri.com.tw.db.pgsql.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dtri.com.tw.db.entity.SystemGroup;

public interface SystemGroupDao extends JpaRepository<SystemGroup, Long> {

	// 查詢群組
	List<SystemGroup> findBySggidOrderBySgidAscSyssortAsc(Integer sggid);

	// 查詢全部
	ArrayList<SystemGroup> findAll();

}