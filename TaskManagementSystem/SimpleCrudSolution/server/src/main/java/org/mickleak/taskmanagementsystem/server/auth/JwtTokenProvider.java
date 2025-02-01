package org.mickleak.taskmanagementsystem.server.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;


@Component
public class JwtTokenProvider {

	public static final String ROLES_CLAIM_NAME = "roles";

	private final SecretKey signingKey = Jwts.SIG.HS256.key().build();

	@Value("${jwt.expiration-time.ms}")
	@Setter( AccessLevel.PACKAGE)
	private long expirationInMilliseconds = 36_000_000; // 10 hours per default


	public String createToken( String username, final List<String> roles ) {
		Instant now = Instant.now();
		return Jwts.builder()
		           .claims( Jwts.claims().subject( username ).add( ROLES_CLAIM_NAME, roles ).build() )
		           .issuedAt( Date.from( now ) )
		           .expiration( Date.from( now.plusMillis( expirationInMilliseconds ) ) )
		           .signWith( signingKey )
		           .compact();
	}

	public String getUsernameFromToken( String token ) {
		return getClaims( token ).getSubject();
	}

	@SuppressWarnings( "unchecked" )
	public List<SimpleGrantedAuthority> getAuthorities( String token ) {
		Claims claims = getClaims(token);
		List<String> roles = (List<String>) claims.get( ROLES_CLAIM_NAME );
		return roles.stream().map(SimpleGrantedAuthority::new).toList();
	}

	public boolean validateToken( String token ) {
		try {
			Claims claims = getClaims( token );
			return !claims.getExpiration().before( new Date() );
		}
		catch( Exception e ) {
			return false;
		}
	}

	private Claims getClaims( String token ) {
		return Jwts.parser()
		           .verifyWith( signingKey )
		           .build()
		           .parseSignedClaims( token )
		           .getPayload();
	}
}
