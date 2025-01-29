package org.mickleak.taskmanagementsystem.server.auth;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class JwtTokenProviderTest {

	private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

	@Test
	void createToken() {
		String token = jwtTokenProvider.createToken( "user", List.of() );
		assertNotNull( token );
	}

	@Test
	void getUsernameFromToken() {
		String token = jwtTokenProvider.createToken( "user", List.of() );
		String username = jwtTokenProvider.getUsernameFromToken( token );
		assertEquals( "user", username );
	}

	@Test
	void getAuthorities() {
		String token = jwtTokenProvider.createToken( "user", List.of( "ROLE_USER", "ROLE_ADMIN" ) );
		var authorities = jwtTokenProvider.getAuthorities( token );
		assertThat( authorities )
			.extracting( SimpleGrantedAuthority::getAuthority )
			.containsExactly( "ROLE_USER", "ROLE_ADMIN" );
	}

	@Test
	void validateToken_valid() {
		String token = jwtTokenProvider.createToken( "user", List.of() );
		assertTrue( jwtTokenProvider.validateToken( token ) );
	}

	@Test
	void validateToken_expired() {
		jwtTokenProvider.setExpirationInMilliseconds( -1 );
		String token = jwtTokenProvider.createToken( "user", List.of() );
		assertFalse( jwtTokenProvider.validateToken( token ) );
	}
}