package org.mickleak.taskmanagementsystem.server.auth;

import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mickleak.taskmanagementsystem.server.api.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest( webEnvironment = RANDOM_PORT )
@EnableAutoConfiguration( exclude = { DataSourceAutoConfiguration.class,
                                      JpaRepositoriesAutoConfiguration.class,
                                      HibernateJpaAutoConfiguration.class } )
class LoginControllerOverNetworkTest {

	@LocalServerPort
	private int port;

	@Autowired
	private LoginController loginController;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	private RequestSpecification request;


	@BeforeEach
	void setUp() {
		request = given()
			.baseUri( "http://localhost:" + port )
			.contentType( "application/json" );
	}


	@Test
	void login_OK() {
		LoginRequest loginRequest = createLoginRequest( "user", " " );
		request.body( loginRequest )
		       .when().post( "/login" )
		       .then().statusCode( 200 );
	}

	@ParameterizedTest
	@CsvSource( {
		"user, unknown",
		"unknown, ' '",
		"unknown, unknown",
	} )
	void login_BadCredentials( final String username, final String password ) {
		LoginRequest loginRequest = createLoginRequest( username, password );
		request.body( loginRequest )
		       .when().post( "/login" )
		       .then()
		       .statusCode( 401 )
		       .header( "WWW-Authenticate", containsString( "Bearer realm=\"TaskManagementSystem\"" ) )
		       .extract().response().asString().contains( "Bad credentials" );
	}


	private static LoginRequest createLoginRequest( final String username, final String password ) {
		return new LoginRequest().username( username ).password( password );
	}
}