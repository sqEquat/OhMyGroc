package ru.treshchilin.OhMyGroc.security.jwts;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JwtProvider {

	private final JwtConfig config;
	private Algorithm algorithm;
	private JWTVerifier  verifier;

	public JwtProvider(JwtConfig config) {
		this.config = config;
	}
	
	@PostConstruct
	private void init() {
		algorithm = Algorithm.HMAC256(config.getSecretKey().getBytes());
		verifier = JWT.require(algorithm).build();
	}
	
	public String createAccessToken(String subject, String issuer, List<String> roles) {
		return JWT.create()
				.withSubject(subject)
				.withExpiresAt(new Date(System.currentTimeMillis() + config.getTokenExpirationMinutes() * 60 * 1000))
				.withIssuer(issuer)
				.withClaim("roles", roles)
				.sign(algorithm);
	}
	
	public DecodedJWT verifyAndDecodeToken(String token) {
		return verifier.verify(token);
	}
}
