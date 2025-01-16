package org.mickleak.taskmanagementsystem.server.configuration;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

	public static final String TMS_SECURITY_FILTER_CHAIN_BEAN_NAME = "tmsSecurityFilterChain";

	@Bean( TMS_SECURITY_FILTER_CHAIN_BEAN_NAME )
	public SecurityFilterChain filterChain( HttpSecurity http,
	                                        AuthenticationEntryPoint authenticationEntryPoint ) throws Exception {
		return http
			.cors( Customizer.withDefaults() )
			.authorizeHttpRequests(
				authorizeRequests -> authorizeRequests
					.dispatcherTypeMatchers( DispatcherType.ERROR, DispatcherType.FORWARD ).permitAll()
					.anyRequest().permitAll() )
			.csrf( AbstractHttpConfigurer::disable )
			.formLogin( AbstractHttpConfigurer::disable )
			.httpBasic( AbstractHttpConfigurer::disable )
			.sessionManagement( sessionManagement -> sessionManagement.sessionCreationPolicy( STATELESS ) )
			.exceptionHandling( exceptionHandling -> exceptionHandling.authenticationEntryPoint( authenticationEntryPoint ) )
			.build();
	}

	@Bean
	public AuthenticationManager authenticationManager( HttpSecurity http,
	                                                    UserDetailsService userDetailsService,
	                                                    PasswordEncoder passwordEncoder ) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject( AuthenticationManagerBuilder.class );
		authenticationManagerBuilder.userDetailsService( userDetailsService ).passwordEncoder( passwordEncoder );
		return authenticationManagerBuilder.build();
	}

	@Bean
	public UserDetailsService userDetailsService( PasswordEncoder passwordEncoder ) {
		return new InMemoryUserDetailsManager(
			User.withUsername( "user" ).password( passwordEncoder.encode( " " ) ).roles( "USER" ).build(), // NOSONAR: test password
			User.withUsername( "user2" ).password( passwordEncoder.encode( " " ) ).roles( "USER" ).build(), // NOSONAR: test password
			User.withUsername( "admin" ).password( passwordEncoder.encode( " " ) ).roles( "ADMIN" ).build() // NOSONAR: test password
		);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// To return 401 instead of 403 on bad credentials (and if no authorization happened at all).
	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return (request, response, authException) -> {
			response.addHeader( "WWW-Authenticate", "Bearer realm=\"TaskManagementSystem\"" );
			response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized" );
		};
	}
}
