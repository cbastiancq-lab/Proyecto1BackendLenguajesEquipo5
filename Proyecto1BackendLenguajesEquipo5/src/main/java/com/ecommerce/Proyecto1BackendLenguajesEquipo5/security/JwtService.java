package com.ecommerce.Proyecto1BackendLenguajesEquipo5.security;

import com.ecommerce.Proyecto1BackendLenguajesEquipo5.config.JwtProperties;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Service
public class JwtService {

	private static final Base64.Encoder BASE64_URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
	private static final Base64.Decoder BASE64_URL_DECODER = Base64.getUrlDecoder();
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

		Map<String, Object> header = Map.of(
				"alg", "HS256",
				"typ", "JWT"
		);
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
		return username != null
				&& username.equals(userDetails.getUsername())
				&& !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractClaims(token)
				.map(claims -> toInstant(claims.get("exp")))
				.map(expiration -> !Instant.now().isBefore(expiration))
				.orElse(true);
	}

	private java.util.Optional<Map<String, Object>> extractClaims(String token) {
		try {
			String[] tokenParts = token.split("\\.");
			if (tokenParts.length != 3 || !isSignatureValid(tokenParts)) {
				return java.util.Optional.empty();
			}

			byte[] payload = BASE64_URL_DECODER.decode(tokenParts[1]);
			return java.util.Optional.of(objectMapper.readValue(payload, CLAIMS_TYPE));
		} catch (Exception ex) {
			return java.util.Optional.empty();
		}
	}

	private boolean isSignatureValid(String[] tokenParts) {
		String unsignedToken = tokenParts[0] + "." + tokenParts[1];
		String expectedSignature = sign(unsignedToken);
		return MessageDigest.isEqual(
				expectedSignature.getBytes(StandardCharsets.UTF_8),
				tokenParts[2].getBytes(StandardCharsets.UTF_8)
		);
	}

	private String encode(Map<String, Object> value) {
		try {
			return BASE64_URL_ENCODER.encodeToString(objectMapper.writeValueAsBytes(value));
		} catch (Exception ex) {
			throw new IllegalStateException("Could not encode JWT value", ex);
		}
	}

	private String sign(String value) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec key = new SecretKeySpec(
					jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8),
					"HmacSHA256"
			);
			mac.init(key);
			return BASE64_URL_ENCODER.encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
		} catch (Exception ex) {
			throw new IllegalStateException("Could not sign JWT", ex);
		}
	}

	private Instant toInstant(Object value) {
		if (value instanceof Number number) {
			return Instant.ofEpochSecond(number.longValue());
		}
		return Instant.ofEpochSecond(Long.parseLong(value.toString()));
	}
}
