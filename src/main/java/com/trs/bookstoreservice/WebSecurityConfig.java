package com.trs.bookstoreservice;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.trs.bookstoreservice.security.JwtAuthenticationEntryPoint;
import com.trs.bookstoreservice.security.JwtAuthenticationFilter;
import com.trs.bookstoreservice.services.UserService;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Value("${jwt.secret}")
	private String jwtSecret;
	
	@Autowired
	private UserService userService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		JwtAuthenticationFilter jwtAuthFilter = new JwtAuthenticationFilter(authenticationManager());
		jwtAuthFilter.setJwtSecret(jwtSecret);
		jwtAuthFilter.configureDependencies(userService);
		
		http
			.antMatcher("/api/**")
			.cors()
			.and()
			.csrf().disable()
			.authorizeRequests()
			.antMatchers(HttpMethod.POST, "/api/*").permitAll()
			.antMatchers(HttpMethod.GET, "swagger-ui.html").permitAll()
			.antMatchers(HttpMethod.GET, "webjars/springfox-swagger-ui**").permitAll()
			.antMatchers("/api/**").authenticated()
			.anyRequest().permitAll()
			.and()
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling().authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
			.and()
			.httpBasic().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Collections.singletonList("*"));
		config.setAllowedMethods(Collections.singletonList("*"));
		config.setAllowedHeaders(Collections.singletonList("*"));
		config.setAllowCredentials(true);
		
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		
		return source;
	}

}
