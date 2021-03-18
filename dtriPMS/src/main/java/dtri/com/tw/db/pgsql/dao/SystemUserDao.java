package dtri.com.tw.db.pgsql.dao;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dtri.com.tw.db.entity.SystemUser;

public interface SystemUserDao extends JpaRepository<SystemUser, Long> {

	//帳號查詢
	SystemUser findBySuaccount(String suaccount);

	//查詢全部
	ArrayList<SystemUser> findAll();

	//@Query註解裡面寫JPQL語句,定義查詢
	@Query(nativeQuery = false,value = " SELECT i FROM SystemUser i WHERE su_id = ?1")
	SystemUser readId(Integer id);
}