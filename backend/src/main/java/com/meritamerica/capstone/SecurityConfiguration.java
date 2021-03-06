package com.meritamerica.capstone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.meritamerica.capstone.filters.JwtRequestFilter;
import com.meritamerica.capstone.services.MyUserDetailsService;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService);
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		/*
		 * //code to test data base by temporarily
		 * 
		 * httpSecurity.csrf().disable().authorizeRequests()
		 * .antMatchers("/**").permitAll().anyRequest() .authenticated()
		 * .and().exceptionHandling().and()
		 * .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		 * httpSecurity.headers().frameOptions().disable();
		 */
		httpSecurity.csrf().disable().authorizeRequests().antMatchers("/AccountHolders/**").hasAuthority("admin")
				.antMatchers("/Me/**", "/Me").hasAuthority("AccountHolder").antMatchers(HttpMethod.POST, "/CDOfferings")
				.hasAuthority("admin").antMatchers(HttpMethod.GET, "/CDOfferings")
				.hasAnyAuthority("admin", "AccountHolder").antMatchers("/authenticate/createUser").hasAuthority("admin")
				.antMatchers("/authenticate", "/authenticate/").permitAll().anyRequest().authenticated().and()
				.exceptionHandling().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}