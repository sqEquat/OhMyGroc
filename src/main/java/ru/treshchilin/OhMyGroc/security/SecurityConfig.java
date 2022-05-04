package ru.treshchilin.OhMyGroc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ru.treshchilin.OhMyGroc.security.filter.JwtAuthenticationFilter;
import ru.treshchilin.OhMyGroc.security.filter.JwtAuthorizationFilter;
import ru.treshchilin.OhMyGroc.security.jwts.JwtProvider;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	
	@Autowired
	public SecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
		this.jwtProvider = jwtProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManagerBean(), jwtProvider);
		jwtAuthenticationFilter.setFilterProcessesUrl("/api/v2/login");
		
		http.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeHttpRequests()
			.antMatchers("/api/v2/login/**").permitAll()
			.antMatchers("/api/v2/client/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENT")
			.antMatchers("/api/v2/admin/**").hasAuthority("ROLE_ADMIN")
			.anyRequest().authenticated()
			.and()		
			.addFilter(jwtAuthenticationFilter)
			.addFilterBefore(new JwtAuthorizationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.POST, "/api/v2/register");
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
