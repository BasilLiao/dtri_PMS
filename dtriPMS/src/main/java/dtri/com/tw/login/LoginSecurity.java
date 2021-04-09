package dtri.com.tw.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
		String system_con = "/ajax/system_config.basil";
		String system_per = "/ajax/system_permission.basil";
		String system_gro = "/ajax/system_group.basil";
		String system_use = "/ajax/system_user.basil";
		http.authorizeRequests()
				// thirdparty && img 資料夾靜態資料可 直接 存取 (預設皆有 訪問權限 資料可[匿名]存取)
				.antMatchers(HttpMethod.GET, "/thirdparty/**", "/img/**", "/login.basil", "/login.html").permitAll()
				// ----請求-index-(訪問)----
				.antMatchers(HttpMethod.POST, "/ajax/index.basil").hasAuthority(actionRole("index.basil", ""))

				// ----請求-system_config-(訪問) ----
				.antMatchers(HttpMethod.POST, system_con).hasAuthority(actionRole(system_con, ""))
				// (查詢)
				.antMatchers(HttpMethod.POST, system_con + ".AR").hasAuthority(actionRole(system_con, "AR"))
				// (新增)
				.antMatchers(HttpMethod.POST, system_con + ".AC").hasAuthority(actionRole(system_con, "AC"))
				// (修改)
				.antMatchers(HttpMethod.PUT, system_con + ".AU").hasAuthority(actionRole(system_con, "AU"))
				// (移除)
				.antMatchers(HttpMethod.DELETE, system_con + ".AD").hasAuthority(actionRole(system_con, "AD"))

				// ----請求-system_permission-(訪問) ----
				.antMatchers(HttpMethod.POST, system_per).hasAuthority(actionRole(system_per, ""))
				// (查詢)
				.antMatchers(HttpMethod.POST, system_per + ".AR").hasAuthority(actionRole(system_per, "AR"))
				// (新增)
				.antMatchers(HttpMethod.POST, system_per + ".AC").hasAuthority(actionRole(system_per, "AC"))
				// (修改)
				.antMatchers(HttpMethod.PUT, system_per + ".AU").hasAuthority(actionRole(system_per, "AU"))
				// (移除)
				.antMatchers(HttpMethod.DELETE, system_per + ".AD").hasAuthority(actionRole(system_per, "AD"))

				// ----請求-sys_group-(訪問) ----
				.antMatchers(HttpMethod.POST, system_gro).hasAuthority(actionRole(system_gro, ""))
				// (查詢)
				.antMatchers(HttpMethod.POST, system_gro + ".AR").hasAuthority(actionRole(system_gro, "AR"))
				// (新增)
				.antMatchers(HttpMethod.POST, system_gro + ".AC").hasAuthority(actionRole(system_gro, "AC"))
				// (修改)
				.antMatchers(HttpMethod.PUT, system_gro + ".AU").hasAuthority(actionRole(system_gro, "AU"))
				// (移除)
				.antMatchers(HttpMethod.DELETE, system_gro + ".AD").hasAuthority(actionRole(system_gro, "AD"))

				// ----請求-sys_user-(訪問) ----
				.antMatchers(HttpMethod.POST, system_use).hasAuthority(actionRole(system_use, ""))
				// (查詢)
				.antMatchers(HttpMethod.POST, system_use + ".AR").hasAuthority(actionRole(system_use, "AR"))
				// (新增)
				.antMatchers(HttpMethod.POST, system_use + ".AC").hasAuthority(actionRole(system_use, "AC"))
				// (修改)
				.antMatchers(HttpMethod.PUT, system_use + ".AU").hasAuthority(actionRole(system_use, "AU"))
				// (移除)
				.antMatchers(HttpMethod.DELETE, system_use + ".AD").hasAuthority(actionRole(system_use, "AD"))

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

		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}

	/** 權限-規則-群組歸類 **/
	private String actionRole(String cell_who, String action_do) {
		// (sg_permission[特殊3(512),特殊2(256),特殊1(128),訪問(64),下載(32),上傳(16),新增(8),修改(4),刪除(2),查詢(1)])
		// 訪問
		String cell_role = cell_who.replace(".", "_").replace("/ajax/", "");
		String hasAuthority = cell_role + "_0001000000";
		// CRUD
		switch (action_do) {
		case "S3":
			hasAuthority = cell_role + "_1000000000"; // 特殊3
			break;
		case "S2":
			hasAuthority = cell_role + "_0100000000"; // 特殊2
			break;
		case "S1":
			hasAuthority = cell_role + "_0010000000"; // 特殊1
			break;
		case "FD":
			hasAuthority = cell_role + "_0000100000"; // 下載
			break;
		case "FU":
			hasAuthority = cell_role + "_0000010000"; // 上載
			break;
		case "AC":
			hasAuthority = cell_role + "_0000001000"; // 新增
			break;
		case "AU":
			hasAuthority = cell_role + "_0000000100"; // 修改
			break;
		case "AD":
			hasAuthority = cell_role + "_0000000010"; // 移除
			break;
		case "AR":
			hasAuthority = cell_role + "_0000000001"; // 查詢
			break;
		default:
			break;
		}
		System.out.println(cell_who + " " + hasAuthority);
		return hasAuthority;
	}
}
