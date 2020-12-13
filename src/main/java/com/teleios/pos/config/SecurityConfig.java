package com.teleios.pos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/javax.faces.resource/**", "/resources/**").permitAll().anyRequest()
				.authenticated();

		http.formLogin().loginPage("/login.xhtml").permitAll().failureUrl("/login.xhtml?error=true");
		http.formLogin().defaultSuccessUrl("/index.xhtml", true);
		// logout
		http.logout().logoutSuccessUrl("/login.xhtml");

		http.csrf().disable();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("Harith").password("{noop}1234").roles("ADMIN").and().withUser("Dilan")
				.password("{noop}1234").roles("ADMIN").and().withUser("kamal").password("{noop}1234").roles("USER");
	}
}
