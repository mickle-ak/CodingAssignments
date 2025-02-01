package org.mickleak.taskmanagementsystem.server;

import org.mickleak.taskmanagementsystem.server.auth.JwtTokenProvider;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class TestServerApplication {

	public static void main( String[] args ) {
		SpringApplication.from( ServerApplication::main ).with( TestcontainersConfiguration.class ).run( args );
	}

	@Bean
	public ApplicationRunner applicationRunner( JwtTokenProvider jwtTokenProvider ) {
		return args -> {
			final String token = jwtTokenProvider.createToken( "admin", List.of( "ADMIN" ) );
			System.out.println( "\n\n\n *****     admin token: " + token + "\n\n\n" );
		};
	}
}
