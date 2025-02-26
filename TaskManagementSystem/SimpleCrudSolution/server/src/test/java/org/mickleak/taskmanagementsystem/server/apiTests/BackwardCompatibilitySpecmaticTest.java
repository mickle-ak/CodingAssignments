package org.mickleak.taskmanagementsystem.server.apiTests;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


class BackwardCompatibilitySpecmaticTest {

	@Test
	void backwardCompatitbilityTest() throws IOException, InterruptedException {
		String specmaticVersion = "2.5.0";
		String specmaticJarCanonicalPath = new File( "lib/specmatic-" + specmaticVersion + ".jar" ).getCanonicalPath().replace( '\\', '/' );
		String gitRoot = executeCommand( "git", "rev-parse", "--show-toplevel" ).output().trim();

		var executionResult = executeCommand( "java", "-jar", specmaticJarCanonicalPath, "backward-compatibility-check", "--repo-dir", gitRoot );

		System.out.println( "\n" + executionResult.output);

		assertThat( executionResult.exitCode )
			.as( "Incompatible changes in Rest API" )
			.isZero();
	}

	private ExecutionResult executeCommand( String... command ) throws IOException, InterruptedException {
		System.out.println( "> " + String.join( " ", command ) );
		ProcessBuilder pb = new ProcessBuilder();
		pb.command( command );
		Process process = pb.start();
		int rc = process.waitFor();
		try( var processOutputReader = process.inputReader() ) {
			String output = processOutputReader.lines().collect( Collectors.joining( System.lineSeparator() ) );
			return new ExecutionResult( rc, output );
		}
	}

	record ExecutionResult( int exitCode, String output ) {}
}
