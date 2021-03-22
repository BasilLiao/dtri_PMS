package dtri.com.tw.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class LoginSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	LoginUserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 這個method可以設定那些路由要經過身分權限的審核，或是login、logout路由特別設定等地方，因此這邊也是設定身分權限的關鍵地方。
		// authorizeRequests()配置路徑攔截，表明路徑訪問所對應的權限，角色，認證信息。
		// formLogin()對應表單認證相關的配置
		// logout()對應了註銷相關的配置
		// httpBasic()可以配置basic登錄
		// (permitAll = 全部允許) (authenticated = 登入後可訪問) (anyRequest = 所有請求)

		// Restful API ( GET -> 訪問/查詢 ) ( POST -> 新增 ) ( PUT -> 更新) ( DELETE -> 刪除 )
		// 下列-權限驗證
		String s_u = "system_user.basil";
		String s_p = "system_permission.basil";
		String s_g = "system_group.basil";
		String i_x = "index.basil";
		http.authorizeRequests()
				// thirdparty && img 資料夾靜態資料可 直接 存取 (預設皆有 訪問權限)
				.antMatchers(HttpMethod.GET, "/thirdparty/**", "/img/**", "/login.basil", "/login.html").permitAll()
				// ----請求-index-(訪問)----
				.antMatchers(HttpMethod.POST, "ajax/" + i_x).access(hasAuthority(i_x, ""))

				// ----請求-system_permission-(訪問) ----
				.antMatchers(HttpMethod.POST, "ajax/" + s_p).access(hasAuthority(s_p, ""))
				// (查詢)
				.antMatchers(HttpMethod.GET, "ajax/" + s_p+".AR").access(hasAuthority(s_p, "AR"))
				// (新增)
				.antMatchers(HttpMethod.POST, "ajax/" + s_p+".AR_AC").access(hasAuthority(s_p, "AR_AC"))
				// (修改)
				.antMatchers(HttpMethod.PUT, "ajax/" + s_p+".AR_AU").access(hasAuthority(s_p, "AR_AU"))
				// (移除)
				.antMatchers(HttpMethod.DELETE, "ajax/" + s_p+".AR_AD").access(hasAuthority(s_p, "AR_AD"))

				// ----請求-sys_user-(訪問) ----
				.antMatchers(HttpMethod.GET, "ajax/" + s_u).access(hasAuthority(s_u, ""))
				// (查詢)
				.antMatchers(HttpMethod.GET, "ajax/" + s_u).access(hasAuthority(s_u, "AR"))
				// (新增)
				.antMatchers(HttpMethod.POST, "ajax/" + s_u).access(hasAuthority(s_u, "AR_AC"))
				// (修改)
				.antMatchers(HttpMethod.PUT, "ajax/" + s_u).access(hasAuthority(s_u, "AR_AU"))
				// (移除)
				.antMatchers(HttpMethod.DELETE, "ajax/" + s_u).access(hasAuthority(s_u, "AR_AD"))

				// ----請求-sys_group-(訪問) ----
				.antMatchers(HttpMethod.GET, "ajax/" + s_g).access(hasAuthority(s_g, ""))
				// (查詢)
				.antMatchers(HttpMethod.GET, "ajax/" + s_g).access(hasAuthority(s_g, "AR"))
				// (新增)
				.antMatchers(HttpMethod.POST, "ajax/" + s_g).access(hasAuthority(s_g, "AR_AC"))
				// (修改)
				.antMatchers(HttpMethod.PUT, "ajax/" + s_g).access(hasAuthority(s_g, "AR_AU"))
				// (移除)
				.antMatchers(HttpMethod.DELETE, "ajax/" + s_g).access(hasAuthority(s_g, "AR_AD"))

				// 請求需要檢驗-全部請求
				.anyRequest().authenticated();
		// 下列-登入位置
		http.formLogin()
				// 登入-預設登入頁面 預設帳密參數為(.usernameParameter(username).passwordParameter(password))
				.loginPage("/login.basil?status")
				// 登入-程序對象
				.loginProcessingUrl("/index.basil")
				// 登入-成功
				.successForwardUrl("/index.basil")
				// 登入-失敗
				.failureUrl("/login.basil?status=account or password incorrect!");
		// 下列-登出位置
		http.logout()
				// 登出-預設登入頁面
				.logoutUrl("/logout.basil")
				// 登出-程序對象
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
				// 登出-後轉跳
				.logoutSuccessUrl("/login.basil?status=You are exit!")
				// 登出-移除Session
				.invalidateHttpSession(true).clearAuthentication(true)
				// 登出-移除Cookies
				.deleteCookies();
		// 關閉CSRF跨域
		// 在 Thymeleaf 或 JSP 中，Token 名稱與值可分別使用 ${_csrf.parameterName} 與 ${_csrf.token}
		// 來取得，發送請求時，必須得包含這個 Token，否則就會被拒絕請求。
		http.csrf().disable();

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		/***
		 * 測試兩組帳號-驗證資訊存放於記憶體-內存幫我們存放使用者的帳號及密碼 <br>
		 * PasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		 * auth.inMemoryAuthentication().passwordEncoder(pwdEncoder).withUser("admin").password(pwdEncoder.encode("admin")).roles("ADMIN",
		 * "MEMBER");
		 * auth.inMemoryAuthentication().passwordEncoder(pwdEncoder).withUser("caterpillar")
		 * .password(pwdEncoder.encode("caterpillar")).roles("MEMBER");
		 */

		auth.userDetailsService(userDetailsService);
	}

	/** 權限-規則-群組歸類 **/
	private String hasAuthority(String cell_who, String action_do) {
		// (sg_permission[特殊3(512),特殊2(256),特殊1(128),訪問(64),下載(32),上傳(16),新增(8),修改(4),刪除(2),查詢(1)])
		// 訪問
		String hasAuthority = "hasAuthority" + "('" + action_do + ".0001000000')";
		// CRUD
		if (action_do.contains("S3")) {
			hasAuthority += " and hasAuthority" + "('" + action_do + ".1000000000')"; // 特殊3
		}
		if (action_do.contains("S2")) {
			hasAuthority += " and hasAuthority" + "('" + action_do + ".0100000000')"; // 特殊2
		}
		if (action_do.contains("S1")) {
			hasAuthority += " and hasAuthority" + "('" + action_do + ".0010000000')"; // 特殊1
		}
		if (action_do.contains("FD")) {
			hasAuthority += " and hasAuthority" + "('" + action_do + ".0000100000')"; // 訪問
		}
		if (action_do.contains("FU")) {
			hasAuthority += " and hasAuthority" + "('" + action_do + ".0000010000')"; // 上載
		}
		if (action_do.contains("AC")) {
			hasAuthority += " and hasAuthority" + "('" + action_do + ".0000001000')"; // 新增
		}
		if (action_do.contains("AU")) {
			hasAuthority += " and hasAuthority" + "('" + action_do + ".0000000100')"; // 修改
		}
		if (action_do.contains("AD")) {
			hasAuthority += " and hasAuthority" + "('" + action_do + ".0000000010')"; // 移除
		}
		if (action_do.contains("AR")) {
			hasAuthority += " and hasAuthority" + "('" + action_do + ".0000000001')"; // 查詢
		}
		return hasAuthority;
	}
}
