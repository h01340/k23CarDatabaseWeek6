package k23SB3.CarDatabaseWeek6;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import k23SB3.CarDatabaseWeek6.service.UserDetailServiceImpl;

@Configuration
//@EnableWebSecurity //SB2.7
//@EnableGlobalMethodSecurity(prePostEnabled = true) //SB2.7
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
	@Autowired
	private UserDetailServiceImpl userDetailsService;
	
	private static final AntPathRequestMatcher[] WHITE_LIST_URLS = {
            new AntPathRequestMatcher("/api/**"),
            new AntPathRequestMatcher("/h2-console/**"),
            new AntPathRequestMatcher("/cars/**")
    };
	
	private static final AntPathRequestMatcher[] ADMIN_LIST_URLS = {
            new AntPathRequestMatcher("/admin/**"),
            new AntPathRequestMatcher("/owner/**")
    };
	
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests().requestMatchers(ADMIN_LIST_URLS).hasAuthority("ADMIN")
		.and()
		.authorizeHttpRequests().requestMatchers(WHITE_LIST_URLS).permitAll()
		.and()
		.authorizeHttpRequests().anyRequest().authenticated()
		//for h2 console
		.and()
		.headers().frameOptions().disable()			
		.and()
		.formLogin().defaultSuccessUrl("/index", true)
		.and()
		.logout().permitAll();
		
		http.cors().and().csrf().disable();
		
		//Ohjelmistoprojekti1:n matskuun tämä
		//.addFilterAfter(new CsrfLoggerFilter(), CsrfFilter.class); 
		
		return http.build();

		
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}
}
