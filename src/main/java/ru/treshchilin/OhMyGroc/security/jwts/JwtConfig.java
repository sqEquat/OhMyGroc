package ru.treshchilin.OhMyGroc.security.jwts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConfig {

	private String secretKey;
	private Integer tokenExpirationMinutes;
	
	public JwtConfig(
			@Value("${jwt.secret}") String secretKey, 
			@Value("${jwt.expiration}") Integer tokenExpirationMinutes) {
		this.secretKey = secretKey;
		this.tokenExpirationMinutes = tokenExpirationMinutes;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public Integer getTokenExpirationMinutes() {
		return tokenExpirationMinutes;
	}
}
