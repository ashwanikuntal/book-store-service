package com.trs.bookstoreservice.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.trs.bookstoreservice.exception.AuthenticationException;
import com.trs.bookstoreservice.services.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

	private String jwtSecret;

	private UserService userService;

	public void setJwtSecret(String s) {
		this.jwtSecret = s;
	}

	public void configureDependencies(UserService userService) {
		this.userService = userService;
	}

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (request.getRequestURI().startsWith("/api/") || request.getRequestURI().startsWith("health")
				|| request.getRequestURI().startsWith("/swagger")
				|| request.getRequestURI().startsWith("/webjars/springfox-swagger-ui")) {

			chain.doFilter(request, response);
			return;
		}

		try {
			UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
			return;
		}

		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request)
			throws AuthenticationException {

		String token = request.getHeader("Authorization");

		if (token != null) {
			final Claims claims = Jwts.parser().setSigningKey(jwtSecret.getBytes())
					.parseClaimsJws(token.replace("Bearer ", "")).getBody();

			String username = claims.getSubject();

			if (username != null) {
				ArrayList<GrantedAuthority> authorities = new ArrayList<>();
				authorities.add(new SimpleGrantedAuthority("USER"));

				// TODO: validate JWT token with Redis Cache and set the request.setAttribute
				// for User Object

				return new UsernamePasswordAuthenticationToken(username, null, authorities);
			}
			// No user found in subject of JWT token. Thus throwing AuthenticationException
			throw new AuthenticationException("Failed to find user in Subject");
		}
		logger.debug("Missing Authorization Header");
		throw new AuthenticationException("Missing Authorization Header");

	}

}
