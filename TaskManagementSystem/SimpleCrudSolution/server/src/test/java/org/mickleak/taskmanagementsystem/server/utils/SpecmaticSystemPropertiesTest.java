package org.mickleak.taskmanagementsystem.server.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;


class SpecmaticSystemPropertiesTest {

	private final SpecmaticSystemProperties specmaticProperties = new SpecmaticSystemProperties();
	private       Properties                originalProperties;

	@BeforeEach
	void setUp() {
		// Save the original system properties
		originalProperties = (Properties) System.getProperties().clone();
	}

	@AfterEach
	void tearDown() {
		// Restore the original system properties
		System.setProperties( originalProperties );
	}


	@Test
	void beforeSetProperties() {
		final Properties allSystemProperties = System.getProperties();
		assertThat( allSystemProperties.stringPropertyNames() )
			.doesNotContainAnyElementsOf( specmaticProperties.getPropertyNames() ); // NOSONAR: can not be empty
	}

	@Test
	void setProperties() {
		final int initialNumberOfSystemProperties = System.getProperties().size();

		specmaticProperties
			.host( "localhost" )
			.port( 8789 )
			.generativeTests( true )
			.testParallelism( "auto" )
			.maxTestRequestCombinations( 10 )
			.filter( "METHOD=POST;PATH=/login" )
			.filterNot( "METHOD=GET;PATH=/login" )
			.authorization( "BearerAuth", "jwtToken" );

		assertThat( System.getProperties() )
			.hasSize( initialNumberOfSystemProperties + specmaticProperties.getPropertyNames().size() )
			.containsKeys( specmaticProperties.getPropertyNames().toArray() );
	}

	@Test
	void cleanProperties() {
		final int initialSizeOfSystemProperties = System.getProperties().size();
		final int initialSizeOfSpecmaticProperties = specmaticProperties.getPropertyNames().size();

		specmaticProperties
			.host( "localhost" )
			.port( 8789 )
			.generativeTests( true )
			.testParallelism( "auto" )
			.maxTestRequestCombinations( 10 )
			.authorization( "BearerAuth", "jwtToken" );

		specmaticProperties.clear();

		assertThat( System.getProperties() )
			.hasSize( initialSizeOfSystemProperties )
			.doesNotContainKeys( specmaticProperties.getPropertyNames() );
		assertThat( specmaticProperties.getPropertyNames() ).hasSize( initialSizeOfSpecmaticProperties );
	}
}