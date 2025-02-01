package org.mickleak.taskmanagementsystem.server.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;


class JwtAuthenticationFilterTest {

	private final HttpServletRequest request = mock(HttpServletRequest.class);
	private final HttpServletResponse response = mock(HttpServletResponse.class);
	private final FilterChain filterChain = mock(FilterChain.class);

	private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
	private final JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider);


	@BeforeEach
	void setUp() {
		SecurityContextHolder.clearContext();
	}


	@Test
	void authorizationHeaderExists_AuthenticationSet() throws ServletException, IOException {
		final String token = jwtTokenProvider.createToken( "user", List.of() );
		doReturn( "Bearer " + token ).when( request ).getHeader( "Authorization" );

		jwtAuthenticationFilter.doFilterInternal( request, response, filterChain );

		assertThat( SecurityContextHolder.getContext().getAuthentication().getPrincipal() ).isEqualTo( "user" );
	}

	@Test
	void noAuthorizationHeaderExists_AuthenticationDoesNotSet() throws ServletException, IOException {

		doReturn( null ).when( request ).getHeader( "Authorization" );

		jwtAuthenticationFilter.doFilterInternal( request, response, filterChain );

		assertThat( SecurityContextHolder.getContext().getAuthentication() ).isNull();
	}

	@Test
	void invalidAuthorizationHeaderExists_AuthenticationDoesNotSet() throws ServletException, IOException {

		doReturn( "Bearer "+"invalid token value" ).when( request ).getHeader( "Authorization" );

		jwtAuthenticationFilter.doFilterInternal( request, response, filterChain );

		assertThat( SecurityContextHolder.getContext().getAuthentication() ).isNull();
	}
}