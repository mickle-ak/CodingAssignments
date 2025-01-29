package org.mickleak.taskmanagementsystem.server.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider tokenProvider;

	@Override
	protected void doFilterInternal( final HttpServletRequest request,
	                                 final HttpServletResponse response,
	                                 final FilterChain filterChain )
		throws ServletException, IOException {

		String token = extractToken( request );
		if( token != null && tokenProvider.validateToken( token ) ) {
			var username = tokenProvider.getUsernameFromToken( token );
			var authorities = tokenProvider.getAuthorities( token );
			var authentication = new UsernamePasswordAuthenticationToken( username, null, authorities );
			authentication.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );
			SecurityContextHolder.getContext().setAuthentication( authentication );
		}
		filterChain.doFilter( request, response );
	}

	@Nullable
	private String extractToken( HttpServletRequest request ) {
		String bearerToken = request.getHeader( "Authorization" );
		if( bearerToken != null && bearerToken.startsWith( "Bearer " ) ) {
			return bearerToken.substring( 7 );
		}
		return null;
	}
}
