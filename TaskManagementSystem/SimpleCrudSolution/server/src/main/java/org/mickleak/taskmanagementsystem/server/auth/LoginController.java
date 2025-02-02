package org.mickleak.taskmanagementsystem.server.auth;

import lombok.RequiredArgsConstructor;
import org.mickleak.taskmanagementsystem.server.api.v1.LoginApi;
import org.mickleak.taskmanagementsystem.server.api.v1.LoginRequest;
import org.mickleak.taskmanagementsystem.server.api.v1.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;

import java.util.List;


@RequiredArgsConstructor
@Controller
public class LoginController implements LoginApi {

	private final JwtTokenProvider tokenProvider;
	private final AuthenticationManager authenticationManager;

	@Override
	public ResponseEntity<LoginResponse> login( final LoginRequest loginRequest ) {
		final String accessToken = login( loginRequest.getUsername(), loginRequest.getPassword() );
		return ResponseEntity.ok( new LoginResponse().accessToken( accessToken ) );
	}

	private String login( String username, String password ) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken( username, password );
		Authentication authentication = authenticationManager.authenticate( authenticationToken );
		List<String> roles = authentication.getAuthorities().stream().map( GrantedAuthority::getAuthority ).toList();
		return tokenProvider.createToken( username, roles );
	}
}
