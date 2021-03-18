package dtri.com.tw.db.pgsql.dao;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import dtri.com.tw.db.entity.SystemGroup;

public interface SystemGroupDao extends JpaRepository<SystemGroup, Long> {

	// 查詢群組
	ArrayList<SystemGroup> findBySggid(Integer sggid);

	// 查詢全部
	ArrayList<SystemGroup> findAll();

}