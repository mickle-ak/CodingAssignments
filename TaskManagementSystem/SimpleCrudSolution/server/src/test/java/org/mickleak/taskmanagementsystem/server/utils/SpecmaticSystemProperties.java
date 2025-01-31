package org.mickleak.taskmanagementsystem.server.utils;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@SuppressWarnings( "UnusedReturnValue" )
public class SpecmaticSystemProperties {


	private static final Set<String> STATIC_SPECMATIC_PROPERTY_NAMES = Set.of(
		"host", "port",
		"SPECMATIC_GENERATIVE_TESTS",
		"SPECMATIC_TEST_PARALLELISM",
		"MAX_TEST_REQUEST_COMBINATIONS",
		"filter", "filterNot" );

	private final Set<String> variableSpecmaticPropertyNames = new HashSet<>();


	public Set<String> getPropertyNames() {
		return Stream.concat( STATIC_SPECMATIC_PROPERTY_NAMES.stream(),
		                      variableSpecmaticPropertyNames.stream() )
		             .collect( Collectors.toUnmodifiableSet() );
	}


	@NotNull
	public SpecmaticSystemProperties host( final String hostname ) {
		return setStaticSpecmaticProperty( "host", hostname );
	}

	@NotNull
	public SpecmaticSystemProperties port( final int port ) {
		return setStaticSpecmaticProperty( "port", String.valueOf( port ) );
	}

	@NotNull
	public SpecmaticSystemProperties generativeTests( final boolean generativeTests ) {
		return setStaticSpecmaticProperty( "SPECMATIC_GENERATIVE_TESTS", String.valueOf( generativeTests ) );
	}

	@NotNull
	public SpecmaticSystemProperties testParallelism( final String testParallelism ) {
		return setStaticSpecmaticProperty( "SPECMATIC_TEST_PARALLELISM", testParallelism );
	}

	@NotNull
	public SpecmaticSystemProperties maxTestRequestCombinations( final int maxTestRequestCombinations ) {
		return setStaticSpecmaticProperty( "MAX_TEST_REQUEST_COMBINATIONS", String.valueOf( maxTestRequestCombinations ) );
	}

	@NotNull
	public SpecmaticSystemProperties filter( final String filter ) {
		return setStaticSpecmaticProperty( "filter", filter );
	}

	@NotNull
	public SpecmaticSystemProperties filterNot( final String filterNot ) {
		return setStaticSpecmaticProperty( "filterNot", filterNot );
	}

	public SpecmaticSystemProperties authorization( final String authName, final String token ) {
		return setVariableSpecmaticProperty( authName, token );
	}


	public void clear() {
		getPropertyNames().forEach( System::clearProperty );
		variableSpecmaticPropertyNames.clear();
	}


	@NotNull
	private SpecmaticSystemProperties setStaticSpecmaticProperty( final String name, final String value ) {
		if( !STATIC_SPECMATIC_PROPERTY_NAMES.contains( name ) ) {
			throw new IllegalArgumentException( "Property name is not supported: " + name );
		}
		System.setProperty( name, value );
		return this;
	}

	@NotNull
	private SpecmaticSystemProperties setVariableSpecmaticProperty( final String authName, final String token ) {
		variableSpecmaticPropertyNames.add( authName );
		System.setProperty( authName, token );
		return this;
	}
}