package com.ecommerce.proyecto.security;

import com.ecommerce.proyecto.config.JwtProperties;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Service
public class JwtService {

	private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();
	private static final Base64.Decoder DECODER = Base64.getUrlDecoder();
	private static final TypeReference<Map<String, Object>> CLAIMS_TYPE = new TypeReference<>() {
	};

	private final JwtProperties jwtProperties;
	private final ObjectMapper objectMapper;

	public JwtService(JwtProperties jwtProperties, ObjectMapper objectMapper) {
		this.jwtProperties = jwtProperties;
		this.objectMapper = objectMapper;
	}

	public String generateToken(UserDetails userDetails) {
		long issuedAt = Instant.now().getEpochSecond();
		long expiration = issuedAt + jwtProperties.getExpirationMs() / 1000;

		Map<String, Object> header = Map.of("alg", "HS256", "typ", "JWT");
		Map<String, Object> claims = new HashMap<>();
		claims.put("sub", userDetails.getUsername());
		claims.put("iat", issuedAt);
		claims.put("exp", expiration);

		String unsignedToken = encode(header) + "." + encode(claims);
		return unsignedToken + "." + sign(unsignedToken);
	}

	public String extractUsername(String token) {
		return extractClaims(token).map(claims -> (String) claims.get("sub")).orElse(null);
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		return username != null && username.equals(userDetails.getUsername()) && !isExpired(token);
	}

	private boolean isExpired(String token) {
		return extractClaims(token)
				.map(claims -> toInstant(claims.get("exp")))
				.map(expiration -> !Instant.now().isBefore(expiration))
				.orElse(true);
	}

	private Optional<Map<String, Object>> extractClaims(String token) {
		try {
			String[] parts = token.split("\\.");
			if (parts.length != 3 || !isSignatureValid(parts)) {
				return Optional.empty();
			}
			return Optional.of(objectMapper.readValue(DECODER.decode(parts[1]), CLAIMS_TYPE));
		} catch (Exception ex) {
			return Optional.empty();
		}
	}

	private boolean isSignatureValid(String[] parts) {
		String unsignedToken = parts[0] + "." + parts[1];
		String expectedSignature = sign(unsignedToken);
		return MessageDigest.isEqual(
				expectedSignature.getBytes(StandardCharsets.UTF_8),
				parts[2].getBytes(StandardCharsets.UTF_8)
		);
	}

	private String encode(Map<String, Object> value) {
		try {
			return ENCODER.encodeToString(objectMapper.writeValueAsBytes(value));
		} catch (Exception ex) {
			throw new IllegalStateException("Could not encode token", ex);
		}
	}

	private String sign(String value) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec key = new SecretKeySpec(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
			mac.init(key);
			return ENCODER.encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
		} catch (Exception ex) {
			throw new IllegalStateException("Could not sign token", ex);
		}
	}

	private Instant toInstant(Object value) {
		if (value instanceof Number number) {
			return Instant.ofEpochSecond(number.longValue());
		}
		return Instant.ofEpochSecond(Long.parseLong(value.toString()));
	}
}
