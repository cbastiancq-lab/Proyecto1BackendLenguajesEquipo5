package com.ecommerce.proyecto.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

	private String secret = "change-this-development-secret-before-production-1234567890";
	private long expirationMs = 86400000;

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public long getExpirationMs() {
		return expirationMs;
	}

	public void setExpirationMs(long expirationMs) {
		this.expirationMs = expirationMs;
	}
}
