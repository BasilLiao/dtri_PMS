package dtri.com.tw.login;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import dtri.com.tw.db.entity.SystemGroup;
import dtri.com.tw.db.entity.SystemUser;

public class LoginUserDetails implements UserDetails {
	/**
	 * 登入使用者主要資料
	 */
	private static final long serialVersionUID = 1L;
	private SystemUser user;
	private ArrayList<SystemGroup> group;
	private Collection<? extends GrantedAuthority> authorities;

	public LoginUserDetails(SystemUser user, ArrayList<SystemGroup> group,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.user = user;
		this.group = group;
		this.authorities = authorities;
	}

	public ArrayList<SystemGroup> getSystemGroup() {
		return group;
	}

	public SystemUser getSystemUser() {
		return user;
	}

	/**
	 * 權限清單 <br>
	 * 規則: <br>
	 * 單元+請求類型(CRUD權限):index.basil.0100000000<br>
	 * 單元+請求類型(CRUD權限):index.basil.0010000000<br>
	 * 
	 **/
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	/**
	 * 注意密碼加密"{noop}" +
	 * 
	 * PasswordEncoder
	 **/
	@Override
	public String getPassword() {
		return "{noop}" + user.getSupassword();
	}

	@Override
	public String getUsername() {
		return user.getSuaccount();
	}

	/** 指示用户的帐户是否已过期 **/
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/** 指示用户是锁定还是解锁 **/
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/** 指示用户的凭据（密码）是否已过期 **/
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/** 指示用户是启用还是禁用 **/
	@Override
	public boolean isEnabled() {
		boolean check = user.getSysstatus() == 0;
		return check;
	}

	/** 顯示全部 **/
	@Override
	public String toString() {
		return "MyUserDetails [id=" + user.getSuid() + ", useraccount=" + user.getSuaccount() + ", password="
				+ user.getSupassword() + ", enabled=" + (user.getSysstatus() == 0) + ", authorities=" + authorities
				+ "]";
	}
}
