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
public class LoginSecutity extends WebSecurityConfigurerAdapter {

	@Autowired
	LoginUserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 這個method可以設定那些路由要經過身分權限的審核，或是login、logout路由特別設定等地方，因此這邊也是設定身分權限的關鍵地方。
		// authorizeRequests()配置路徑攔截，表明路徑訪問所對應的權限，角色，認證信息。
		// formLogin()對應表單認證相關的配置
		// logout()對應了註銷相關的配置
		// httpBasic()可以配置basic登錄
		// (permitAll = 全部允許)
		// (authenticated = 登入後可訪問)
		// (anyRequest = 所有請求)

		// (sg_permission[特殊3(512),特殊2(256),特殊1(128),訪問(64),下載(32),上傳(16),新增(8),修改(4),刪除(2),查詢(1)])
		// 下列-權限驗證
		http.authorizeRequests()
				// thirdparty && img 資料夾靜態資料可 直接 存取
				.antMatchers(HttpMethod.GET, "/thirdparty/**", "/img/**", "/login.basil", "/login.html").permitAll()
				// 請求-index-(訪問+查詢)
				.antMatchers(HttpMethod.PATCH, "ajax/index.basil")
				.access("hasAuthority('index.basil.0001000000') and hasAuthority('index.basil.0000000001')")
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
}
