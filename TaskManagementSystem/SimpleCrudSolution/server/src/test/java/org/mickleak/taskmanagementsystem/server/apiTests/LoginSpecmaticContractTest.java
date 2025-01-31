package org.mickleak.taskmanagementsystem.server.apiTests;

import io.specmatic.test.SpecmaticContractTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.mickleak.taskmanagementsystem.server.utils.SpecmaticSystemProperties;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@Disabled
@SpringBootTest( webEnvironment = DEFINED_PORT, properties = { "server.port=8789" } )
@EnableAutoConfiguration( exclude = { DataSourceAutoConfiguration.class,
                                      JpaRepositoriesAutoConfiguration.class,
                                      HibernateJpaAutoConfiguration.class } )
class LoginSpecmaticContractTest implements SpecmaticContractTest {

	private static final SpecmaticSystemProperties specmaticProperties = new SpecmaticSystemProperties();

	@BeforeEach
	void setUp() {
		specmaticProperties
			.host( "localhost" )
			.port( 8789 )
			.generativeTests( false )
			.filter( "PATH=/login" );
	}

	@AfterEach
	void tearDown() {
		specmaticProperties.clear();
	}
}
