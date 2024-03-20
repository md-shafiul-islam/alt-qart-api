package com.altqart.security.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.altqart.model.User;
import com.altqart.services.UserServices;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;

@EnableConfigurationProperties
@Slf4j
@Component
public class JwtTokenProvider {

	@Autowired
	private UserServices userServices;

	@Value("${user.token}")
	private String secretKey;

	// Generate the token
	// The JWT signature algorithm we will be using to sign the token
	private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	public String generateToken(Authentication authentication) {

		User user = (User) authentication.getPrincipal();
		Date now = new Date(System.currentTimeMillis());

		Date expiryDate = new Date(now.getTime() + SecurityConstants.EXP_TIME);

		String role = "";
		if (user.getRole() != null) {
			role = user.getRole().getName();

		} else {
			role = "User";
		}

		Map<String, Object> claims = new HashMap<>();
		claims.put("id", (user.getPublicId()));
		claims.put("username", user.getUsername());
		claims.put("fullName", user.getName());

		claims.put("role", role);

		return createJWT(user.getPublicId(), "alt-qart", now, expiryDate, claims);
	}

	private String createJWT(String id, String subject, Date now, Date expiryDate, Map<String, Object> claims) {
		// We will sign our JWT with our ApiKey secret
		JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(now).setSubject(subject).setExpiration(expiryDate)
				.setIssuer("IMS-System").setClaims(claims).signWith(signatureAlgorithm, getSecretKeySpec());

		return builder.compact();
	}

	// Validate the token
	public boolean validateToken(String token) {

		String userId = getUserIdFromJWT(token);
		User user = userServices.getOnlyUserByPublicID(userId);

		try {
			SecretKey signingKey = getSecretKeySpec();
			JwtParser jwtParser = Jwts.parser().verifyWith(signingKey).build();

			jwtParser.parse(token);

			if (user != null) {

				if (!user.isEnabled()) {
					throw new Exception("User Acoount is temporary Disable, Please contact administrator!");
				}

				if (!user.isAccountNonLocked()) {
					throw new Exception("User Acoount is temporary locked, Please contact administrator!");
				}
			} else {
				throw new Exception("User token validate failed. User is null, Please contact administrator!");
			}
			
			
			return true;
		} catch (SignatureException ex) {
			log.info("Invalid JWT Signature");
		} catch (MalformedJwtException ex) {
			log.info("Invalid JWT Token");
		} catch (ExpiredJwtException ex) {
			log.info("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			log.info("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			log.info("JWT claims string is empty");
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return false;
	}

	private SecretKey getSecretKeySpec() {
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
		SecretKey signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		return signingKey;
	}

	// Get user Id from token

	public String getUserIdFromJWT(String token) {

		JwtParser jwtParser = Jwts.parser().verifyWith(getSecretKeySpec()).build();

		Claims claims = (Claims) jwtParser.parse(token).getBody();

		return (String) claims.get("id");
	}

}
