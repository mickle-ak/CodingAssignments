package org.mickleak.taskmanagementsystem.server.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mickleak.taskmanagementsystem.server.api.v1.LoginRequest;
import org.mickleak.taskmanagementsystem.server.api.v1.LoginResponse;
import org.mickleak.taskmanagementsystem.server.configuration.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SuppressWarnings( "DataFlowIssue" )
@WebMvcTest( controllers = LoginController.class )
@Import( WebSecurityConfig.class )
class LoginControllerWebMvcTest {

	@Autowired
	private LoginController loginController;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;


	@Test
	void login_OK() {
		LoginRequest loginRequest = createLoginRequest( "user", " " );

		final ResponseEntity<LoginResponse> response = loginController.login( loginRequest );

		assertThat( response.getStatusCode() ).isEqualTo( HttpStatus.OK );
		assertThat( response.getBody().getAccessToken() ).isNotNull();
		assertThat( jwtTokenProvider.validateToken( response.getBody().getAccessToken() ) ).isTrue();
	}

	@ParameterizedTest
	@CsvSource( {
		"user, unknown",
		"unknown, ' '",
		"unknown, unknown",
	} )
	void login_BadCredentials( final String username, final String password ) {
		LoginRequest loginRequest = createLoginRequest( username, password );

		assertThatThrownBy( () -> loginController.login( loginRequest ) )
			.isInstanceOf( BadCredentialsException.class );
	}


	private static LoginRequest createLoginRequest( final String username, final String password ) {
		return new LoginRequest().username( username ).password( password );
	}
}