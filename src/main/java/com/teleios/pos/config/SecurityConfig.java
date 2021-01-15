package com.teleios.pos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/javax.faces.resource/**", "/resources/**", "/error/403_err.xhtml/**")
				.permitAll().antMatchers("/settings/**").hasRole("ADMIN").anyRequest().authenticated().and()
				.exceptionHandling().accessDeniedHandler(accessDeniedHandler()).and().formLogin()
				.loginPage("/login.xhtml").permitAll().failureUrl("/login.xhtml?error=true")
				.defaultSuccessUrl("/index.xhtml", true).and().logout().invalidateHttpSession(true)
				.clearAuthentication(true).logoutSuccessUrl("/login.xhtml").deleteCookies("JSESSIONID").permitAll();

		http.csrf().disable();

	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new TeleiosAccessDeniedHandler();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("Harith").password("{noop}1234").roles("ADMIN").and().withUser("Dilan")
				.password("{noop}1234").roles("ADMIN").and().withUser("kamal").password("{noop}1234").roles("USER");
	}

	@Override
	public void init(WebSecurity web) throws Exception {
		super.init(web);

		web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
	}

	@Bean
	public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
		return new DefaultHttpFirewall();
	}
}
