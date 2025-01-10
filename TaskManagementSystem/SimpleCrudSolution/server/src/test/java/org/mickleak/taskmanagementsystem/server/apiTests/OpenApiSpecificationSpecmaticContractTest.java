package org.mickleak.taskmanagementsystem.server.apiTests;

import io.specmatic.test.SpecmaticContractTest;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;


@SpringBootTest( webEnvironment = DEFINED_PORT, properties = { "server.port=8789" } )
@EnableAutoConfiguration( exclude = { DataSourceAutoConfiguration.class,
                                      JpaRepositoriesAutoConfiguration.class,
                                      HibernateJpaAutoConfiguration.class } )
class OpenApiSpecificationSpecmaticContractTest implements SpecmaticContractTest {

	@BeforeAll
	static void setUp() {
		System.setProperty( "host", "localhost" );
		System.setProperty( "port", "8789" );
		System.setProperty( "SPECMATIC_GENERATIVE_TESTS", "true" );
		System.setProperty( "SPECMATIC_TEST_PARALLELISM", "auto" );
	}
}
