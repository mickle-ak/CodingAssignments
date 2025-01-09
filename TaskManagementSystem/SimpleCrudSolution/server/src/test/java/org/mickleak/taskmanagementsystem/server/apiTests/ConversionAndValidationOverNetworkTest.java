package org.mickleak.taskmanagementsystem.server.apiTests;


import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mickleak.taskmanagementsystem.server.api.Task;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest( webEnvironment = RANDOM_PORT )
@EnableAutoConfiguration( exclude = { DataSourceAutoConfiguration.class,
                                      JpaRepositoriesAutoConfiguration.class,
                                      HibernateJpaAutoConfiguration.class } )
class ConversionAndValidationOverNetworkTest {

	@LocalServerPort
	private int port;

	private RequestSpecification request;


	@BeforeEach
	void setUp() {
		request = given()
			.baseUri( "http://localhost:" + port )
			.contentType( "application/json" );
	}


	@Test
	void checkConversion_expectedError400() {
		request
			.when().get( "/tasks/invalid-id" ) // non-integer id => 400
			.then().statusCode( 400 );
	}

	@Test
	void checkValidation_expectedError400() {
		final Task invalidTask = new Task( null, null ); // id and title are required and must not be null => 400
		request.body( invalidTask )
		       .when().post( "/tasks" )
		       .then().statusCode( 400 );
	}

	@Test
	void checkConversionAndValidation_expectedOK() {
		final Task correctTask = new Task( 12, "title" );
		request.body( correctTask )
		       .when().put( "/tasks/" + correctTask.getId() )
		       .then().statusCode( 200 );
	}
}
