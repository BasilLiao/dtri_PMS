package dtri.com.tw.db.pgsql.dao;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import dtri.com.tw.db.entity.SystemPermission;

public interface SystemPermissionDao extends JpaRepository<SystemPermission, Long> {

	// 查詢群組權限
	ArrayList<SystemPermission> findBySpgid(Integer spgid);

	// 查詢全部
	ArrayList<SystemPermission> findAllByOrderBySpgidAscSyssortAsc();

}