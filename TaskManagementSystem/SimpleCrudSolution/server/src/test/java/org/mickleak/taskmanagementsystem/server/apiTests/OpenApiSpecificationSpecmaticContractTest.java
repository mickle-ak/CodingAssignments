package org.mickleak.taskmanagementsystem.server.apiTests;

import io.specmatic.test.SpecmaticContractTest;
import org.junit.jupiter.api.BeforeEach;
import org.mickleak.taskmanagementsystem.server.auth.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;


@SpringBootTest( webEnvironment = DEFINED_PORT, properties = { "server.port=8789" } )
@EnableAutoConfiguration( exclude = { DataSourceAutoConfiguration.class,
                                      JpaRepositoriesAutoConfiguration.class,
                                      HibernateJpaAutoConfiguration.class } )
class OpenApiSpecificationSpecmaticContractTest implements SpecmaticContractTest {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Value( "${server.port}" )
	private String port;

	@Value( "${openapi.simpleTaskManagementSystem.base-path:}" )
	private String basePath;


	@BeforeEach
	void setUp() {
		final String jwtToken = jwtTokenProvider.createToken( "admin", List.of( "ADMIN" ) );

		System.setProperty( "testBaseURL", "http://localhost:"+port+basePath );
		System.setProperty( "SPECMATIC_GENERATIVE_TESTS", "true" );
		System.setProperty( "SPECMATIC_TEST_PARALLELISM", "auto" );
		System.setProperty( "MAX_TEST_REQUEST_COMBINATIONS", "10" );
		System.setProperty( "BearerAuth", jwtToken );
		System.setProperty( "filterNot", "PATH=/login" );
		System.setProperty( "strictMode", "true" );
	}
}
