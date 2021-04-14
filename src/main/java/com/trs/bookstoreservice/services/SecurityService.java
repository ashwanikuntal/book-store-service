package com.trs.bookstoreservice.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import com.trs.bookstoreservice.domain.User;
import com.trs.bookstoreservice.domain.UserAuth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class SecurityService {

	@Value("${jwt.secret}")
	private String jwtSecret;
	
	@Value("${expirationTimeInMilliseconds")
	private String expirationTime;
	
	
	//Call this method from UserController API which would take User Object as request body and will save the UserAuth
	// object in database for authentication purpose and will return JWT bearer token which will further be used to access other
	// endpoints (other than /swagger and /health)
	public String storeTokenInCache(User user) {
		
		String jwtToken = Jwts.builder().setSubject(user.getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(this.expirationTime)))
				.signWith(SignatureAlgorithm.HS512, this.jwtSecret.getBytes()).compact();
		
		UserAuth userAuth = new UserAuth();
		userAuth.setId(jwtToken);
		userAuth.setUsername(user.getUsername());
		
		//Save this UserAuth Object to Database and authenticate each time any service is called.
		return jwtToken;
		
	}
}
